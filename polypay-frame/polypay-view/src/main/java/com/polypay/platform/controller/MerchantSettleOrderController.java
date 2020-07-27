package com.polypay.platform.controller;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.*;
import com.polypay.platform.consts.*;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.service.*;
import com.polypay.platform.utils.*;
import com.polypay.platform.vo.MerchantAccountBindbankVO;
import com.polypay.platform.vo.MerchantAccountInfoVO;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantSettleOrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class MerchantSettleOrderController extends BaseController<MerchantSettleOrderVO> {

	private Logger log = LoggerFactory.getLogger(MerchantSettleOrderController.class);

	@Autowired
	private IMerchantSettleOrderService merchantSettleOrderService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	@Autowired
	private IMerchantAccountBindbankService merchantAccountBindbankService;
	
	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;

	@Autowired
	private IMerchantChannelAmountService merchantChannelAmountService;

	@Autowired
	private IChannelService channelService;

	@Autowired
	private IMerchantApiService merchantApiService;

	private ExecutorService executorService = Executors.newFixedThreadPool(5);


	@RequestMapping("/merchant/settle/order/list")
	@ResponseBody
	public ServiceResponse listMerchantSettleOrder()
			throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantSettleOrderVO> pageList = null;
			
			MerchantSettleOrderVO merchantSettleOrderVO = new MerchantSettleOrderVO();
			
			
			merchantSettleOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));
			
			
			merchantSettleOrderVO.setMerchantOrderNumber(getRequest().getParameter("morderNumber"));

			
			
			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");
			
			Date[] datas;
			if(!StringUtils.isEmpty(createTime))
			{
				datas = DateUtils.changeDate(createTime);
				merchantSettleOrderVO.setcBeginTime(datas[0]);
				merchantSettleOrderVO.setcEndTime(datas[1]);
			}
			
			if(!StringUtils.isEmpty(successTime))
			{
				datas = DateUtils.changeDate(successTime);
				merchantSettleOrderVO.setsBeginTime(datas[0]);
				merchantSettleOrderVO.setsEndTime(datas[1]);
			}
			
				String status = getRequest().getParameter("status");
			
			if(!StringUtils.isEmpty(status))
			{
				merchantSettleOrderVO.setStatus(Integer.parseInt(status));
			}
			
			pageList = merchantSettleOrderService.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO );
			Page<MerchantSettleOrderVO> pageData = getPageData(pageList);
			response = ResponseUtils.buildResult(pageData);
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		} catch (Exception e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		}
		return response;

	}

	/**
	 * @param merchantSettleOrderVO
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping("/merchant/settle/order")
	@ResponseBody
	public  ServiceResponse settleOrderGenerator(MerchantSettleOrderVO merchantSettleOrderVO)
			throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		MerchantSettleOrder merchantSettleOrder = null;
		try {

			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			if (null == merchant) {
				ResponseUtils.exception(response, "未登录", RequestStatus.FAILED.getStatus());
				return response;
			}

			// 行级锁
			synchronized (merchant.getUuid().intern()) {

				MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchant.getUuid());

				if (null == merchantFinance) {
					ResponseUtils.exception(response, "未有財務信息", RequestStatus.FAILED.getStatus());
					return response;
				}
				
				if (!merchantFinance.getStatus().equals(MerchantFinanceStatusConsts.USABLE)) {
					ResponseUtils.exception(response, "网络异常", RequestStatus.FAILED.getStatus());
					return response;
				}
				
				MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
				merchantInfo.setUuid(merchant.getUuid());
				MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);
			
				if(!merchantInfoByUUID.getStatus().equals(MerchantAccountInfoStatusConsts.SUCCESS))
				{
					ResponseUtils.exception(response, "网络异常", RequestStatus.FAILED.getStatus());
					return response;
				}

				// 结算必须有结算金额
				BigDecimal settleAmount = merchantSettleOrderVO.getSettleAmount();
				if (null == settleAmount) {
					ResponseUtils.exception(response, "请输入结算金额", RequestStatus.FAILED.getStatus());
					return response;
				}
				
				if(settleAmount.compareTo(new BigDecimal(10))<0)
				{
					ResponseUtils.exception(response, "最低金额10", RequestStatus.FAILED.getStatus());
					return response;
				}
				
				// 结算必须有支付密码
				String payPassword = merchantSettleOrderVO.getPayPassword();
				if (null == payPassword) {
					ResponseUtils.exception(response, "请输入支付密码", RequestStatus.FAILED.getStatus());
					return response;
				}

				String reallyPayPassword = merchantFinance.getPayPassword();

				if (!payPassword.equals(reallyPayPassword)) {
					ResponseUtils.exception(response, "支付密码错误", RequestStatus.FAILED.getStatus());
					return response;
				}

				BigDecimal blanceAmount = merchantFinance.getBlanceAmount();
				
				Integer channelId = merchant.getChannelId();
				BigDecimal serviceAmount = null;
				
				serviceAmount = merchant.getHandAmount();
				merchantSettleOrderVO.setServiceAmount(serviceAmount);
				// 双乾支付
				if(channelId == 1)
				{
					if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
						ResponseUtils.exception(response, "余额不足", RequestStatus.FAILED.getStatus());
						return response;
					}
					settleAmount = settleAmount.add(serviceAmount);
					merchantSettleOrderVO.setSettleAmount(settleAmount);
				}
				else if(channelId == 2)
				{
					if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
						ResponseUtils.exception(response, "余额不足", RequestStatus.FAILED.getStatus());
						return response;
					}
					settleAmount = settleAmount.add(serviceAmount);
					merchantSettleOrderVO.setSettleAmount(settleAmount);
				}
				else if(channelId == 3)
				{
					if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
						ResponseUtils.exception(response, "余额不足", RequestStatus.FAILED.getStatus());
						return response;
					}
					settleAmount = settleAmount.add(serviceAmount);
					merchantSettleOrderVO.setSettleAmount(settleAmount);
				}
				else if(channelId == 4)
				{
					if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
						ResponseUtils.exception(response, "余额不足", RequestStatus.FAILED.getStatus());
						return response;
					}
					settleAmount = settleAmount.add(serviceAmount);
					merchantSettleOrderVO.setSettleAmount(settleAmount);
				}
				else
				{
					if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
						ResponseUtils.exception(response, "余额不足", RequestStatus.FAILED.getStatus());
						return response;
					}
					settleAmount = settleAmount.add(serviceAmount);
					merchantSettleOrderVO.setSettleAmount(settleAmount);
				}
				

				Integer merchantBindBankId = merchantSettleOrderVO.getMerchantBindBankId();
				if (null == merchantBindBankId) {
					ResponseUtils.exception(response, "请选择提现银行卡", RequestStatus.FAILED.getStatus());
					return response;
				}

				MerchantAccountBindbank merchantAccountBindbank = merchantAccountBindbankService
						.selectMerchantBindBankByID(merchantBindBankId);
				if (null == merchantAccountBindbank) {
					ResponseUtils.exception(response, "银行卡信息缺失", RequestStatus.FAILED.getStatus());
					return response;
				}
				merchantSettleOrderVO.setMerchantId(merchant.getUuid());
				
				merchantSettleOrder = generatorSettleOrder(merchantSettleOrderVO,merchantAccountBindbank);
				// 扣除余额
				merchantFinance.setBlanceAmount(blanceAmount.subtract(settleAmount));
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance,settleAmount,new BigDecimal(0),"del");


				if("0".equals(merchantInfoByUUID.getHelppayStatus().toString())){
					final MerchantSettleOrder asyncMerchantSettleOrder = merchantSettleOrder;
					// 异步发起请求第三方提现
					executorService.submit(() -> submitSettleOrder(asyncMerchantSettleOrder));
				}
			}



			response.setMessage("订单提交成功");
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());

			if (null != merchantSettleOrder) {
				merchantSettleOrder.setStatus(OrderStatusConsts.FAIL);
				merchantSettleOrderService.updateByPrimaryKeySelective(merchantSettleOrder);
			}

			throw new ServiceException("提现订单生成失败" + e.getMessage(), RequestStatus.FAILED.getStatus());
		}

		return response;

	}

	private void submitSettleOrder(MerchantSettleOrder merchantSettleOrder) {

		Object status = null;
		try {

			MerchantSettleOrder selectByPrimaryKey;
			synchronized (merchantSettleOrder.getMerchantId().intern()) {

				selectByPrimaryKey = merchantSettleOrderService.getSettleOrderByOrderNo(merchantSettleOrder.getOrderNumber());

				if (null == selectByPrimaryKey) {
					return;
				}

				if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
					return;
				}

				MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
				merchantInfo.setUuid(merchantSettleOrder.getMerchantId());
				MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

				Channel channel = channelService.selectByPrimaryKey(merchantInfoByUUID.getChannelId());

				Class<?> payBean = Class.forName(channel.getBean());
				IPayChannel paychannel = (IPayChannel) payBean.newInstance();

				// {"status":1,"msg":"代付申请成功，系统处理中","serial":"代付订单号"}
				// {"status":0,"msg":"代付失败"}

				List<MerchantChannelAmount> merchantChannelAmounts = merchantChannelAmountService.listAll();

				if(!CollectionUtils.isEmpty(merchantChannelAmounts)){
					for (MerchantChannelAmount merchantChannelAmount : merchantChannelAmounts) {
						if("1".equals(merchantChannelAmount.getDfFlag().toString())
								&& merchantSettleOrder.getMerchantId().equals(merchantChannelAmount.getChannelId().toString())){
							selectByPrimaryKey.setMm(merchantChannelAmount);
							break;
						}
					}
				}

				Map<String, Object> result = paychannel.settleOrder(selectByPrimaryKey);

				status = result.get("status");

				// 返回结果失败 回滚订单
				if (status.toString().equals("0")) {

					selectByPrimaryKey.setDescreption(null == result.get("msg") ? "" : result.get("msg").toString());
					// 回滚
					rollBackSettlerOrder(selectByPrimaryKey);
					filaPlacePayNotify(selectByPrimaryKey);
					// 通知
					return;
				}

				// 未支付状态返回
				if (status.toString().equals("2")) {
					return;
				}

				// 成功修改订单状态
				selectByPrimaryKey.setStatus(OrderStatusConsts.HANDLE);

				selectByPrimaryKey.setHandlePeople("admin");
				selectByPrimaryKey.setPayTime(new Date());
				merchantSettleOrderService.updateByPrimaryKeySelective(selectByPrimaryKey);
			}

		} catch (Exception e) {
			// 回滚
			try {
				if (null != status && "1".equals(status.toString())) {
					return;
				}
				rollBackSettlerOrder(merchantSettleOrder);
				filaPlacePayNotify(merchantSettleOrder);
			} catch (ServiceException e1) {
				log.error(" settle order fail: " + merchantSettleOrder.getMerchantId() + " - "
						+ merchantSettleOrder.getId());

			}

		}
	}


	private void filaPlacePayNotify(MerchantSettleOrder merchantSettleOrder) throws ServiceException {


		Map<String, String> param = Maps.newHashMap();
		List<String> keys = Lists.newArrayList();

		param.put("merchant_id", merchantSettleOrder.getMerchantId());
		keys.add("merchant_id");

		param.put("order_no", merchantSettleOrder.getOrderNumber());
		keys.add("order_no");

		param.put("m_order_no", merchantSettleOrder.getMerchantOrderNumber());
		keys.add("m_order_no");

		param.put("amount", merchantSettleOrder.getPostalAmount().toString());
		keys.add("amount");

		String status = "";
		if (merchantSettleOrder.getStatus().equals(OrderStatusConsts.SUCCESS)) {
			status = "success";
		} else if (merchantSettleOrder.getStatus().equals(OrderStatusConsts.FAIL)) {
			status = "fail";
		}

		param.put("status", status);
		keys.add("status");  // 1 支付中   0 成功  -1  失败

		param.put("time", System.currentTimeMillis() + "");
		keys.add("time");

		MerchantApi merchantApiByUUID = merchantApiService.getMerchantApiByUUID(merchantSettleOrder.getMerchantId());

		String signParam = getUrl(param, keys);
		signParam += "&api_key=" + merchantApiByUUID.getSecretKey();
		String sign = MD5.md5(signParam);

		param.put("sign", sign);

		HttpClientUtil.httpPost(merchantSettleOrder.getCallUrl(), null, param);

	}

	private String getUrl(Map<String, String> signMap, List<String> keys) {
		StringBuffer newSign = new StringBuffer();
		keys.forEach(key -> {
			newSign.append(key).append("=").append(signMap.get(key)).append("&");
		});

		if (StringUtils.isEmpty(newSign.toString())) {
			return null;
		}
		newSign.delete(newSign.length() - 1, newSign.length());
		return newSign.toString();
	}


	// 同步回滚订单
	private void rollBackSettlerOrder(MerchantSettleOrder merchantSettleOrder) throws ServiceException {
		synchronized (merchantSettleOrder.getMerchantId().intern()) {

			MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService
					.selectByPrimaryKey(merchantSettleOrder.getId());
			if (selectByPrimaryKey.getStatus().equals(OrderStatusConsts.SUBMIT)
					|| selectByPrimaryKey.getStatus().equals(OrderStatusConsts.HANDLE)) {
				// 回滚金额
				MerchantFinance merchantFinance = merchantFinanceService
						.getMerchantFinanceByUUID(merchantSettleOrder.getMerchantId());
				merchantFinance
						.setBlanceAmount(merchantFinance.getBlanceAmount().add(merchantSettleOrder.getPostalAmount()));
				merchantSettleOrder.setStatus(OrderStatusConsts.FAIL);
				merchantSettleOrderService.updateByPrimaryKeySelective(merchantSettleOrder);
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
			}
		}
	}


	@RequestMapping("merchant/settle/order/pre")
	public String preSettleOrder(Map<String,Object>result) throws ServiceException
	{
		PageBounds pageBounds = this.getPageBounds();
		MerchantAccountBindbankVO merchantAccountBindbankVO = new MerchantAccountBindbankVO();
		PageList<MerchantAccountBindbankVO> listMerchantBindBank = merchantAccountBindbankService.listMerchantBindBank(pageBounds, merchantAccountBindbankVO);
		
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantFinance merchantFinanceByUUID = merchantFinanceService.getMerchantFinanceByUUID(merchant.getUuid());
		
		result.put("merchantfinance", merchantFinanceByUUID);
		result.put("bank", listMerchantBindBank);
		
		return "admin/merchantsettleapply";
	}
	
	
	@GetMapping("merchant/settle/query")
	public String queryMerchantSettleOrder(@RequestParam(name="id") Integer id,Map<String,Object> result) throws ServiceException
	{
		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService.selectByPrimaryKey(id);
		result.put("settleorder", selectByPrimaryKey);
		return "admin/merchantsettleedit";
	}
	
	
	
	@GetMapping("merchant/settle/all")
	@ResponseBody
	public ServiceResponse allMerchantRecharge() throws ServiceException
	{
		ServiceResponse response = new ServiceResponse();
		
		MerchantMainDateVO merchantGroupDate = merchantSettleOrderService.allMerchantSettle(MerchantUtils.getMerchant().getUuid());
		response.setData(merchantGroupDate);
		
		return response;
	}
	
	
	

	private MerchantSettleOrder generatorSettleOrder(MerchantSettleOrderVO merchantSettleOrderVO, MerchantAccountBindbank merchantAccountBindbank)
			throws ServiceException {

		MerchantSettleOrder merchantSettleOrder = new MerchantSettleOrder();
		String currentOrder = DateUtil.getCurrentDate();
		String orderNumber = "S" + currentOrder + RandomUtils.random(2);
		merchantSettleOrder.setOrderNumber(orderNumber);

		merchantSettleOrder.setMerchantBindBank(merchantAccountBindbank.getAccountNumber());
		merchantSettleOrder.setBankCode(merchantAccountBindbank.getBankCode());
		merchantSettleOrder.setBankName(merchantAccountBindbank.getBankName());
		merchantSettleOrder.setBranchBankName(merchantAccountBindbank.getBranchName());
		
		merchantSettleOrder.setAccountName(merchantAccountBindbank.getAccountName());
		merchantSettleOrder.setAccountProvice(merchantAccountBindbank.getProvice());
		merchantSettleOrder.setAccountCity(merchantAccountBindbank.getCity());
		merchantSettleOrder.setBankNo(merchantAccountBindbank.getRemark());
		
		merchantSettleOrder.setMerchantId(merchantSettleOrderVO.getMerchantId());
		merchantSettleOrder.setStatus(OrderStatusConsts.SUBMIT);
		merchantSettleOrder.setCreateTime(new Date());
		merchantSettleOrder.setPostalAmount(merchantSettleOrderVO.getSettleAmount());
		
		merchantSettleOrder.setServiceAmount(merchantSettleOrderVO.getServiceAmount());
		
		merchantSettleOrder.setType(MerchantOrderTypeConsts.SETTLE_ORDER);
		merchantSettleOrderService.insertSelective(merchantSettleOrder);

		return merchantSettleOrder;
	}

	
}
