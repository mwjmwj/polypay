package com.polypay.platform.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

	
}
