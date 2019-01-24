package com.polypay.platform.aspect;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.consts.MerchantGlobaKeyConsts;
import com.polypay.platform.consts.RoleConsts;
import com.polypay.platform.exception.ServiceException;

/**
 * 用戶接口權限校驗類
 * 
 * @author tom
 *
 *         2018年11月26日
 */
@Component
@Aspect
public class ManagerAspect {
	private final static Integer PERMISSION_DENIED = -9007;
	public final static String TOKEN = "token";

	@Pointcut("execution(* com.polypay.platform.managercontroller.*.*(..))")
	public void pointCut() {

	}

	@Before("pointCut()")
	public void before(JoinPoint joinPoint) throws ServiceException, IOException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		// 获取请求的request
		HttpServletRequest request = attributes.getRequest();
		HttpServletResponse response = attributes.getResponse();

		Object user = request.getSession().getAttribute(MerchantGlobaKeyConsts.USER);

		if (null == user) {
			response.sendRedirect("admin");
			return;
		}
		
		if (((MerchantAccountInfo) user).getRoleId() != RoleConsts.PROXY
				|| ((MerchantAccountInfo) user).getRoleId() != RoleConsts.MANAGER) {
			response.sendRedirect("toAdminIndex");
			return;
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
