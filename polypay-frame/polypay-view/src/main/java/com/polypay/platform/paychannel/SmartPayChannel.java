package com.polypay.platform.paychannel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.utils.MD5;

public class SmartPayChannel implements IPayChannel {

	@Override
	public void sendRedirect(Map<String, Object> param, HttpServletResponse response) {

		// 接口路径
		String basePath = "http://api.yundesun.com/apisubmit?";

		// 版本
		String version = "1.0";

		// 商户号
		String customerid = "10989";

		// 订单金额
		Object pay_amount = param.get("pay_amount");
		BigDecimal total_fee = new BigDecimal(pay_amount.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);

		
		// 订单号 (订单号由自己系统提供)
		Object order_number = param.get("we_order_number");
		String sdorderno = order_number.toString();

		// 返回回调
		// 同步回调不作为订单最终状态随意回调一个
		String returnurl = "http://xx/callback/PayInfo";

		// apikey
		String apiKey = "95de2d2d4b2dc95efb9e9c8981dd7743a110a438";

		// 支付类型
		String paytype = param.get("pay_channel").toString();

		// 异步回调
		// 异步回调为最终订单状态
		// 此处不填写 提供回调接口的参数
		// 类型 POST
		/**
		 * {value} 为对应的值 由平台填写 入参：
		 * customerid={value}&status={value}&sdpayno={value}&sdorderno={value}&total_fee={value}&paytype={value}&{apikey}
		 * status: 1成功，其他失败 customerid： 商户号 sdpayno:平台订单号 sdorderno: 商户订单号 total_fee
		 * :订单金额
		 * 
		 * 
		 * customerid={value}&status={value}&sdpayno={value}&sdorderno={value}&total_fee={value}&paytype={value}&{apikey}
		 * 根据上述参数顺序 拼接参数字符串 使用MD5加密 验证参数 添加加密sign 拼接在url末端
		 */
		// 订单查询接口：
		String notifyurl = "http://47.104.181.26/open/api/recharge/back";
		// 最终平台回调为：
		// http://212.64.72.200:8889/TradeService/PayReBack?customerid=10989&status=1&sdpayno=xxxx&sdorderno={平台订单号}&total_fee=10.0&paytype={支付类型}&95de2d2d4b2dc95efb9e9c8981dd7743a110a438&sign={加密签名}
		// 商户需要验签后 返回 success 为确认收到回调，否则继续回调

		// 商户根据平台回调接口后调查询订单接口获取订单状态
		// 查询订单url ：http://api.yundesun.com/apiorderquery
		// 类型：GET/POST 参数: customerid={value}&sdorderno={value}&reqtime={value}&{apikey}
		// customerid ： 商户号， sdorderno： 商户生成的订单号， reqtime：时间（yyyymmddhhmmss）
		// 根据参数顺序 customerid={value}&sdorderno={value}&reqtime={value}&{apikey} MD5 加密签名
		// 拼接sign
		// 最终查询订单url:
		// http://api.yundesun.com/apiorderquery?customerid=10989&sdorderno=xxxx&reqtime=20190221225130&95de2d2d4b2dc95efb9e9c8981dd7743a110a438&sign={加密签名}
		/**
		 * 订单查询 返回值： 成功：
		 * {"status":1,"msg":"成功订单","sdorderno":"商户订单号","total_fee":"订单金额","sdpayno":"平台订单号"}
		 * 失败： {"status":0,"msg":"失败订单"}
		 */

		// 拼接签名url
		StringBuffer signBuf = new StringBuffer();
		signBuf.append("version=" + version).append("&customerid=" + customerid)
				.append("&total_fee=" + total_fee.toString()).append("&sdorderno=" + sdorderno)
				.append("&notifyurl=" + notifyurl).append("&returnurl=" + returnurl).append("&" + apiKey);

		// 加密签名
		String sign = MD5.md5(signBuf.toString());

		// 拼接最终url
		String sendPath = basePath + signBuf.toString() + "&paytype=" + paytype + "&sign=" + sign;

		// 根据自己的代码 选择发送http请求
		try {
			response.sendRedirect(sendPath);
		} catch (IOException e) {
		}

	}

