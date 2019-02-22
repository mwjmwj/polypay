package com.polypay.platform.paychannel;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IPayChannel {

	public void sendRedirect(Map<String, Object> param, HttpServletResponse response);
	
	public Map<String,Object> checkOrder(HttpServletRequest request);
	
	public Map<String,Object> getOrder(String orderNumber);
	
}
