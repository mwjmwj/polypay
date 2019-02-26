package com.polypay.platform.paychannel;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;

public interface IPayChannel {

	public void sendRedirect(Map<String, Object> param, HttpServletResponse response);
	
	public Map<String,Object> checkOrder(HttpServletRequest request);
	
	public Map<String,Object> getOrder(String orderNumber);

	public Map<String, Object> settleOrder(MerchantSettleOrder selectByPrimaryKey);
	
	public Map<String, Object> placeOrder(MerchantPlaceOrder selectByPrimaryKey);

}
