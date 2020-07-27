package com.polypay.platform.advice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.utils.MD5;

/**
 * 
 * @author mwj
 *
 */
//@ControllerAdvice
public class ControllerExceptionAdvice {
	
	private Logger log = LoggerFactory.getLogger(ControllerExceptionAdvice.class);
	
	
	/**
	 *  <异常拦截器拦截>
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler({ServiceException.class,Exception.class})
	@ResponseBody
	public ServiceResponse exception(HttpServletRequest request, ServiceException ex){
		ServiceResponse response = new ServiceResponse();
		ResponseUtils.exception(response, "内部错误", ex.getStatus());
		log.error(String.format("requestid:%s,controller:%s,error:%s,parameter:%s",response.getRequestId(),request.getRequestURI(),ex.getMessage(),JSONObject.toJSON(request.getParameterMap())));
		return response;
	}
 public static void main(String[] args) {
	
	 System.out.println(MD5.md5("123456"));
	 
	 
}

}
