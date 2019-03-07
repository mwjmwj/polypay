package com.polypay.platform.paychannel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.utils.MD5;

public class SiChuanPayChannel implements IPayChannel {

	@SuppressWarnings("rawtypes")
	@Override
	public void sendRedirect(Map<String, Object> param, HttpServletResponse response) {

		// 接口路径
		String basePath = "http://39.108.126.141/pay/unifiedorder";

		Map<String, String> preSignParam = Maps.newHashMap();

		// 订单金额
		Object pay_amount = param.get("pay_amount");
		BigDecimal total_amount = new BigDecimal(pay_amount.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		preSignParam.put("total_amount", total_amount.toString());

		// 订单号 (订单号由自己系统提供)
		Object order_number = param.get("we_order_number");
		String sdorderno = order_number.toString();

		preSignParam.put("out_trade_no", sdorderno);

		preSignParam.put("order_name", "手机");

		preSignParam.put("spbill_create_ip", "127.0.0.1");

		// 订单查询接口：
		String notifyurl = param.get("call_back").toString();
		preSignParam.put("notify_url", notifyurl);

		// 支付类型
		String paytype = param.get("pay_channel").toString();
		preSignParam.put("paytype", paytype);

		String successurl = "www.baidu.com";

		String bankcode = param.get("bank_no").toString();

		preSignParam.put("bankcode", bankcode);

		preSignParam.put("successurl", successurl);

		String appid = "152245082772849808";

		preSignParam.put("appid", appid);

		String sign = getSign(preSignParam);

		preSignParam.put("sign", sign);
		
		preSignParam.put("sign_type", "MD5");

		HttpRequestDetailVo httpPost = HttpClientUtil.httpPost(basePath, null, preSignParam);

		Map iresult = (Map) JSONUtils.parse(httpPost.getResultAsString());

		if (!"200".equals(iresult.get("bxcode").toString())) {
			return;
		}

		Object object = iresult.get("pay_url");

		try {
			response.sendRedirect(object.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * {"appid":"152245082772849808","bxcode":"200","bxmsg":"下单成功","bxstatus":"SUCCESS",
		 * "out_trade_no":"1","pay_url":"http://39.108.126.141/redirect?redirectKey=1B183DA2277506CB6389C4248C49EE1F"}
		 * 
		 */

	}

//	public static void main(String[] args) {
//		
//		
//
//			// 接口路径
//			String basePath = "http://39.108.126.141/pay/unifiedorder";
//			
//			Map<String,String> preSignParam = Maps.newHashMap();
//
//
//			// 订单金额
//			Object pay_amount = "10.0";
//			BigDecimal total_amount = new BigDecimal(pay_amount.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
//			preSignParam.put("total_amount", total_amount.toString());
//			
//			// 订单号 (订单号由自己系统提供)
//			Object order_number = "1";
//			String sdorderno = order_number.toString();
//
//			preSignParam.put("out_trade_no", sdorderno);
//			
//			preSignParam.put("order_name", "手机");
//			
//			preSignParam.put("spbill_create_ip", "127.0.0.1");
//			
//			// 订单查询接口：
//			String notifyurl = "www.baidu.com";
//			preSignParam.put("notify_url", notifyurl);
//
//			// 支付类型
//			String paytype = "100050";
//			preSignParam.put("paytype", paytype);
//			
//			String successurl = "www.baidu.com";
//			
//			String bankcode = "6227002930070614333";
//			
//			preSignParam.put("bankcode", bankcode);
//
//			preSignParam.put("successurl", successurl);
//			
//			String appid = "152245082772849808";
//			
//			preSignParam.put("appid", appid);
//
//			String sign = getSign(preSignParam);
//			
//			preSignParam.put("sign", sign);
//			
//			preSignParam.put("sign_type", "MD5");
//			
//			HttpRequestDetailVo httpPost = HttpClientUtil.httpPost(basePath,null, preSignParam);
//			System.out.println(httpPost.getResultAsString());
//	}

	@Override
	public Map<String, Object> checkOrder(HttpServletRequest request) {

		Map<String, String> signP = Maps.newHashMap();

		// 状态码
		String code = getParameter(request, "code");
		signP.put("code", code);

		// 订单号
		String order = getParameter(request, "order");
		signP.put("order", order);

		// 金额
		String mount = getParameter(request, "mount");
		signP.put("mount", mount);

		// 消息
		String msg = getParameter(request, "msg");
		signP.put("msg", msg);

		// 时间
		String time = getParameter(request, "time");
		signP.put("time", time);

		// 签名
		String sign = getParameter(request, "sign");

		String sign2 = getSign(signP);

		Map<String, Object> result = Maps.newHashMap();

		if (!sign2.equals(sign)) {
			result.put("status", "-10");
			return result;
		}

		result.put("total_fee", mount);
		result.put("orderno", order);
		result.put("channel", "WY");
		result.put("status", code);

		return result;
	}

	public String getParameter(HttpServletRequest request, String key) {
		String parameter = request.getParameter(key);
		return StringUtils.isEmpty(parameter) ? "" : parameter;
	}

	@Override
	public Map<String, Object> getOrder(String channelOrderNumber) {
		return null;
	}

	/**
	 * 结算调用第三方
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> settleOrder(MerchantSettleOrder settleOrder) {

		String basePath = "http://39.108.126.141/cash/unifiedorder";

		Map<String, String> sParam = Maps.newHashMap();

		// appid
		String appid = "152245082772849808";
		sParam.put("appid", appid);

		// 支付类型
		String paytype = "200010";
		sParam.put("paytype", paytype);

		// 异步通知回调
		String notify_url = "http://47.104.181.26/getway/settleorder";
		sParam.put("notify_url", notify_url);

		// 支付金额
		String total_amount = settleOrder.getPostalAmount().subtract(settleOrder.getServiceAmount()).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString();
		sParam.put("total_amount", total_amount);

		// 订单编号
		String out_trade_no = settleOrder.getOrderNumber();
		sParam.put("out_trade_no", out_trade_no);

		// 银行卡号
		String bankno = settleOrder.getMerchantBindBank();
		sParam.put("bankno", bankno);

		// 开户名
		String bankusername = settleOrder.getAccountName();
		sParam.put("bankusername", bankusername);

		// 对公
		String ispublic = "PRIVATE";
		sParam.put("ispublic", ispublic);

		// 银行编号
		String sendbankcode = settleOrder.getBankCode();
		sParam.put("sendbankcode", sendbankcode);

		// 充值账户：100000，支付宝：100010
		String cashtype = "100050";
		sParam.put("cashtype", cashtype);

		// 省
		String province = settleOrder.getAccountProvice();
		sParam.put("province", province);

		// 城市
		String city = settleOrder.getAccountCity();
		sParam.put("city", city);

		// 银行全称
		String bankaddress = settleOrder.getBranchBankName();
		sParam.put("bankaddress", bankaddress);

		// 手机号（ 乱写的）
		String phone = "13800138000";
		sParam.put("phone", phone);

		// 获取签名
		String sign = getSign(sParam);

		sParam.put("sign_type", "MD5");

		sParam.put("sign", sign);

		HttpRequestDetailVo httpGet = HttpClientUtil.httpPost(basePath, null, sParam);

		String resultAsString = httpGet.getResultAsString();

		Map result = (Map) JSONUtils.parse(resultAsString);

		String status = result.get("bxcode").toString();

		if ("200".equals(status)) {
			result.put("status", "1");
		} else{
			result.put("status", "0");
		} 

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> placeOrder(MerchantPlaceOrder settleOrder) {
		String basePath = "http://39.108.126.141/cash/unifiedorder";

		Map<String, String> sParam = Maps.newHashMap();

		// appid
		String appid = "152245082772849808";
		sParam.put("appid", appid);

		// 支付类型
		String paytype = "200010";
		sParam.put("paytype", paytype);

		// 异步通知回调
		String notify_url = "www.xxx.com";
		sParam.put("notify_url", notify_url);

		// 支付金额
		String total_amount = settleOrder.getPayAmount().subtract(settleOrder.getServiceAmount()).setScale(2,BigDecimal.ROUND_HALF_DOWN).toString();
		sParam.put("total_amount", total_amount);

		// 订单编号
		String out_trade_no = settleOrder.getOrderNumber();
		sParam.put("out_trade_no", out_trade_no);

		// 银行卡号
		String bankno = settleOrder.getBankNumber();
		sParam.put("bankno", bankno);

		// 开户名
		String bankusername = settleOrder.getAccountName();
		sParam.put("bankusername", bankusername);

		// 对公
		String ispublic = "PRIVATE";
		sParam.put("ispublic", ispublic);

		// 银行编号
		String sendbankcode = settleOrder.getBankCode();
		sParam.put("sendbankcode", sendbankcode);

		// 充值账户：100000，支付宝：100010
		String cashtype = "100050";
		sParam.put("cashtype", cashtype);

		// 省
		String province = settleOrder.getAccountProvice();
		sParam.put("province", province);

		// 城市
		String city = settleOrder.getAccountCity();
		sParam.put("city", city);

		// 银行全称
		String bankaddress = settleOrder.getBranchBankName();
		sParam.put("bankaddress", bankaddress);

		// 手机号（ 乱写的）
		String phone = "13800138000";
		sParam.put("phone", phone);

		// 获取签名
		String sign = getSign(sParam);

		sParam.put("sign_type", "MD5");

		sParam.put("sign", sign);

		HttpRequestDetailVo httpGet = HttpClientUtil.httpPost(basePath, null, sParam);

		String resultAsString = httpGet.getResultAsString();

		Map result = (Map) JSONUtils.parse(resultAsString);

		String status = result.get("bxcode").toString();

		if ("200".equals(status)) {
			result.put("status", "1");
		} else {
			result.put("status", "0");
		}

		return result;
	}

	/**
	 * 生成签名
	 * 
	 * @param map
	 * @return
	 */
	public static String getSign(Map<String, String> map) {

		String result = "";
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});

			// 构造签名键值对的格式
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds) {
				if (item.getKey() != null || item.getKey() != "") {
					String key = item.getKey();
					String val = item.getValue();
					if (!(val == "" || val == null)) {
						sb.append(key + "=" + val + "&");
					}
				}

			}
//			sb.append(PropertyManager.getProperty("SIGNKEY"));
			result = sb.toString().substring(0, sb.toString().length() - 1);
			result += "&key=" + "1ea134cfe8bdd441b22460053e9ed5af";

			// 进行MD5加密
			result = DigestUtils.md5Hex(result).toLowerCase();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> taskPayOrderNumber(String orderNumber) {

		String baseUrl = "http://39.108.126.141/querybill/refund";
		
		Map<String,String> sParam = Maps.newHashMap();
		
		String appid = "152245082772849808";
		sParam.put("appid", appid);
		
		String out_trade_no = orderNumber;
		sParam.put("out_trade_no", out_trade_no);
		
		String sign = getSign(sParam);
		
		sParam.put("sign", sign);
		
		sParam.put("sign_type", "MD5");
		
		HttpRequestDetailVo httpGet = HttpClientUtil.httpPost(baseUrl,null,sParam);

		String resultAsString = httpGet.getResultAsString();
		
		if(StringUtils.isEmpty(resultAsString))
		{
			Map result = Maps.newHashMap();
			
			result.put("status", "0");
			return result;
		}
		
		Map result = (Map) JSONUtils.parse(httpGet.getResultAsString());
		
		String bxcode = result.get("bxcode").toString();
		
		if("200".equals(bxcode))   // 成功
		{
			String bxstatus = result.get("bxstatus").toString();
			if("SUCCESS".equals(bxstatus))
			{
				result.put("status", "1");
			}
		}
		else if("400".equals(bxcode))  // 待支付
		{
			result.put("status", "2");
		}
		else if("401".equals(bxcode))  // 失败
		{
			result.put("status", "0");
		}
		

		return result;
		
		
		
	}

}
