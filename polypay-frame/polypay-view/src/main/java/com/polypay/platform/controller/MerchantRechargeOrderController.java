package com.polypay.platform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

@RestController
public class MerchantRechargeOrderController extends BaseController<MerchantRechargeOrderVO>{
	
	private Logger log = LoggerFactory.getLogger(MerchantRechargeOrderController.class);

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;
	
	@RequestMapping("/merchant/recharge/order/list")
	public ServiceResponse listMerchantRechargeOrder() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantRechargeOrderVO> pageList = null;
			MerchantRechargeOrderVO merchantPlaceOrderVO =  new MerchantRechargeOrderVO();
			pageList = merchantRechargeOrderService.listMerchantRechargeOrder(pageBounds, merchantPlaceOrderVO);
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
	
}
