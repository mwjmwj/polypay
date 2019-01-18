package com.polypay.platform.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantBillService;
import com.polypay.platform.utils.MerchantUtils;

@Controller
public class MerchantBillController extends BaseController<MerchantBill>{
	
	private final static Logger log = LoggerFactory.getLogger(MerchantBillController.class);
	
	@Autowired
	private IMerchantBillService merchantBillService;

	@RequestMapping("/merchant/bill/list")
	@ResponseBody
	public ServiceResponse listMerchantBindBank()
			throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantBill> pageList = null;
			MerchantBill param = new MerchantBill();
			param.setMerchantId(MerchantUtils.getMerchant().getUuid());
			pageList = merchantBillService.listMerchantBill(pageBounds, param);
			Page<MerchantBill> pageData = getPageData(pageList);
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
	
	
	@GetMapping("/merchant/bill/get")
	public String getMerchantBill(@RequestParam("billid") Integer billid,Map<String,Object> result) throws ServiceException
	{
		
		MerchantBill selectByPrimaryKey = merchantBillService.selectByPrimaryKey(billid);
		result.put("merchantbill", selectByPrimaryKey);
		
		return "admin/merchantbilledit";
	}
	
	
}
