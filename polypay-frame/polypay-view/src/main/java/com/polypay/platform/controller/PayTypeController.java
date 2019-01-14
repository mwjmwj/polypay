package com.polypay.platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.PayType;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IPayTypeService;
import com.polypay.platform.utils.MerchantUtils;

@Controller
public class PayTypeController {
	
	@Autowired
	private IPayTypeService payTypeService;
	
	
	@RequestMapping("paytype/list")
	@ResponseBody
	public ServiceResponse listPayType() throws ServiceException
	{
		ServiceResponse response = new ServiceResponse();
		
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		List<PayType> list = payTypeService.listPayType(merchant.getPayLevel());
		response.setData(list);
		
		return response;
	}
	
}
