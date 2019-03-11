package com.polypay.platform.managercontroller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantPlaceOrderVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;
import com.polypay.platform.vo.MerchantSettleOrderVO;

@Controller
public class ManagerProxyController extends BaseController{

	
	private Logger log = LoggerFactory.getLogger(ManagerMerchantAccountController.class);
	
	@Autowired
	private IMerchantPlaceOrderService merchantPlaceOrderService;
	
	@Autowired
	private IMerchantSettleOrderService merchantSettleOrderService;
	
	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;
	
	
	@RequestMapping("/proxy/merchant/placeorder/list")
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
			

			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			
			if(null == merchant)
			{
				return null;
			}
			merchantPlaceOrderVO.setProxyId(merchant.getUuid());
			

			pageList = merchantPlaceOrderService.listProxyMerchantPlaceOrder(pageBounds, merchantPlaceOrderVO);
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

	@RequestMapping("/proxy/merchant/rechargeorder/list")
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
			
			
			
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			
			if(null == merchant)
			{
				return null;
			}
			merchantRechargeOrderVO.setProxyId(merchant.getUuid());

			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantRechargeOrderVO> pageList = null;
			pageList = merchantRechargeOrderService.listProxyMerchantRechargeOrder(pageBounds, merchantRechargeOrderVO);
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
	
	
	
	@RequestMapping("/proxy/merchant/settleorder/list")
	@ResponseBody
	public ServiceResponse listMerchantSettleOrder() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		
		try {
			
			PageBounds pageBounds = this.getPageBounds();
			
			PageList<MerchantSettleOrderVO> pageList = null;

			MerchantSettleOrderVO merchantSettleOrderVO = new MerchantSettleOrderVO();

			merchantSettleOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));

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
			
			String merchantId = getRequest().getParameter("merchantId");
			if (!StringUtils.isEmpty(merchantId)) {
				merchantSettleOrderVO.setMerchantId(merchantId);;
			}
			

			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			
			if(null == merchant)
			{
				return null;
			}
			merchantSettleOrderVO.setProxyId(merchant.getUuid());
			
			String status = getRequest().getParameter("status");

			if (!StringUtils.isEmpty(status)) {
				
				merchantSettleOrderVO.setStatus(Integer.parseInt(status));
			}

			pageList = merchantSettleOrderService.listProxyMerchantSettleOrder(pageBounds, merchantSettleOrderVO);
			
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
	
	
	
}
