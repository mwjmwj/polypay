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
	
	/**
	 * 商户注册页
	 * @return
	 */
	@RequestMapping("merchantregister")
	public String register() {
		return "proxymanager/merchantregister";
	}
	
	/**
	 * 商户列表页
	 * @return
	 */
	@RequestMapping("allmerchantaccountinfolist")
	public String merchantlist() {
		return "proxymanager/allmerchantaccountinfolist";
	}
	
	@RequestMapping("merchantpaytype")
	public String merchantpaytypelist() {
		return "adminmanager/merchantpaytype";
	}
	@RequestMapping("proxylist")
	public String toProxyMerchantlist() {
		return "adminmanager/allproxymerchantaccountinfolist";
	}
	
	@RequestMapping("addproxy")
	public String toAddProxy()
	{
		return "adminmanager/addproxyaccount";
	}
	
	@RequestMapping("proxy/rechargeorder")
	public String toProxyRecharge()
	{
		return "proxymanager/allmerchantrechargelist";
	}
	
	
	@RequestMapping("proxy/settleorder")
	public String toProxySettle()
	{
		return "proxymanager/allmerchantsettlelist";
	}
	
	
	@RequestMapping("proxy/placeorder")
	public String toProxyPlace()
	{
		return "proxymanager/allmerchantplacelist";
	}
	
	@RequestMapping("merchantapilist")
	public String toMerchantApiList()
	{
		return "adminmanager/merchantapilist";
	}
	
	
	@RequestMapping("merchantfinancelist")
	public String toMerchantFinanceList()
	{
		return "adminmanager/merchantfinancelist";
	}
	
	@RequestMapping("updateproxyloginpwd")
	public String updateProxyPwd()
	{
		return "proxymanager/proxymerchantloginpwd";
	}
	
	
}
