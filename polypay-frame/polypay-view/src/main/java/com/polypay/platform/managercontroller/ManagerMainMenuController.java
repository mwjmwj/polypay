package com.polypay.platform.managercontroller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantFrezzService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

@Controller
public class ManagerMainMenuController {

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;

	@Autowired
	private IMerchantFrezzService merchantFrezzService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;
	
	private void get()
	{
		
	}

	@RequestMapping("main/mainmenu")
	public String getManagetMainMenu(Map<String, Object> result) throws ServiceException {

		// 个人可用金额
		MerchantFinance allMerchantFinance = merchantFinanceService.allMerchantFinance();

		 // 所有时间
		MerchantMainDateVO merchantGroupDate = merchantRechargeOrderService.getMerchantGroupDate(null);

		MerchantRechargeOrderVO param = new MerchantRechargeOrderVO();
		Date[] todayTime = DateUtils.getTodayTime();
		param.setBeginTime(todayTime[0]);
		param.setEndTime(todayTime[1]);
		
		// 当日
		MerchantMainDateVO todayMerchantGroupDate = merchantRechargeOrderService.getTodayMerchantOrder(param);

		result.put("allMerchantFinance", allMerchantFinance);
		// 总
		result.put("merchantGroupDate", merchantGroupDate);
		// 今日
		result.put("todayMerchantGroupDate", todayMerchantGroupDate);

		return "adminmanager/main";
	}

}
