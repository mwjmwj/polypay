package com.polypay.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class ViewController {
	
	@RequestMapping("goodsList")
	public String pathControl(){
		return "admin/goodslist";
	}
	@RequestMapping("addGoods")
	public String toAddGoods(){
		return "admin/goodsadd";
	}
	@RequestMapping("login")
	public String toLogin(){
		return "login";
	}
	@RequestMapping("register")
	public String toRegister(){
		return "register";
	}
	@RequestMapping("usercenter")
	public String toUserCenter(){
		return "userview/usercenter";
	}
	@RequestMapping("index")
	public String toIndex(){
		return "index";
	}
	@RequestMapping("index1")
	public String toIndex1(){
		return "index1";
	}
	@RequestMapping("cart")
	public String toCart(){
		return "userview/shopping_cart";
	}
	@RequestMapping("admin")
	public String toAdmin(){
		return "adminlogin";
	}
	@RequestMapping("orderlist")
	public String toOrderList(){
		return "admin/orderlist";
	}
	@RequestMapping("userlist")
	public String toUserList(){
		return "admin/userlist";
	}
	@RequestMapping("bannerlist")
	public String toBannerList(){
		return "admin/bannerlist";
	}
	@RequestMapping("addbanner")
	public String toAddBanner(){
		return "admin/banneradd";
	}
	@RequestMapping("evaluate")
	public String toEva(){
		return "userview/evaluate";
	}
	@RequestMapping("evalist")
	public String toEvalist(){
		return "admin/evalist";
	}
	@RequestMapping("welcome")
	public String toWelcome(){
		return "admin/welcome";
	}
	@RequestMapping("toAdminIndex")
	public String toAdminIndex(){
		return "admin/index";
	}
	@RequestMapping("admininfo")
	public String adminInfo(){
		return "admin/info";
	}
	@RequestMapping("updatepass")
	public String updatePass(){
		return "admin/updatepass";
	}
	@RequestMapping("typelist")
	public String toTypeList(){
		return "admin/typelist";
	}
	@RequestMapping("merchantRechargeList")
	public String toMerchantRechargeList(){
		return "admin/merchantrechargelist";
	}
	
	@RequestMapping("merchantSuccessRechargeList")
	public String toMerchantSuccessRechargeList(){
		return "admin/merchantsuccessrechargelist";
	}
	
	
	@RequestMapping("merchantFailRechargeList")
	public String toMerchantFailRechargeList(){
		return "admin/merchantfailrechargelist";
	}
	
	@RequestMapping("merchantbindbankList")
	public String toMerchantBindbankList(){
		return "admin/merchantbindbanklist";
	}
	
	@RequestMapping("merchantsettleList")
	public String toMerchantSettleList()
	{
		return "admin/merchantsettleorderlist";
	}
	
	@RequestMapping("merchantplaceList")
	public String toMerchantPlaceList()
	{
		return "admin/merchantplaceorderlist";
	}
	
	@RequestMapping("merchantfrezzList")
	public String toMerchantFrezzList()
	{
		return "admin/merchantfrezzlist";
	}
	
	@RequestMapping("merchantBindBank")
	public String toMerchantBindBank()
	{
		return "admin/addmerchantbindbank";
	}
	
	@RequestMapping("loginlog")
	public String toLoginLog()
	{
		return "admin/merchantloginlog";
	}
	
	@RequestMapping("paypageList")
	public String toPaypageList()
	{
		return "admin/merchantpaytype";
	}

}
