package com.polypay.platform.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.polypay.platform.bean.IpAdressResult;

public class IPUtils {
	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * 
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
	 * 
	 * 用户真实IP为： 192.168.1.110
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@SuppressWarnings("rawtypes")
	public static String getLoginAddress(HttpServletRequest request) {
		String ip = getIpAddress(request);
		if (StringUtils.isEmpty(ip)) {
			return null;
		}
		try {
			HttpRequestDetailVo httpGet = HttpClientUtil.httpGet("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
			httpGet.getResultAsString();
			IpAdressResult parseObject = JSON.parseObject(httpGet.getResultAsString(), IpAdressResult.class);
			Map data = parseObject.getData();
			return data.get("country") + "-" + data.get("region") + "-" + data.get("city");
		} catch (Exception e) {

		}
		
		return null;
	}

}