	@Override
	public Map<String, Object> checkOrder(HttpServletRequest request) {
		
		Map<String, Object> result = Maps.newHashMap();
		
		String customerid = getParameter(request, "customerid");
		
		String status = getParameter(request, "status");
		
		// 商户订单号
		String sdpayno = getParameter(request, "sdpayno");
		
		// 平台订单号
		String sdorderno = getParameter(request, "sdorderno");
		
		String total_fee = getParameter(request, "total_fee");
		
		String paytype = getParameter(request, "paytype");
		
		String api_key = "95de2d2d4b2dc95efb9e9c8981dd7743a110a438";
		
		String sign = getParameter(request, "sign");
		
		StringBuffer sb = new StringBuffer();
		sb.append("customerid="+customerid)
		.append("&status="+status)
		.append("&sdpayno="+sdpayno)
		.append("&sdorderno="+sdorderno)
		.append("&total_fee="+total_fee)
		.append("&paytype="+paytype)
		.append("&"+api_key);
		
		if(!MD5.md5(sb.toString()).equals(sign))
		{
			result.put("status", "-10");
			return result;
		}
		
		result.put("total_fee", total_fee);
		result.put("payno", sdpayno);
		result.put("orderno", sdorderno);
		result.put("channel", "WY");
		result.put("status", status);
		
		return result;
	}
	
	public String getParameter(HttpServletRequest request, String key) {
		String parameter = request.getParameter(key);
		return StringUtils.isEmpty(parameter) ? "" : parameter;
	}

	
	@Override
	public Map<String, Object> getOrder(String channelOrderNumber) {
		
		String baseUrl = "http://api.yundesun.com/apiorderquery?";
		StringBuffer signParam = new StringBuffer();
		signParam.append("customerid=10989")
		.append("&sdorderno="+channelOrderNumber)
		.append("&reqtime="+DateUtils.getOrderTime())
		.append("&95de2d2d4b2dc95efb9e9c8981dd7743a110a438");
		String sign = MD5.md5(signParam.toString());
		
		baseUrl +=  signParam.toString()+"&sign="+sign;
		
		HttpRequestDetailVo httpGet = HttpClientUtil.httpGet(baseUrl);
		
		Map result = (Map)JSONUtils.parse(httpGet.getResultAsString());
		// {"status":1,"msg":"成功订单","sdorderno":"商户订单号","total_fee":"订单金额","sdpayno":"平台订单号"}
		// {"status":0,"msg":"失败订单"}
		
		return result;
	}

	
	/**
	 *  结算调用第三方
	 */
	 
