package com.polypay.platform.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.utils.MerchantUtils;

@Controller
public class MerchantFinanceController {

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	@RequestMapping("merchantfinance/amountoutin/list")
	@ResponseBody
	public ServiceResponse listMerchantFinance() throws ServiceException {
		ServiceResponse response = new ServiceResponse();
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		List<Map<String, Object>> listMerchantFinance = merchantFinanceService.listMerchantFinance(merchant.getUuid());
		response.setData(listMerchantFinance);
		return response;

	}

	@RequestMapping("merchantfinance/amount")
	public String getMerchantFinance(Map<String, Object> result) throws ServiceException {
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantFinance merchantFinanceByUUID = merchantFinanceService.getMerchantFinanceByUUID(merchant.getUuid());
		result.put("merchantFinance", merchantFinanceByUUID);
		return "admin/merchantfinance";

	}

}
