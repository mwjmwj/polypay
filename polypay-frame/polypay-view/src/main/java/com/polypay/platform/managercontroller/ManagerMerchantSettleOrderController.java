package com.polypay.platform.managercontroller;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.*;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.service.*;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.MD5;
import com.polypay.platform.utils.MerchantUtils;
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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class ManagerMerchantSettleOrderController extends BaseController<MerchantSettleOrderVO> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantSettleOrderController.class);

	@Autowired
	private IMerchantSettleOrderService merchantSettleOrderService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	@Autowired
	private ISystemConstsService systemConstsService;

	@Autowired
	private IChannelService channelService;

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;
	
	
	@Autowired
	private IMerchantApiService merchantApiService;

	@Autowired
	private IMerchantChannelAmountService merchantChannelAmountService;

	@RequestMapping("/merchantmanager/settle/order/list")
	@ResponseBody
	public ServiceResponse listMerchantSettleOrder() throws ServiceException {

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
			if (!StringUtils.isEmpty(createTime)) {
				datas = DateUtils.changeDate(createTime);
				merchantSettleOrderVO.setcBeginTime(datas[0]);
				merchantSettleOrderVO.setcEndTime(datas[1]);
			}

			if (!StringUtils.isEmpty(successTime)) {
				datas = DateUtils.changeDate(successTime);
				merchantSettleOrderVO.setsBeginTime(datas[0]);
				merchantSettleOrderVO.setsEndTime(datas[1]);
			}

			String status = getRequest().getParameter("status");

			if (!StringUtils.isEmpty(status)) {
				merchantSettleOrderVO.setStatus(Integer.parseInt(status));
			}

			String merchantId = getRequest().getParameter("merchantId");
			if (!StringUtils.isEmpty(merchantId)) {
				merchantSettleOrderVO.setMerchantId(merchantId);
				;
			}

			pageList = merchantSettleOrderService.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO);
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
	
	@RequestMapping("/merchantmanager/settle/order/list1")
	@ResponseBody
	public ServiceResponse listMerchantSettleOrder1() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantSettleOrderVO> pageList = null;

			MerchantSettleOrderVO merchantSettleOrderVO = new MerchantSettleOrderVO();

			String createTime = getRequest().getParameter("beginTime");

			if (!StringUtils.isEmpty(createTime)) {
				merchantSettleOrderVO.setcTime(createTime.substring(0, 10));
			}


			String merchantId = getRequest().getParameter("merchantId");
			if (!StringUtils.isEmpty(merchantId)) {
				merchantSettleOrderVO.setMerchantId(merchantId);
			}
			
			merchantSettleOrderVO.setProxyId(MerchantUtils.getMerchant().getUuid());


			pageList = merchantSettleOrderService.listMerchantSettleOrder1(pageBounds, merchantSettleOrderVO);
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

	@GetMapping("managermerchant/settle/all")
	@ResponseBody
	public ServiceResponse allMerchantRecharge() throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantMainDateVO merchantGroupDate = merchantSettleOrderService.managerAllMerchantSettleOrder();
		response.setData(merchantGroupDate);

		return response;
	}

	@RequestMapping("merchantmanager/settle/query")
	public String getMerchantAccountInfo(@RequestParam("id") Integer id, Map<String, Object> result)
			throws ServiceException {

		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService.selectByPrimaryKey(id);
		result.put("settleorder", selectByPrimaryKey);

		return "adminmanager/managermerchantsettleaudit";

	}

	@RequestMapping("merchantmanager/settleorder/audit")
	@ResponseBody
	public ServiceResponse settleOrder(MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService
				.selectByPrimaryKey(merchantSettleOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		// 异步执行该订单修改该订单状态
		executorService.execute(() -> submitSettleOrder(selectByPrimaryKey));

		response.setMessage("提交成功");

		return response;
	}

	@RequestMapping("merchantmanager/settleorder/fail")
	@ResponseBody
	public ServiceResponse failSettleOrder(MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService
				.selectByPrimaryKey(merchantSettleOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		selectByPrimaryKey.setDescreption("系统审核不通过");
		selectByPrimaryKey.setHandlePeople(MerchantUtils.getMerchant().getAccountName());
		// 回滚订单
		rollBackSettlerOrder(selectByPrimaryKey);
		
		filaPlacePayNotify(selectByPrimaryKey);

		response.setMessage("提交成功");

		return response;
	}

	@RequestMapping("merchantmanager/settleorder/success")
	@ResponseBody
	public ServiceResponse successSettleOrder(MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService
				.selectByPrimaryKey(merchantSettleOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		selectByPrimaryKey.setHandlePeople(MerchantUtils.getMerchant().getAccountName());
		selectByPrimaryKey.setStatus(OrderStatusConsts.SUCCESS);
		
		merchantSettleOrderService.updateByPrimaryKeySelective(selectByPrimaryKey);
		
		filaPlacePayNotify(selectByPrimaryKey);

		response.setMessage("提交成功");

		return response;
	}

	
	private void submitSettleOrder(MerchantSettleOrder merchantSettleOrder) {

		Object status = null;
		try {

			MerchantSettleOrder selectByPrimaryKey;
			synchronized (merchantSettleOrder.getMerchantId().intern()) {

				selectByPrimaryKey = merchantSettleOrderService.selectByPrimaryKey(merchantSettleOrder.getId());

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
					
					merchantSettleOrder.setDescreption(null==result.get("msg")?"":result.get("msg").toString());
					// 回滚
					rollBackSettlerOrder(merchantSettleOrder);
					
					
					// 通知
					filaPlacePayNotify(merchantSettleOrder);
					return;
				}

				// 未支付状态返回
				if (status.toString().equals("2")) {
					return;
				}

				// 成功修改订单状态
				merchantSettleOrder.setStatus(OrderStatusConsts.HANDLE);

				merchantSettleOrder.setHandlePeople("admin");
				merchantSettleOrder.setPayTime(new Date());
				merchantSettleOrderService.updateByPrimaryKeySelective(merchantSettleOrder);
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
		
		param.put("order_no", merchantSettleOrder.getMerchantOrderNumber());
		keys.add("order_no");
		
		param.put("amount", merchantSettleOrder.getPostalAmount().toString());
		keys.add("amount");
		
		String status = "";
		if(merchantSettleOrder.getStatus().equals(OrderStatusConsts.SUCCESS))
		{
			status = "success";
		}else if (merchantSettleOrder.getStatus().equals(OrderStatusConsts.FAIL))
		{
			status = "fail";
		}
		
		param.put("status", status);
		keys.add("status");  // 1 支付中   0 成功  -1  失败
		
		param.put("time", System.currentTimeMillis()+"");
		keys.add("time");
		
		MerchantApi merchantApiByUUID = merchantApiService.getMerchantApiByUUID(merchantSettleOrder.getMerchantId());
		
		String signParam = getUrl(param, keys);
		signParam += "&api_key="+merchantApiByUUID.getSecretKey();
		String sign = MD5.md5(signParam);
		
		param.put("sign", sign);
		
		HttpClientUtil.httpPost(merchantSettleOrder.getCallUrl(), null, param);
		
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

}
