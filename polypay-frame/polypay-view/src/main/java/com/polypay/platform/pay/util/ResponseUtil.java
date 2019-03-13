package com.polypay.platform.pay.util;

import java.util.Map;

import com.polypay.platform.pay.secret.CertUtil;

public class ResponseUtil {
	
	/**
	 * 解析返回数据
	 * @param response
	 * @return
	 */
	public static Map parseResponse(String response){
		//解析返回信息到map中
		Map transMap = ParamUtil.getParamsMap(response, "utf-8");
		//获取签名
		String sign = (String) transMap.get("sign");
		sign = sign.replaceAll(" ", "+");
		transMap.remove("sign");
		//验签
		String transData = ParamUtil.getSignMsg(transMap);
		boolean result = false;
		try {
			result = CertUtil.getInstance().verify(transData, sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!result){
			transMap.clear();
			transMap.put("tranData", transData);
			transMap.put("sign", sign);
			transMap.put("msg", "验签失败");
		}
		return transMap;
	}

}
