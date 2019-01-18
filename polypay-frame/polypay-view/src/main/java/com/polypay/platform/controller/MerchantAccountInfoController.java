package com.polypay.platform.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@Controller
public class MerchantAccountInfoController {

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;

	@RequestMapping("merchant/accountinfo")
	public String getMerchantAccountInfo(Map<String, Object> result) throws ServiceException {
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantAccountInfoVO param = new MerchantAccountInfoVO();
		param.setUuid(merchant.getUuid());
		MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(param);
		result.put("merchantAccount", merchantInfoByUUID);
		return "admin/merchantaccountinfo";
	}

	
	public static void main(String[] args) {
		
		List<Object> list = Lists.newArrayList();
		
		
		//方法1
		list.forEach(b->{
			System.out.println();
			System.out.println();
		});
		
		
		//方法2
		list.forEach(b->method(b));
		
		
	}
	
	public static void method(Object b)
	{
		System.out.println();
	}
	
	
}
