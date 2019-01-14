package com.polypay.platform.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantMainDateVO;

@Controller
public class MainMenuController {

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;
	
	
	@RequestMapping("index/mainmenu")
	public String getMainDate(Map<String,Object> result) throws ServiceException{
		
		// 总订单 当日订单
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantMainDateVO data = merchantRechargeOrderService.getMerchantGroupDate(merchant.getUuid());
		result.put("data", data);
		
		List<MerchantMainDateVO> echarData = merchantRechargeOrderService.allTimeMerchantOrder(merchant.getUuid());
		
		String[] ehtimes = new String[echarData.size()];
		String[] ehOrderNumber = new String[echarData.size()];
		BigDecimal[] ehOrderAmount = new BigDecimal[echarData.size()];
		
		int index = 0;
		
		//赋值
		for (MerchantMainDateVO merchantMainDateVO : echarData) {
			ehtimes[index] = merchantMainDateVO.getCreateTime();
			ehOrderNumber[index] = merchantMainDateVO.getMerchantAllOrderNumber();
			ehOrderAmount[index] = merchantMainDateVO.getMerchantAllRechargeAmount();
			index++;
		}
		
		result.put("ehtimes", JSON.toJSON(ehtimes));
		result.put("ehOrderNumbers", JSON.toJSON(ehOrderNumber));
		result.put("ehOrderAmounts", JSON.toJSON(ehOrderAmount));
		
		return "admin/indexmenu";
	}

}
