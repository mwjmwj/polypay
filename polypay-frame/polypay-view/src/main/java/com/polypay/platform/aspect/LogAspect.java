package com.polypay.platform.aspect;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author mwj
 *
 */
@Component
@Aspect
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
	private static final ThreadLocal<Long> timeTreadLocal = new ThreadLocal<>();


	@Pointcut("execution(* com.polypay.platform.controller.*.*(..))")
	public void pointCut() {
	}

	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		timeTreadLocal.set(System.currentTimeMillis());
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		// 获取请求的request
		HttpServletRequest request = attributes.getRequest();
		// 获取所有请求的参数，封装为map对象
		// Map<String,Object> parameterMap = getParameterMap(request);
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// 获取被拦截的方法
		Method method = methodSignature.getMethod();
		// 获取被拦截的方法名
		String methodName = method.getName();
		logger.info("AOP begin ,请求开始方法  :{}", method.getDeclaringClass() + "." + methodName + "()");
		// 获取所有请求参数key和value
		String keyValue = getReqParameter(request);
		logger.info("请求url = {}", request.getRequestURL().toString());
		logger.info("请求方法requestMethod = {}", request.getMethod());
		logger.info("请求资源uri = {}", request.getRequestURI());
		logger.info("所有的请求参数 key：value = {}", keyValue);

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

	public String getReqParameter(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		Enumeration<String> enumeration = request.getParameterNames();
		JSONArray jsonArray = new JSONArray();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getParameter(key);
			JSONObject json = new JSONObject();
			json.put(key, value);
			jsonArray.add(json);
		}
		return jsonArray.toString();
	}

}
