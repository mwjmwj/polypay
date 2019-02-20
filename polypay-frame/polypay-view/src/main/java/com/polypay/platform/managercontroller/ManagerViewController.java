package com.polypay.platform.managercontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("managerview")
public class ManagerViewController {

	@RequestMapping("merchantaccountlist")
	public String toMerchantAccountList(){
		return "adminmanager/allmerchantaccountinfolist";
	}
	
	@RequestMapping("merchantplacelist")
	public String toMerchantPlaceList(){
		return "adminmanager/allmerchantplacelist";
	}
	
	@RequestMapping("merchantsettlelist")
	public String toMerchantSettleList(){
		return "adminmanager/allmerchantsettlelist";
	}
	@RequestMapping("merchant/register")
	public String toMerchantRegister()
	{
		return "adminmanager/merchantregister";
	}
	
	
	@RequestMapping("merchantrechargelist")
	public String toMerchantRechargeList(){
		return "adminmanager/allmerchantrechargelist";
	}
	
	
}
