package com.polypay.platform.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantFrezzService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

@Controller
public class MainMenuController {

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;

	@Autowired
	private IMerchantFrezzService merchantFrezzService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;
	
	
	@RequestMapping("index/mainmenu")
	public String getMainDate(Map<String, Object> result) throws ServiceException {

		MerchantAccountInfo merchant = MerchantUtils.getMerchant();

		// 个人总冻结金额
//		String allFrezzAmount = merchantFrezzService.allMerchantFrezzAmount(merchant.getUuid());

		// 个人可用金额
		MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchant.getUuid());
		
		MerchantMainDateVO merchantGroupDate = merchantRechargeOrderService.getMerchantGroupDate(merchant.getUuid());
		
		MerchantRechargeOrderVO param = new MerchantRechargeOrderVO();
		param.setMerchantId(merchant.getUuid());
		Date[] todayTime = DateUtils.getTodayTime();
		param.setBeginTime(todayTime[0]);
		param.setEndTime(todayTime[1]);
		MerchantMainDateVO todayMerchantGroupDate = merchantRechargeOrderService.getTodayMerchantOrder(param);
		
//		result.put("allfrezzamount", allFrezzAmount);
		result.put("merchantFinance", merchantFinance);
		// 总
		result.put("merchantGroupDate", merchantGroupDate);
		// 今日
		result.put("todayMerchantGroupDate", todayMerchantGroupDate);
		
		
		
		return "admin/indexmenu";
	}



}
