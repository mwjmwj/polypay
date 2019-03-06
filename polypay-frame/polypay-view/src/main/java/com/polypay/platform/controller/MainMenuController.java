package com.polypay.platform.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantFrezzService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantMainDateVO;

@Controller
public class MainMenuController {

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;
	
	@Autowired
	private IMerchantFrezzService merchantFrezzService;
	
	@Autowired
	private IMerchantFinanceService merchantFinanceService;
	
	
	@RequestMapping("index/mainmenu")
	public String getMainDate(Map<String,Object> result) throws ServiceException{
		
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
//		
//		// 图标数据
//		List<MerchantMainDateVO> echarData = merchantRechargeOrderService.allTimeMerchantOrder(merchant.getUuid());
//		
//		String[] ehtimes = new String[echarData.size()];
//		String[] ehOrderNumber = new String[echarData.size()];
//		BigDecimal[] ehOrderAmount = new BigDecimal[echarData.size()];
//		
//		int index = 0;
//		
//		// 图标数据赋值
//		for (MerchantMainDateVO merchantMainDateVO : echarData) {
//			ehtimes[index] = merchantMainDateVO.getCreateTime();
//			ehOrderNumber[index] = merchantMainDateVO.getMerchantAllOrderNumber();
//			ehOrderAmount[index] = merchantMainDateVO.getMerchantAllRechargeAmount();
//			index++;
//		}
//		
//		result.put("ehtimes", JSON.toJSON(ehtimes));
//		result.put("ehOrderNumbers", JSON.toJSON(ehOrderNumber));
//		result.put("ehOrderAmounts", JSON.toJSON(ehOrderAmount));
//		
//		
//		// 个人总冻结金额
//		String allFrezzAmount = merchantFrezzService.allMerchantFrezzAmount(merchant.getUuid());
//		
//		//个人可用金额
		MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchant.getUuid());
//		
//		result.put("allfrezzamount", allFrezzAmount);
		result.put("merchantFinance", merchantFinance);
//		
		
		
		return "admin/indexmenu";
	}
	

}
