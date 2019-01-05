package com.polypay.platform.aspect;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.polypay.platform.exception.ServiceException;

/**
 *  用戶接口權限校驗類
 * @author tom
 *
 * 2018年11月26日
 */
@Component
@Aspect
public class AuthAspect {
	private final static Integer PERMISSION_DENIED = -9007;
	public final static String TOKEN = "token";

	@Pointcut("execution(* com.polypay.platform.controller.*.*(..)) && !execution(* com.polypay.platform.controller.MerchantLoginController.*(..))")
	public void pointCut()
	{
		
	}
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) throws ServiceException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		// 获取请求的request
		HttpServletRequest request = attributes.getRequest();

		String token = request.getHeader(TOKEN);

		Object avaliableToken = request.getSession().getAttribute(TOKEN);

		if (null == avaliableToken) {
			throw new ServiceException("权限不足", PERMISSION_DENIED);
		}
		if (!avaliableToken.equals(token)) {
			throw new ServiceException("权限不足", PERMISSION_DENIED);

		}

	}

	/**
	 * 获取所有请求参数，封装为map对象
	 *
	 * @return
	 */
	public Map<String, Object> getParameterMap(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		Enumeration<String> enumeration = request.getParameterNames();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		StringBuilder stringBuilder = new StringBuilder();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getParameter(key);
			String keyValue = key + " : " + value + " ; ";
			stringBuilder.append(keyValue);
			parameterMap.put(key, value);
		}
		return parameterMap;
	}
}
