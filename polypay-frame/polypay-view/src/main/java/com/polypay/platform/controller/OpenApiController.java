package com.polypay.platform.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantApi;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantFrezzon;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.bean.PayType;
import com.polypay.platform.bean.SystemConsts;
import com.polypay.platform.consts.MerchantAccountInfoStatusConsts;
import com.polypay.platform.consts.MerchantFinanceStatusConsts;
import com.polypay.platform.consts.MerchantOrderTypeConsts;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.SystemConstans;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IMerchantApiService;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantFrezzService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.service.IPayTypeService;
import com.polypay.platform.service.ISystemConstsService;
import com.polypay.platform.utils.DateUtil;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MD5Utils;
import com.polypay.platform.utils.RandomUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@RestController
public class OpenApiController {

	private static final String RECHARGE = "R";

	private static final Integer DEFAULT_LEVEL = 1;

	private static final BigDecimal FREZZ_RATE = new BigDecimal(0.1);
	
	private Logger log = LoggerFactory.getLogger(OpenApiController.class);

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;

	@Autowired
	private ISystemConstsService systemConstsService;

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;

	@Autowired
	private IMerchantApiService merchantApiService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;
	
	@Autowired
	private IMerchantFrezzService merchantFrezzService;

	@Autowired
	private IPayTypeService payTypeService;
	
