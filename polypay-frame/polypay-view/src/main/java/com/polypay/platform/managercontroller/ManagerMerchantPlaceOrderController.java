package com.polypay.platform.managercontroller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.Channel;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantApi;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.SystemConstans;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.service.IChannelService;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IMerchantApiService;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.service.ISystemConstsService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.utils.MD5;
import com.polypay.platform.vo.MerchantAccountInfoVO;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

@Controller
public class ManagerMerchantPlaceOrderController extends BaseController<MerchantPlaceOrderVO> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantPlaceOrderController.class);

	@Autowired
	private IMerchantPlaceOrderService merchantPlaceOrderService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	private ExecutorService executorService = Executors.newFixedThreadPool(5);

	@Autowired
	private IChannelService channelService;

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;
	
	
	@Autowired
	private IMerchantApiService merchantApiService;

	@RequestMapping("/merchantmanager/place/order/list")
	@ResponseBody
	public ServiceResponse listMerchantPlaceOrder() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			MerchantPlaceOrderVO merchantPlaceOrderVO = new MerchantPlaceOrderVO();
			PageList<MerchantPlaceOrderVO> pageList = null;

			merchantPlaceOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));

			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");

			Date[] datas;
			if (!StringUtils.isEmpty(createTime)) {
				datas = DateUtils.changeDate(createTime);
				merchantPlaceOrderVO.setcBeginTime(datas[0]);
				merchantPlaceOrderVO.setcEndTime(datas[1]);
			}

			if (!StringUtils.isEmpty(successTime)) {
				datas = DateUtils.changeDate(successTime);
				merchantPlaceOrderVO.setsBeginTime(datas[0]);
				merchantPlaceOrderVO.setsEndTime(datas[1]);
			}

			String status = getRequest().getParameter("status");

			if (!StringUtils.isEmpty(status)) {
				merchantPlaceOrderVO.setStatus(Integer.parseInt(status));
			}
			
			String merchantId = getRequest().getParameter("merchantId");
			if (!StringUtils.isEmpty(merchantId)) {
				merchantPlaceOrderVO.setMerchantId(merchantId);;
			}
			

			pageList = merchantPlaceOrderService.listMerchantPlaceOrder(pageBounds, merchantPlaceOrderVO);
			Page<MerchantPlaceOrderVO> pageData = getPageData(pageList);
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

	@GetMapping("managermerchant/place/all")
	@ResponseBody
	public ServiceResponse allMerchantPlace() throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantMainDateVO merchantGroupDate = merchantPlaceOrderService.managerAllMerchantPlaceOrder();
		response.setData(merchantGroupDate);

		return response;
	}

	@RequestMapping("merchantmanager/place/query")
	public String getMerchantAccountInfo(@RequestParam("id") Integer id, Map<String, Object> result)
			throws ServiceException {

		MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService.selectByPrimaryKey(id);
		result.put("placeorder", selectByPrimaryKey);

		return "adminmanager/managermerchantplaceaudit";

	}

	@RequestMapping("merchantmanager/placeorder/audit")
	@ResponseBody
	public ServiceResponse placeOrder(MerchantPlaceOrderVO merchantplaceOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService
				.selectByPrimaryKey(merchantplaceOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		// 异步执行该订单修改该订单状态
		executorService.execute(() -> submitPlaceOrder(selectByPrimaryKey));

		response.setMessage("提交成功");

		return response;
	}

	@RequestMapping("merchantmanager/placeorder/fail")
	@ResponseBody
	public ServiceResponse failplaceOrder(MerchantPlaceOrderVO merchantplaceOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService
				.selectByPrimaryKey(merchantplaceOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		
		// 回滚订单
		filaPlacePayNotify(selectByPrimaryKey);
		placePayNotify1(selectByPrimaryKey);

		response.setMessage("提交成功");

		return response;
	}

	private void submitPlaceOrder(MerchantPlaceOrder merchantPlaceOrder) {

		try {

			MerchantPlaceOrder selectByPrimaryKey;
			synchronized (merchantPlaceOrder.getMerchantId().intern()) {

				selectByPrimaryKey = merchantPlaceOrderService.selectByPrimaryKey(merchantPlaceOrder.getId());

				if (null == selectByPrimaryKey) {
					return;
				}

				if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
					return;
				}

				MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
				merchantInfo.setUuid(merchantPlaceOrder.getMerchantId());
				MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);
				
				Channel channel = channelService.selectByPrimaryKey(merchantInfoByUUID.getChannelId());
				
				Class<?> payBean = Class.forName(channel.getBean());
				IPayChannel paychannel = (IPayChannel) payBean.newInstance();

				// {"status":1,"msg":"代付申请成功，系统处理中","serial":"代付订单号"}
				// {"status":0,"msg":"代付失败"}
				Map<String, Object> result = paychannel.placeOrder(selectByPrimaryKey);

				Object status = result.get("status");

				// 返回结果失败 回滚订单
				if (null == status || status.toString().equals("0")) {

					// 回滚
					filaPlacePayNotify(merchantPlaceOrder);
					
					placePayNotify1(merchantPlaceOrder);
					return;
				}

				// 成功修改订单状态
				merchantPlaceOrder.setStatus(OrderStatusConsts.HANDLE);
				merchantPlaceOrder.setHandlerTime(new Date());
				merchantPlaceOrderService.updateByPrimaryKeySelective(merchantPlaceOrder);
			}
		} catch (Exception e) {
			// 回滚
			try {
				filaPlacePayNotify(merchantPlaceOrder);
				
				placePayNotify1(merchantPlaceOrder);
			} catch (ServiceException e1) {
				log.error(" settle order fail: " + merchantPlaceOrder.getMerchantId() + " - "
						+ merchantPlaceOrder.getId());

			}

		}
	}
	
	/**
	 *  回调给商户代付信息
	 * @param merchantSettleOrder
	 * @return
	 * @throws ServiceException
	 */
	private String placePayNotify1(MerchantPlaceOrder merchantSettleOrder) throws ServiceException {
		
		Map<String, String> param = Maps.newHashMap();
		List<String> keys = Lists.newArrayList();
		
		param.put("merchant_id", merchantSettleOrder.getMerchantId());
		keys.add("merchant_id");
		
		param.put("order_no", merchantSettleOrder.getOrderNumber());
		keys.add("order_no");
		
		param.put("amount", merchantSettleOrder.getPayAmount().toString());
		keys.add("amount");
		
		param.put("status", "fail");
		keys.add("status");  // 1 支付中   0 成功  -1  失败
		
		param.put("time", System.currentTimeMillis()+"");
		keys.add("time");
		
		
		MerchantApi merchantApiByUUID = merchantApiService.getMerchantApiByUUID(merchantSettleOrder.getMerchantId());
		
		String signParam = getUrl(param, keys);
		signParam += "&api_key="+merchantApiByUUID.getSecretKey();
		String sign = MD5.md5(signParam);
		
		param.put("sign", sign);
		
		HttpRequestDetailVo httpPost = HttpClientUtil.httpPost(merchantSettleOrder.getTradeType(), null, param);
		return httpPost.getResultAsString();
	}
	
	private String getUrl(Map<String, String> signMap, List<String> keys)
	{
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
	private void filaPlacePayNotify(MerchantPlaceOrder merchantPlaceOrder) throws ServiceException {
		synchronized (merchantPlaceOrder.getMerchantId().intern()) {

			MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService
					.selectByPrimaryKey(merchantPlaceOrder.getId());
			if (selectByPrimaryKey.getStatus().equals(OrderStatusConsts.SUBMIT)
					|| selectByPrimaryKey.getStatus().equals(OrderStatusConsts.HANDLE)) {
				// 回滚金额
				MerchantFinance merchantFinance = merchantFinanceService
						.getMerchantFinanceByUUID(merchantPlaceOrder.getMerchantId());
				merchantFinance
						.setBlanceAmount(merchantFinance.getBlanceAmount().add(merchantPlaceOrder.getPayAmount()));
				merchantPlaceOrder.setStatus(OrderStatusConsts.FAIL);
				merchantPlaceOrder.setHandlerTime(new Date());
				merchantPlaceOrder.setDescreption("订单提交操作异常！");
				merchantPlaceOrder.setHandlerName("系统");

				merchantPlaceOrderService.updateByPrimaryKeySelective(merchantPlaceOrder);
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
			}
		}
	}

}
