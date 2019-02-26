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
import com.polypay.platform.consts.MerchantFinanceStatusConsts;
import com.polypay.platform.consts.MerchantOrderTypeConsts;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.SystemConstans;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IMerchantApiService;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantFrezzService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.service.IPayTypeService;
import com.polypay.platform.service.ISystemConstsService;
import com.polypay.platform.utils.DateUtil;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.MD5;
import com.polypay.platform.utils.RandomUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@RestController
public class OpenApiController {

	private static final String RECHARGE = "R";

	private static final Integer DEFAULT_LEVEL = 1;

	private static final String FREZZ_RATE = "FREZZ_RATE";

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
	public ServiceResponse recharge(Map<String, Object> paramMap, HttpServletRequest request,
			HttpServletResponse response) {
		ServiceResponse result = new ServiceResponse();
		// http://localhost:8080/polypay-view/open/api/recharge?

//		merchant_id=141b6ccb8bde4b10b1d0c4a5db91cf52&order_number=11&pay_amount=10.00&time=5522541&pay_channel&notify_url&api_key

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

			String payChannel = getParameter(request, "pay_channel");
			signMap.put("pay_channel", payChannel);
			if (StringUtils.isEmpty(payChannel)) {
				ResponseUtils.exception(result, "支付通道不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}
			keys.add("pay_channel");

			String notify_url = getParameter(request, "notify_url");
			signMap.put("notify_url", notify_url);
			if (StringUtils.isEmpty(notify_url)) {
				ResponseUtils.exception(result, "异步通知地址不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}
			keys.add("notify_url");

			String sign = getParameter(request, "sign");
			if (StringUtils.isEmpty(sign)) {
				ResponseUtils.exception(result, "签名不能为空！", RequestStatus.FAILED.getStatus());
				return result;
			}

			// 校验商户资格
			checkMerchantId(merchantUUID, result);
			if (!RequestStatus.SUCCESS.getStatus().equals(result.getStatus())) {
				return result;
			}

			// 校验商户秘钥
			MerchantApi merchantApi = getMerchantApi(merchantUUID);

			keys.add("api_key");
			signMap.put("api_key", merchantApi.getSecretKey());

			// 校验签名是否正确
			if (!checkSign(signMap, sign, keys)) {
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
			MerchantRechargeOrder generatorOrder = generatorOrder(merchantUUID, signMap);

			// 请求转发到第三方
			sendRediect(response, signMap, generatorOrder);

		} catch (Exception e) {
			result.setMessage(e.getMessage());
			return result;
		}

		return result;
	}
	
	public static void main(String[] args) {
		String url = "http://localhost:8080/polypay-view/open/api/recharge?merchant_id=141b6ccb8bde4b10b1d0c4a5db91cf52&order_number=999999995&pay_amount=10.00&time=5522541&pay_channel=usdt&notify_url=www.baidu.com&api_key=1a280dcd6d354a3b80dffad647100c74&sign=3f80785b52ab226d7c106819c61034cd";
		
		System.out.println(MD5.encryption("merchant_id=141b6ccb8bde4b10b1d0c4a5db91cf52&order_number=999999995&pay_amount=10.00&time=5522541&pay_channel=usdt&notify_url=www.baidu.com&api_key=1a280dcd6d354a3b80dffad647100c74"));
	}

	@RequestMapping("/open/api/recharge/back")
	public String rechargeOrderBack(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		SystemConsts consts = systemConstsService.getConsts(SystemConstans.SMART_RECHARGE_BEAN);

		String constsValue = consts.getConstsValue();

		Class<?> payBean = Class.forName(constsValue);

		IPayChannel paychannel = (IPayChannel) payBean.newInstance();

		/**
		 * result.put("total_fee", total_fee); result.put("merchantno", sdpayno);
		 * result.put("payno", sdorderno); result.put("channel", "WY");
		 * result.put("status", status);
		 */
		Map<String, Object> result = paychannel.checkOrder(request);

		Object payStatus = result.get("status");

		if (null == payStatus || "-10".equals(payStatus)) {
			return "fail";
		}

		String merchantOrderNumber = result.get("orderno").toString();

		MerchantRechargeOrder orderByMerchantOrderNumber = merchantRechargeOrderService
				.getOrderByOrderNumber(merchantOrderNumber);

		if (null == orderByMerchantOrderNumber) {
			return "fail";
		}

		// 除去1 为 订单成功处理失败
		if (!(OrderStatusConsts.SUBMIT + "").equals(payStatus)) {
			orderByMerchantOrderNumber.setStatus(OrderStatusConsts.FAIL);
			merchantRechargeOrderService.updateByPrimaryKeySelective(orderByMerchantOrderNumber);

			// callbackurl
			String callbackurl = orderByMerchantOrderNumber.getDescreption();

			String param = "status=" + payStatus + "&merchantno=" + orderByMerchantOrderNumber.getMerchantOrderNumber()
					+ "&payno=" + orderByMerchantOrderNumber.getOrderNumber() + "&merchantid="
					+ orderByMerchantOrderNumber.getMerchantId();
			String sign = MD5.md5(param);

			HttpClientUtil.httpGet(callbackurl + "?" + param + "&sign=" + sign);

			return "success";
		}

		Map<String, Object> order = paychannel.getOrder(result.get("orderno").toString());

		// {"status":1,"msg":"成功订单","sdorderno":"商户订单号","total_fee":"订单金额","sdpayno":"平台订单号"}
		// {"status":0,"msg":"失败订单"}

		// 失败订单 直接回调商户 并修改订单状态
		if (!"1".equals(order.get("status"))) {
			if (!(OrderStatusConsts.SUBMIT == orderByMerchantOrderNumber.getStatus().intValue())) {
				return "success";
			}
			orderByMerchantOrderNumber.setStatus(OrderStatusConsts.FAIL);
			merchantRechargeOrderService.updateByPrimaryKeySelective(orderByMerchantOrderNumber);

			// callbackurl
			String callbackurl = orderByMerchantOrderNumber.getDescreption();
			String param = "status=" + payStatus + "&merchantno=" + orderByMerchantOrderNumber.getMerchantOrderNumber()
					+ "&payno=" + orderByMerchantOrderNumber.getOrderNumber() + "&merchantid="
					+ orderByMerchantOrderNumber.getMerchantId();
			String sign = MD5.md5(param);
			HttpClientUtil.httpGet(callbackurl + "?" + param + "&sign=" + sign);

			return "success";
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

			// 冻结费率
			SystemConsts frezzRate = systemConstsService.getConsts(FREZZ_RATE);
			String frezzrate = frezzRate.getConstsValue();

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

			payAmount = new BigDecimal(order.get("total_fee").toString());
			orderByMerchantOrderNumber.setPayAmount(payAmount);

			poundage = payAmount.multiply(new BigDecimal(rate)).setScale(4, BigDecimal.ROUND_HALF_UP);

			arrivalAmount = payAmount.subtract(poundage);

			frezzAmount = arrivalAmount.multiply(new BigDecimal(frezzrate)).setScale(4, BigDecimal.ROUND_HALF_UP);

			Integer status = orderByMerchantOrderNumber.getStatus();
			if (OrderStatusConsts.SUBMIT == status) {
				// 统一入库
				endInsertOrder(orderByMerchantOrderNumber, resourceAmount, resourceFrezzAmount, poundage, arrivalAmount,
						frezzAmount, merchantFinance);
			}

			// callbackurl
			String callbackurl = orderByMerchantOrderNumber.getDescreption();

			String param = "status=" + payStatus + "&merchantno=" + orderByMerchantOrderNumber.getMerchantOrderNumber()
					+ "&payno=" + orderByMerchantOrderNumber.getOrderNumber() + "&merchantid="
					+ orderByMerchantOrderNumber.getMerchantId();
			String sign = MD5.md5(param);
			HttpClientUtil.httpGet(callbackurl + "?" + param + "&sign=" + sign);
		}

		return "success";
	}

	@RequestMapping("/getway/order/query")
	public Map<String, Object> getOrder(HttpServletRequest request) throws ServiceException {

		Map<String, Object> result = Maps.newHashMap();

		String merchantid = getParameter(request, "merchantid");

		String orderNumber = getParameter(request, "payno");

		String time = getParameter(request, "time");

		String apikey = getParameter(request, "apikey");

		String sign = getParameter(request, "sign");

		StringBuffer signParam = new StringBuffer();

		signParam.append("merchantid=" + merchantid).append("&payno=" + orderNumber).append("&time=" + time)
				.append("&" + apikey);

		if (!sign.equals(MD5.md5(signParam.toString()))) {
			result.put("status", -1);
			result.put("msg", "签名错误");
			return result;
		}

		MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
		merchantInfo.setUuid(merchantid);
		MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

		if (null == merchantInfoByUUID) {
			result.put("status", -1);
			result.put("msg", "商户不存在");
			return result;
		}

		MerchantRechargeOrder orderByMerchantOrderNumber = merchantRechargeOrderService
				.getOrderByMerchantOrderNumber(orderNumber);
		if (null == orderByMerchantOrderNumber) {
			result.put("status", -1);
			result.put("msg", "订单不存在");
			return result;
		}

		if (!merchantid.equals(orderByMerchantOrderNumber.getMerchantId())) {
			result.put("status", -1);
			result.put("msg", "订单不存在");
			return result;
		}

		MerchantApi merchantApiByUUID = merchantApiService.getMerchantApiByUUID(merchantid);

		if (!merchantApiByUUID.getSecretKey().equals(apikey)) {
			result.put("status", -1);
			result.put("msg", "apikey不正确");
			return result;
		}
		result.put("status", 1);
		result.put("msg", "成功订单");
		result.put("merchantno", orderByMerchantOrderNumber.getMerchantOrderNumber());
		result.put("payno", orderByMerchantOrderNumber.getOrderNumber());
		result.put("amount", orderByMerchantOrderNumber.getPayAmount());

		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	private void endInsertOrder(MerchantRechargeOrder orderByMerchantOrderNumber, BigDecimal resourceAmount,
			BigDecimal resourceFrezzAmount, BigDecimal poundage, BigDecimal arrivalAmount, BigDecimal frezzAmount,
			MerchantFinance merchantFinance) throws ServiceException {

		try {
			// 修改订单入库
			saveRechargeOrder(orderByMerchantOrderNumber, poundage, arrivalAmount);

			MerchantFrezzon merchantFrezzon = new MerchantFrezzon();
			merchantFrezzon.setAmount(frezzAmount);
			merchantFrezzon.setFrezzTime(new Date());
			merchantFrezzon.setArrivalTime(DateUtils.getAfterDayDate("1"));
			merchantFrezzon.setStatus(MerchantFinanceStatusConsts.FREEZE);

			merchantFrezzService.insertSelective(merchantFrezzon);
			merchantFinance.setBlanceAmount(resourceAmount.add(arrivalAmount.subtract(frezzAmount)));
			merchantFinance.setFronzeAmount(resourceFrezzAmount.add(frezzAmount));
			merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
		} catch (Exception e) {
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

		PayType payType = payTypeService.getPayTypeChannel(orderByMerchantOrderNumber.getMerchantId(), "WY");
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

	private void sendRediect(HttpServletResponse response, Map<String, Object> signMap,
			MerchantRechargeOrder generatorOrder) throws ServiceException, IOException {

		SystemConsts consts = systemConstsService.getConsts(SystemConstans.SMART_RECHARGE_BEAN);

		// 根据配置的四方平台
		sendRedirectUrl(response, consts.getConstsValue(), signMap, generatorOrder);
	}

	private void sendRedirectUrl(HttpServletResponse response, String bean, Map<String, Object> signMap,
			MerchantRechargeOrder generatorOrder) {

		try {

			Class<?> forName = Class.forName(bean);

			IPayChannel payChannel = (IPayChannel) forName.newInstance();

			// 通道关闭
			if (null == payChannel) {
				generatorOrder.setStatus(OrderStatusConsts.FAIL);
				merchantRechargeOrderService.updateByPrimaryKeySelective(generatorOrder);
				return;
			}

			payChannel.sendRedirect(signMap, response);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	private void checkMerchantOrderNumber(ServiceResponse result, String merchantOrderNumber) throws ServiceException {

		MerchantRechargeOrder merchantRechargeOrder = merchantRechargeOrderService
				.getOrderByMerchantOrderNumber(merchantOrderNumber);

		if (null != merchantRechargeOrder) {
			ResponseUtils.exception(result, "订单已提交！", RequestStatus.FAILED.getStatus());
		}

	}

	private MerchantRechargeOrder generatorOrder(String merchantUUID, Map<String, Object> signMap)
			throws ServiceException {

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

		order.setDescreption(getValue(signMap, "notify_url"));

		order.setMerchantId(merchantUUID);
		order.setMerchantOrderNumber(getValue(signMap, "order_number"));
		signMap.put("we_order_number", orderNumber);

		merchantRechargeOrderService.insertSelective(order);
		return order;
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

	private MerchantApi getMerchantApi(String merchantUUID) throws ServiceException {

		MerchantApi merchantApi = merchantApiService.getMerchantApiByUUID(merchantUUID);

		return merchantApi;
	}

	private static boolean checkSign(Map<String, Object> signMap, String sign, List<String> keys) throws Exception {

		StringBuffer newSign = new StringBuffer();
		keys.forEach(key -> {
			newSign.append(key).append("=").append(signMap.get(key)).append("&");
		});

		if (StringUtils.isEmpty(newSign.toString())) {
			return false;
		}
		newSign.delete(newSign.length() - 1, newSign.length());

		return MD5.md5(newSign.toString()).equals(sign) ? true : false;
	}

	private void checkMerchantId(String merchantUUID, ServiceResponse result) throws ServiceException {

		MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
		merchantInfo.setUuid(merchantUUID);
		MerchantAccountInfo merchantAccountInfo = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

		if (null == merchantAccountInfo) {
			ResponseUtils.exception(result, "该商户未注册！", RequestStatus.FAILED.getStatus());
			return;
		}

		/*
		 * if (MerchantAccountInfoStatusConsts.PRE_AUDIT ==
		 * merchantAccountInfo.getStatus()) { ResponseUtils.exception(result, "该商户未审核！",
		 * RequestStatus.FAILED.getStatus()); return; }
		 * 
		 * if (MerchantAccountInfoStatusConsts.FAIL == merchantAccountInfo.getStatus())
		 * { ResponseUtils.exception(result, "该商户审核失败！",
		 * RequestStatus.FAILED.getStatus()); }
		 */
	}

	public String getParameter(HttpServletRequest request, String key) {
		String parameter = request.getParameter(key);
		return StringUtils.isEmpty(parameter) ? "" : parameter;
	}

}