	@RequestMapping("/open/api/recharge")
	public ServiceResponse recharge(HttpServletRequest request, HttpServletResponse response) {
		ServiceResponse result = new ServiceResponse();

		try {
			List<String> keys = Lists.newArrayList();

			// 签名校验 map
			Map<String, Object> signMap = Maps.newHashMap();
			String merchantUUID = getParameter(request, "merchant_id");
			signMap.put("merchant_id", merchantUUID);
			if (StringUtils.isEmpty(merchantUUID)) {
				ResponseUtils.exception(result, "商户ID不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}
			keys.add("merchant_id");

			// 必传商户订单号
			String merchantOrderNumber = getParameter(request, "order_number");
			signMap.put("order_number", merchantOrderNumber);
			if (StringUtils.isEmpty(merchantOrderNumber)) {
				ResponseUtils.exception(result, "订单ID不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}
			keys.add("order_number");

			// 订单金额必传
			String orderAmount = getParameter(request, "pay_amount");
			signMap.put("pay_amount", orderAmount);
			if (StringUtils.isEmpty(orderAmount)) {
				ResponseUtils.exception(result, "支付金额不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}
			keys.add("pay_amount");
			try {
				Double.parseDouble(orderAmount);
			} catch (Exception e) {
				ResponseUtils.exception(result, "支付金额填写错误！", RequestStatus.FAILED.getStatus());
				return result;
			}

			String time = getParameter(request, "time");
			signMap.put("time", time);
			if (StringUtils.isEmpty(time)) {
				ResponseUtils.exception(result, "时间撮不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}
			keys.add("time");

			String bankCode = getParameter(request, "bank_code");
			signMap.put("bank_code", bankCode);
			if (StringUtils.isEmpty(bankCode)) {
				ResponseUtils.exception(result, "银行号不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}
			keys.add("bank_code");

			String payChannel = getParameter(request, "pay_channel");
			signMap.put("pay_channel", payChannel);
			if (StringUtils.isEmpty(payChannel)) {
				ResponseUtils.exception(result, "支付通道不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}
			keys.add("pay_channel");

			String sign = getParameter(request, "sign");
			if (StringUtils.isEmpty(sign)) {
				ResponseUtils.exception(result, "签名不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}

			String securityKey = getParameter(request, "security_key");

			// 校验商户资格
			checkMerchantId(merchantUUID, result);
			if (!RequestStatus.SUCCESS.getStatus().equals(result.getStatus())) {
				return result;
			}

			// 校验商户秘钥
			MerchantApi merchantApi = checkSerurityKey(result, merchantUUID, securityKey);
			if (!RequestStatus.SUCCESS.getStatus().equals(result.getStatus())) {
				return result;
			}

			// 校验签名是否正确
			if (!checkSign(signMap, sign, keys, merchantApi.getMd5Key())) {
				ResponseUtils.exception(result, "簽名不正确！", RequestStatus.FAILED.getStatus());
				return result;
			}

			// 校验账户财务信息
			checkMerchantFinance(result, merchantUUID);
			if (!RequestStatus.SUCCESS.getStatus().equals(result.getStatus())) {
				return result;
			}

			// 校验订单是否重复
			checkMerchantOrderNumber(result, merchantOrderNumber);
			if (!RequestStatus.SUCCESS.getStatus().equals(result.getStatus())) {
				return result;
			}

			// 生成订单
			generatorOrder(merchantUUID, signMap);

			// 请求转发到第三方
			sendRediect(response, signMap);

		} catch (Exception e) {
			result.setMessage(e.getMessage());
			return result;
		}

		return result;
	}

	@RequestMapping("/open/api/recharge/back")
	public String rechargeOrderBack(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		
		String success = getParameter(request, "success");
		String merchantOrderNumber = getParameter(request, "merchant_order_number");

		MerchantRechargeOrder orderByMerchantOrderNumber = merchantRechargeOrderService
				.getOrderByOrderNumber(merchantOrderNumber);

		if (null == orderByMerchantOrderNumber) {
			return "fail";
		}

		if (StringUtils.isEmpty(success)) {
			orderByMerchantOrderNumber.setStatus(OrderStatusConsts.FAIL);
			merchantRechargeOrderService.updateByPrimaryKeySelective(orderByMerchantOrderNumber);
			return "fail";
		}

		if (!success.equals("true")) {
			orderByMerchantOrderNumber.setStatus(OrderStatusConsts.FAIL);
			merchantRechargeOrderService.updateByPrimaryKeySelective(orderByMerchantOrderNumber);
			return "fail";
		}

		String merchantId = orderByMerchantOrderNumber.getMerchantId();

		// 原有资金
		BigDecimal resourceAmount;
		// 原冻结资金
		BigDecimal resourceFrezzAmount;

		// 订单金额
		BigDecimal payAmount;
		// 费率
		Double rate;
		// 手续费
		BigDecimal poundage;
		// 到账金额
		BigDecimal arrivalAmount;

		// 冻结金额
		BigDecimal frezzAmount;

		synchronized (merchantId.intern()) {
			
			MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchantId);

			if (null == merchantFinance) {
				return "fail";
			}

			resourceAmount = merchantFinance.getBlanceAmount();
			if (null == resourceAmount) {
				resourceAmount = new BigDecimal(0);
			}
			resourceFrezzAmount = merchantFinance.getFronzeAmount();
			if (null == resourceFrezzAmount) {
				resourceFrezzAmount = new BigDecimal(0);
			}

			rate = getRate(orderByMerchantOrderNumber);

			payAmount = orderByMerchantOrderNumber.getPayAmount();

			poundage = payAmount.multiply(new BigDecimal(rate)).setScale(4,BigDecimal.ROUND_HALF_UP);

			arrivalAmount = payAmount.subtract(poundage);

			frezzAmount = arrivalAmount.multiply(FREZZ_RATE).setScale(4,BigDecimal.ROUND_HALF_UP);

			
			//统一入库
			endInsertOrder(orderByMerchantOrderNumber, resourceAmount, resourceFrezzAmount, poundage, arrivalAmount,
					frezzAmount, merchantFinance);
		}

		return "success";
	}
	
	@Transactional(rollbackFor=Exception.class)
	private void endInsertOrder(MerchantRechargeOrder orderByMerchantOrderNumber, BigDecimal resourceAmount,
			BigDecimal resourceFrezzAmount, BigDecimal poundage, BigDecimal arrivalAmount, BigDecimal frezzAmount,
			MerchantFinance merchantFinance) throws ServiceException {
		
		try {
		// 修改订单入库
		saveRechargeOrder(orderByMerchantOrderNumber, poundage, arrivalAmount);

		MerchantFrezzon merchantFrezzon  = new MerchantFrezzon();
		merchantFrezzon.setAmount(frezzAmount);
		merchantFrezzon.setFrezzTime(new Date());
		merchantFrezzon.setArrivalTime(DateUtils.getAfterDayDate("1"));
		merchantFrezzon.setStatus(MerchantFinanceStatusConsts.FREEZE);
		
		merchantFrezzService.insertSelective(merchantFrezzon);
		merchantFinance.setBlanceAmount(resourceAmount.add(arrivalAmount.subtract(frezzAmount)));
		merchantFinance.setFronzeAmount(resourceFrezzAmount.add(frezzAmount));
		merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
		}catch(Exception e)
		{
		log.error("save order back error");	
		}
	}
	
	private void saveRechargeOrder(MerchantRechargeOrder orderByMerchantOrderNumber, BigDecimal poundage,
			BigDecimal arrivalAmount) throws ServiceException {
		orderByMerchantOrderNumber.setStatus(OrderStatusConsts.SUCCESS);
		orderByMerchantOrderNumber.setServiceAmount(poundage);
		orderByMerchantOrderNumber.setSuccessTime(new Date());
		orderByMerchantOrderNumber.setArrivalAmount(arrivalAmount);
		merchantRechargeOrderService.updateByPrimaryKeySelective(orderByMerchantOrderNumber);
	}

	private Double getRate(MerchantRechargeOrder orderByMerchantOrderNumber) throws ServiceException {
		Integer payLevel;
		String payChannel;
		payLevel = getPayLevel(orderByMerchantOrderNumber.getMerchantId());
		payChannel = orderByMerchantOrderNumber.getPayChannel();
		PayType payType = payTypeService.getRateByLevelAndChannel(payLevel, payChannel);
		if (null == payType) {
			return 0.02;
		}
		return payType.getRate() / 1000.0;
	}

	private Integer getPayLevel(String merchantId) throws ServiceException {
		MerchantAccountInfo merchantInfo;
		Integer payLevel;
		MerchantAccountInfoVO merchantAccountInfoVO = new MerchantAccountInfoVO();
		merchantAccountInfoVO.setUuid(merchantId);
		merchantInfo = merchantAccountInfoService.getMerchantInfoByUUID(merchantAccountInfoVO);
		payLevel = merchantInfo.getPayLevel();
		if (null == payLevel) {
			payLevel = DEFAULT_LEVEL;
		}
		return payLevel;
	}

	private void sendRediect(HttpServletResponse response, Map<String, Object> signMap)
			throws ServiceException, IOException {
		SystemConsts consts = systemConstsService.getConsts(SystemConstans.RECHARGE_REST_URL);
		String sendUrl = buildRedirectUrl(consts.getConstsValue(), signMap);
		response.sendRedirect(sendUrl);
	}

	private void checkMerchantOrderNumber(ServiceResponse result, String merchantOrderNumber) throws ServiceException {

		MerchantRechargeOrder merchantRechargeOrder = merchantRechargeOrderService
				.getOrderByMerchantOrderNumber(merchantOrderNumber);

		if (null != merchantRechargeOrder) {
			ResponseUtils.exception(result, "订单已提交！", RequestStatus.FAILED.getStatus());
		}

	}

	private String buildRedirectUrl(String url, Map<String, Object> signMap) {
		return url;
	}

	private void generatorOrder(String merchantUUID, Map<String, Object> signMap) throws ServiceException {

		MerchantRechargeOrder order = new MerchantRechargeOrder();
		String currentOrder = DateUtil.getCurrentDate();
		String orderNumber = RECHARGE + currentOrder + RandomUtils.random(6);

		order.setOrderNumber(orderNumber);
		order.setPayBank(getValue(signMap, "bank_code"));
		order.setPayAmount(new BigDecimal(getValue(signMap, "pay_amount")));
		order.setCreateTime(new Date());
		order.setPayChannel(getValue(signMap, "pay_channel"));
		order.setStatus(OrderStatusConsts.SUBMIT);
		order.setType(MerchantOrderTypeConsts.RECHARGE_ORDER);

		order.setMerchantId(merchantUUID);
		order.setMerchantOrderNumber(getValue(signMap, "order_number"));
		signMap.put("we_order_number", orderNumber);

		merchantRechargeOrderService.insertSelective(order);
	}

	private String getValue(Map<String, Object> signMap, String key) {
		return signMap.get(key) == null ? "" : signMap.get(key).toString();
	}

	private void checkMerchantFinance(ServiceResponse result, String merchantUUID) throws ServiceException {
		MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchantUUID);
		if (null == merchantFinance) {
			ResponseUtils.exception(result, "财务信息缺失！", RequestStatus.FAILED.getStatus());
			return;
		}
		if (merchantFinance.getStatus().equals(MerchantFinanceStatusConsts.FREEZE)) {
			ResponseUtils.exception(result, "财务状态已冻结！", RequestStatus.FAILED.getStatus());
		}

	}

	private MerchantApi checkSerurityKey(ServiceResponse result, String merchantUUID, String securityKey)
			throws ServiceException {
		if (StringUtils.isEmpty(securityKey)) {
			ResponseUtils.exception(result, "请输入秘钥！", RequestStatus.FAILED.getStatus());
			return null;
		}

		MerchantApi merchantApi = merchantApiService.getMerchantApiByUUID(merchantUUID);

		if (null == merchantApi) {
			ResponseUtils.exception(result, "賬戶失效，未有財務信息！", RequestStatus.FAILED.getStatus());
			return null;
		}

		if (!securityKey.equals(merchantApi.getSecretKey())) {
			ResponseUtils.exception(result, "秘钥不正确！", RequestStatus.FAILED.getStatus());
		}
		return merchantApi;
	}

	private static boolean checkSign(Map<String, Object> signMap, String sign, List<String> keys, String md5Key)
			throws Exception {

		StringBuffer newSign = new StringBuffer();
		keys.forEach(key -> {
			newSign.append(key).append("=").append(signMap.get(key)).append("&");
		});

		if (StringUtils.isEmpty(newSign.toString())) {
			return false;
		}
		newSign.delete(newSign.length() - 1, newSign.length());

		return MD5Utils.md5(newSign.toString(), md5Key).equals(sign) ? true : false;
	}

	private void checkMerchantId(String merchantUUID, ServiceResponse result) throws ServiceException {

		MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
		merchantInfo.setUuid(merchantUUID);
		MerchantAccountInfo merchantAccountInfo = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

		if (null == merchantAccountInfo) {
			ResponseUtils.exception(result, "该商户未注册！", RequestStatus.FAILED.getStatus());
			return;
		}

		if (MerchantAccountInfoStatusConsts.PRE_AUDIT == merchantAccountInfo.getStatus()) {
			ResponseUtils.exception(result, "该商户未审核！", RequestStatus.FAILED.getStatus());
			return;
		}

		if (MerchantAccountInfoStatusConsts.FAIL == merchantAccountInfo.getStatus()) {
			ResponseUtils.exception(result, "该商户审核失败！", RequestStatus.FAILED.getStatus());
		}
	}

	public String getParameter(HttpServletRequest request, String key) {
		String parameter = request.getParameter(key);
		return StringUtils.isEmpty(parameter) ? "" : parameter;
	}

}
