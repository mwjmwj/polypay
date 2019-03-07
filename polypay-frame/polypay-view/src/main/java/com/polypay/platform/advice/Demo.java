package com.polypay.platform.advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class Demo {

//	public static void main(String[] args) {
//
//		// 接口路径
//		String basePath = "http://api.yundesun.com/apisubmit?";
//
//		// 版本
//		String version = "1.0";
//
//		// 商户号
//		String customerid = "10989";
//
//		// 订单金额
//		BigDecimal total_fee = new BigDecimal("100").setScale(2, BigDecimal.ROUND_HALF_UP);
//
//		// 订单号 (订单号由自己系统提供)
//		String sdorderno = "3";
//
//		// 返回回调
//		// 同步回调不作为订单最终状态随意回调一个
//		String returnurl = "http://212.64.72.200:8889/TradeService/PayInfo?orderid=xxx";
//
//		// apikey
//		String apiKey = "95de2d2d4b2dc95efb9e9c8981dd7743a110a438";
//
//		// 支付类型
//		String paytype = "usdt";
//
//		// 异步回调
//		// 异步回调为最终订单状态
//		// 此处不填写 提供回调接口的参数
//		// 类型 POST
//		/**
//		 * {value} 为对应的值 由平台填写 入参：
//		 * customerid={value}&status={value}&sdpayno={value}&sdorderno={value}&total_fee={value}&paytype={value}&{apikey}
//		 * status: 1成功，其他失败 customerid： 商户号 sdpayno:平台订单号 sdorderno: 商户订单号 total_fee
//		 * :订单金额
//		 * 
//		 * 
//		 * customerid={value}&status={value}&sdpayno={value}&sdorderno={value}&total_fee={value}&paytype={value}&{apikey}
//		 * 根据上述参数顺序 拼接参数字符串 使用MD5加密 验证参数 添加加密sign 拼接在url末端
//		 */
//		// 订单查询接口：
//		String notifyurl = "http://212.64.72.200:8889/TradeService/PayReBack";
//		// 最终平台回调为：
//		// http://212.64.72.200:8889/TradeService/PayReBack?customerid=10989&status=1&sdpayno=xxxx&sdorderno={平台订单号}&total_fee=10.0&paytype={支付类型}&95de2d2d4b2dc95efb9e9c8981dd7743a110a438&sign={加密签名}
//		// 商户需要验签后 返回 success 为确认收到回调，否则继续回调
//
//		
//		
//		// 商户根据平台回调接口后调查询订单接口获取订单状态
//		// 查询订单url ：http://api.yundesun.com/apiorderquery
//		// 类型：GET/POST 参数: customerid={value}&sdorderno={value}&reqtime={value}&{apikey}
//		// customerid ： 商户号， sdorderno： 商户生成的订单号， reqtime：时间（yyyymmddhhmmss）
//		// 根据参数顺序 customerid={value}&sdorderno={value}&reqtime={value}&{apikey} MD5 加密签名
//		// 拼接sign
//		// 最终查询订单url:
//		// http://api.yundesun.com/apiorderquery?customerid=10989&sdorderno=xxxx&reqtime=20190221225130&95de2d2d4b2dc95efb9e9c8981dd7743a110a438&sign={加密签名}
//		/**
//		 * 订单查询 返回值： 
//		 * 成功： {"status":1,"msg":"成功订单","sdorderno":"商户订单号","total_fee":"订单金额","sdpayno":"平台订单号"}
//		 * 失败： {"status":0,"msg":"失败订单"}
//		 */
//
//		// 拼接签名url
//		StringBuffer signBuf = new StringBuffer();
//		signBuf.append("version=" + version).append("&customerid=" + customerid)
//				.append("&total_fee=" + total_fee.toString()).append("&sdorderno=" + sdorderno)
//				.append("&notifyurl=" + notifyurl).append("&returnurl=" + returnurl).append("&" + apiKey);
//
//		// 加密签名
//		String sign = MD5.md5(signBuf.toString());
//
//		// 拼接最终url
//		String sendPath = basePath + signBuf.toString() + "&paytype=" + paytype + "&sign=" + sign;
//		System.out.println(sendPath);
//
//		// 根据自己的代码 选择发送http请求
////		HttpClientUtil.httpGet(sendPath);
//
//	}
	public static void main(String[] args) {
		
//			frezzAmount = arrivalAmount.multiply(new BigDecimal(frezzrate)).setScale(4, BigDecimal.ROUND_HALF_UP);
			BigDecimal bigDecimal = new BigDecimal(0);
			BigDecimal setScale = new BigDecimal(10).multiply(bigDecimal).setScale(4, BigDecimal.ROUND_HALF_UP);
			System.out.println(setScale);
	}

	
}