	@Override
	public Map<String, Object> settleOrder(MerchantSettleOrder settleOrder) {
		
		String basePath = "http://api.yundesun.com/apisettle";
		
		String customerid = "10989";
		
		String serial = settleOrder.getOrderNumber();
		
//		customerid={value}&serial={value}&bankname={value}&cardno={value}&accountname={value}&provice={value}&city={value}&branchname={value}&total_fee={value}&{apikey}
		
		String bankname = settleOrder.getBankName();
		
		String cardno = settleOrder.getMerchantBindBank();
		
		String accountname = settleOrder.getAccountName();
		
		String provice = settleOrder.getAccountProvice();
		
		String city = settleOrder.getAccountCity();
		
		String branchname = settleOrder.getBranchBankName();
		
		// 100 - 5 + 3 
		BigDecimal total_fee = settleOrder.getPostalAmount().subtract(settleOrder.getServiceAmount()).add(new BigDecimal(3)).setScale(2, BigDecimal.ROUND_DOWN);
		
		String bankcode = settleOrder.getBankCode();
		
		String api_key = "95de2d2d4b2dc95efb9e9c8981dd7743a110a438";
		
//		customerid={value}&serial={value}&bankname={value}&cardno={value}&
//				accountname={value}&provice={value}&city={value}&branchname={value}&total_fee={value}&{apikey}
		StringBuffer signParam = new StringBuffer();
		signParam.append("customerid="+customerid)
		.append("&serial="+serial)
		.append("&bankname="+bankname)
		.append("&cardno="+cardno)
		.append("&accountname="+accountname)
		.append("&provice="+provice)
		.append("&city="+city)
		.append("&branchname="+branchname)
		.append("&total_fee="+total_fee)
		.append("&"+api_key);
		
		
		String sign = MD5.encryption(signParam.toString());

		Map<String,String> param = Maps.newHashMap();
		param.put("customerid", customerid);
		param.put("serial", serial);
		param.put("bankname", bankname);
		param.put("cardno", cardno);
		param.put("accountname", accountname);
		param.put("provice", provice);
		param.put("city", city);
		param.put("branchname", branchname);
		param.put("total_fee", total_fee.toString());
		param.put("sign", sign);
		param.put("bankcode", bankcode);
		
		HttpRequestDetailVo httpGet = HttpClientUtil.httpPost(basePath, null, param);

		
		String resultAsString = httpGet.getResultAsString();
		Map result = (Map)JSONUtils.parse(resultAsString);
		
		//{"status":1,"msg":"代付申请成功，系统处理中","serial":"代付订单号"}
		//{"status":0,"msg":"代付失败"}
		if("代付订单号重复".equals(result.get("msg")))
		{
			result.put("status", 2);
		}
		
		return result;
	}

	@Override
	public Map<String, Object> placeOrder(MerchantPlaceOrder settleOrder) {
		String basePath = "http://api.yundesun.com/apisettle?";
		
		String customerid = "10989";
		
		String serial = settleOrder.getOrderNumber();
		
//		customerid={value}&serial={value}&bankname={value}&cardno={value}&accountname={value}&provice={value}&city={value}&branchname={value}&total_fee={value}&{apikey}
		
		String bankname = settleOrder.getBankName();
		
		String cardno = settleOrder.getBankNumber();
		
		String accountname = settleOrder.getAccountName();
		
		String provice = settleOrder.getAccountProvice();
		
		String city = settleOrder.getAccountCity();
		
		String branchname = settleOrder.getBranchBankName();
		
		BigDecimal total_fee = settleOrder.getPayAmount().subtract(settleOrder.getServiceAmount()).add(new BigDecimal(3)).setScale(2, BigDecimal.ROUND_DOWN);
		
		String bankcode = settleOrder.getBankCode();
		
		String api_key = "95de2d2d4b2dc95efb9e9c8981dd7743a110a438";
		
		StringBuffer signParam = new StringBuffer();
		signParam.append("customerid="+customerid)
		.append("&serial="+serial)
		.append("&bankname="+bankname)
		.append("&cardno="+cardno)
		.append("&accountname="+accountname)
		.append("&provice="+provice)
		.append("&city="+city)
		.append("&branchname="+branchname)
		.append("&total_fee="+total_fee)
		.append("&"+api_key);

		String sign = MD5.encryption(signParam.toString());

		Map<String,String> param = Maps.newHashMap();
		param.put("customerid", customerid);
		param.put("serial", serial);
		param.put("bankname", bankname);
		param.put("cardno", cardno);
		param.put("accountname", accountname);
		param.put("provice", provice);
		param.put("city", city);
		param.put("branchname", branchname);
		param.put("total_fee", total_fee.toString());
		param.put("sign", sign);
		param.put("bankcode", bankcode);
		HttpRequestDetailVo httpGet = HttpClientUtil.httpPost(basePath, null, param);


		String resultAsString = httpGet.getResultAsString();
		Map result = (Map)JSONUtils.parse(resultAsString);
		
		//{"status":1,"msg":"代付申请成功，系统处理中","serial":"代付订单号"}
		//{"status":0,"msg":"代付失败"}
		
		if("代付订单号重复".equals(result.get("msg")))
		{
			result.put("status", 2);
		}
		
		
		return result;
	}

}
