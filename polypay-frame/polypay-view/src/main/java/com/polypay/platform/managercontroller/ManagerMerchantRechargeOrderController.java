package com.polypay.platform.managercontroller;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantApi;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.bean.PaymentCode;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.dao.PaymentCodeMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IMerchantApiService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.*;
import com.polypay.platform.vo.MerchantAllRechargeVO;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
public class ManagerMerchantRechargeOrderController extends BaseController {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantRechargeOrderController.class);

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;
	
	@Autowired
	private PaymentCodeMapper paymentCodeMapper;
	
	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;
	
	@Autowired
	private IMerchantApiService merchantApiService;
	

	@RequestMapping("/merchantmanager/recharge/order/list")
	@ResponseBody
	public ServiceResponse listMerchantRechargeOrder(HttpServletRequest request) throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {

			MerchantRechargeOrderVO merchantRechargeOrderVO = new MerchantRechargeOrderVO();
			String status = request.getParameter("status");

			if (!StringUtils.isEmpty(status)) {
				merchantRechargeOrderVO.setStatus(Integer.parseInt(status));
			}

			merchantRechargeOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));

			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");

			Date[] datas;
			if (!StringUtils.isEmpty(createTime)) {
				datas = DateUtils.changeDate(createTime);
				merchantRechargeOrderVO.setcBeginTime(datas[0]);
				merchantRechargeOrderVO.setcEndTime(datas[1]);
			}

			if (!StringUtils.isEmpty(successTime)) {
				datas = DateUtils.changeDate(successTime);
				merchantRechargeOrderVO.setsBeginTime(datas[0]);
				merchantRechargeOrderVO.setsEndTime(datas[1]);
			}
			
			String merchantId = getRequest().getParameter("merchantId");
			if (!StringUtils.isEmpty(merchantId)) {
				merchantRechargeOrderVO.setMerchantId(merchantId);;
			}
			
			String merchantOrder = getRequest().getParameter("merchantOrder");
			if (!StringUtils.isEmpty(merchantOrder)) {
				merchantRechargeOrderVO.setMerchantOrderNumber(merchantOrder);
			}
			
			

			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantRechargeOrderVO> pageList = null;
			pageList = merchantRechargeOrderService.listMerchantRechargeOrder(pageBounds, merchantRechargeOrderVO);
			Page<MerchantRechargeOrderVO> pageData = getPageData(pageList);
			
//			List<String> payIds = Lists.newArrayList();
//			pageData.getRows().forEach(i->{
//				payIds.add(i.getBankOrderNumber());
//			});
//			
//			List<PaymentCode> ps = paymentCodeMapper.listByIds(payIds);
//			Map<String,PaymentCode> pays = Maps.newHashMap();
//			ps.forEach(i->{
//				pays.put(i.getId().toString(), i);
//			});
//			
//			pageData.getRows().forEach(i->{
//			PaymentCode paymentcode = pays.get(i.getBankOrderNumber()) ;
//				if(paymentcode != null)
//				{
//					i.setPayCode(paymentcode.getImgurl());
//				}
//				
//			});
//			
			
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
	
	@RequestMapping("/merchantmanager/recharge/order/list1")
	@ResponseBody
	public ServiceResponse listMerchantRechargeOrder1(HttpServletRequest request) throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {

			MerchantRechargeOrderVO merchantRechargeOrderVO = new MerchantRechargeOrderVO();

			String createTime = getRequest().getParameter("beginTime");

			if (!StringUtils.isEmpty(createTime)) {
				merchantRechargeOrderVO.setcTime(createTime.substring(0, 10));
			}
			
			
			String merchantId = getRequest().getParameter("merchantId");
			if (!StringUtils.isEmpty(merchantId)) {
				merchantRechargeOrderVO.setMerchantId(merchantId);
			}
			
			merchantRechargeOrderVO.setProxyId(MerchantUtils.getMerchant().getUuid());

			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantAllRechargeVO> pageList = null;
			pageList = merchantRechargeOrderService.listMerchantrechargeall(pageBounds, merchantRechargeOrderVO);
			Page<MerchantRechargeOrderVO> pageData = getPageData(pageList);
			
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

	@GetMapping("managermerchant/recharge/all")
	@ResponseBody
	public ServiceResponse allMerchantRecharge() throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantMainDateVO merchantGroupDate = merchantRechargeOrderService.managerAllMerchantRechargeOrder();
		response.setData(merchantGroupDate);

		return response;
	}
	
	@GetMapping("managermerchant/recharge/query")
	public String queryMerchantRechargeOrder(@RequestParam(name="id") Integer id,Map<String,Object> result) throws ServiceException
	{
		MerchantRechargeOrder selectByPrimaryKey = merchantRechargeOrderService.selectByPrimaryKey(id);
		result.put("rechargeorder", selectByPrimaryKey);
		
		int id1;
		PaymentCode paymentCode;
		try {
			id1 = Integer.parseInt(selectByPrimaryKey.getBankOrderNumber());
			
			paymentCode = paymentCodeMapper.selectByPrimaryKey(id1);
			
			result.put("paymentCode", paymentCode);
		}catch(Exception e)
		{
			
		}
		
		return "adminmanager/merchantrechargeedit";
	}
	

	

	@RequestMapping("send/notify/callback")
	@ResponseBody
	public String sendNotify(@RequestParam("orderId")String orderId) throws ServiceException {
		MerchantRechargeOrder selectByPrimaryKey = merchantRechargeOrderService.selectByPrimaryKey(Integer.parseInt(orderId));

		String response = "error";
		if(OrderStatusConsts.SUCCESS == selectByPrimaryKey.getStatus())
		{
			response = sendNotifyMerchant(selectByPrimaryKey);
		}
		
		return response;
	}
	
	
	private String sendNotifyMerchant(MerchantRechargeOrder orderByMerchantOrderNumber) throws ServiceException {
		String callbackurl = orderByMerchantOrderNumber.getDescreption();
		Long time = System.currentTimeMillis();
		String param = "status=" + orderByMerchantOrderNumber.getStatus() + "&merchantno="
				+ orderByMerchantOrderNumber.getMerchantOrderNumber() + "&payno="
				+ orderByMerchantOrderNumber.getOrderNumber() + "&merchantid="
				+ orderByMerchantOrderNumber.getMerchantId()+
				"&time="+time;
		String param1 = "status=" + orderByMerchantOrderNumber.getStatus() + "&merchantno="
				+ orderByMerchantOrderNumber.getMerchantOrderNumber() + "&payno="
				+ orderByMerchantOrderNumber.getOrderNumber() + "&merchantid="
				+ orderByMerchantOrderNumber.getMerchantId()+
				"&time="+time;
		MerchantApi mapi = merchantApiService.getMerchantApiByUUID(orderByMerchantOrderNumber.getMerchantId());

		param += "&api_key=" + mapi.getSecretKey();
		String sign = MD5.md5(param);

		log.info(callbackurl+"?"+param+"&sign="+sign);
		HttpRequestDetailVo httpGet = HttpClientUtil.httpGet(callbackurl + "?" + param1 + "&sign=" + sign);

		// success - 成功 -- fail - 失败
		String resultAsString = httpGet.getResultAsString();
		return resultAsString;
	}
	
	
	
	
	

}
