package com.polypay.platform.paychannel;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.pay.util.DateUtil;
import com.polypay.platform.pay.util.ParamUtil;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.utils.MD5;

/**
 * 
 * 快捷支付
 * 
 * @author Administrator
 *
 */
public class kJpayChannel implements IPayChannel {

	/**
	 * 
	 * 
	 * version merchantNo pickupUrl receiveUrl signType orderTime orderNo
	 * orderAmount orderCurrency customerId accNo priv transType sign
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void sendRedirect(Map<String, Object> param, HttpServletResponse response, HttpServletRequest request) {

		response.setCharacterEncoding("utf-8");
		String redirectPath = "/send.jsp";
		String msg = "处理失败，请重试";
		try {
			request.setCharacterEncoding("utf-8");
			// 利用treeMap对参数按key值进行排序
			Map<String, String> transMap = Maps.newTreeMap();
			System.out.println("get trans param:" + transMap);

			// 支付url
			String payUrl = "http://gateway.fulianga.cn/ftpay-gateway-web/trans/qcdebit.htm";

//			version 		
//			merchantNo 		
//			pickupUrl 		
//			receiveUrl 		
//			signType 		
//			orderTime 		
//			orderNo 		
//			orderAmount 	
//			orderCurrency 	
//			customerId 		
//			accNo			
//			priv 			
//			transType 		
//			sign 

			String version = "1.0.0";
//			transMap.put("version", version);

			String merchantNo = "NB20190118005";
			transMap.put("merchantNo", merchantNo);

			String pickupUrl = URLEncoder.encode("www.xxx.com");
			transMap.put("pickupUrl", pickupUrl);

			String receiveUrl = URLEncoder.encode(param.get("call_back").toString());
			transMap.put("receiveUrl", receiveUrl);

			String signType = "MD5";
			transMap.put("signType", signType);

			String orderTime = DateUtil.getDate() + DateUtil.getTime();
			transMap.put("orderTime", orderTime);

			String orderNo = param.get("we_order_number").toString();
			transMap.put("orderNo", orderNo);

			String orderAmount = param.get("pay_amount").toString();
			transMap.put("orderAmount", orderAmount);

			String orderCurrency = "CNY";
			transMap.put("orderCurrency", orderCurrency);

			String customerId = "";
//			transMap.put("customerId", customerId);

			String accNo = param.get("bank_no").toString();
			transMap.put("accNo", accNo);

			String priv = "1";
			transMap.put("priv", priv);

			String transType = "2002";
			transMap.put("transType", transType);

			String signKey = "1BF9CBD5389F36F55A7C3F0FCE8D8F84";
			String signMsg = ParamUtil.getSignMsg(transMap);
			signMsg += "&signKey=" + signKey;

			String sign = MD5.md5(signMsg).toUpperCase();

			transMap.put("version", version);
			transMap.put("customerId", customerId);
//			transMap.put("priv", priv);
			transMap.put("signKey", signKey);

			transMap.put("pickupUrl", "www.xxx.com");
			transMap.put("receiveUrl", param.get("call_back").toString());

			transMap.put("sign", sign);
			request.setAttribute("action", payUrl);
			request.setAttribute("dataMap", transMap);

		} catch (Exception e) {
			e.printStackTrace();
			redirectPath = "/WEB-INF/jsp/result.jsp";
			msg = "订单报文处理失败";
		} finally {
			request.setAttribute("errorMsg", msg);
			try {
				request.getRequestDispatcher(redirectPath).forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public Map<String, Object> checkOrder(HttpServletRequest request) {

//		version 		
//		cmdType 		
//		signType 		
//		merchantNo		
//		transType 		
//		orderNo 		
//		orderTime 		
//		orderAmount		
//		orderFee 		
//		orderCurrency	
//		transactionId 	
//		transactionTime	
//		payeeName 		
//		payeeAcctNo 	
//		respCode 		
//		respMsg 		
//		customerId 		
//		priv 			
//		status 			
//		processing		
//		sign 

		/**
		 * 
		 * cmdType=00&merchantNo=test&orderAmount=3500.00&orderCurrency=CNY
		 * &orderFee=28.00&orderNo=a93b8654d7d
		 * 84c92ad0fd636c4d4a68b&respCode=000000&signType=MD5&status=success&transType=IN_PAY&transactionId=2018071826672&
		 * transactionTime=20180718170620&signKey=md5key
		 */

		Map<String, String> signP = Maps.newTreeMap();

		String cmdType = getParameter(request, "cmdType");
		signP.put("cmdType", cmdType);

		String merchantNo = getParameter(request, "merchantNo");
		signP.put("merchantNo", merchantNo);

		String orderAmount = getParameter(request, "orderAmount");
		signP.put("orderAmount", orderAmount);

		String orderCurrency = getParameter(request, "orderCurrency");
		signP.put("orderCurrency", orderCurrency);

		String orderFee = getParameter(request, "orderFee");
		signP.put("orderFee", orderFee);

		String orderNo = getParameter(request, "orderNo");
		signP.put("orderNo", orderNo);

		String respCode = getParameter(request, "respCode");
		signP.put("respCode", respCode);

		String signType = getParameter(request, "signType");
		signP.put("signType", signType);

		String status = getParameter(request, "status");
		signP.put("status", status);

		String transType = getParameter(request, "transType");
		signP.put("transType", transType);

		String transactionId = getParameter(request, "transactionId");
		signP.put("transactionId", transactionId);

		String transactionTime = getParameter(request, "transactionTime");
		signP.put("transactionTime", transactionTime);

		// 签名
		String sign = getParameter(request, "sign");

		String sign2 = getSign(signP);

		Map<String, Object> result = Maps.newHashMap();

		if (!sign2.equals(sign)) {
			result.put("status", "-10");
			return result;
		}

		/**
		 * 
		 * success: 支 付 成 功 ,fail: 支 付 失 败 , processing,处理中
		 */

		if ("success".equals(status)) {
			result.put("status", "1");
		} else if ("fail".equals(status)) {
			result.put("status", "-1");
		}
		result.put("total_fee", orderAmount);
		result.put("orderno", orderNo);
		result.put("channel", "WY");

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

		String basePath = "http://gateway.futonglink.com/ftpay-gateway-web/trans/ftdfpay.htm";

		/**
		 * 
		 * version receiveUrl signType merchantNo orderTime orderNo orderAmount
		 * orderCurrency payeeName payeeAcctNo payeePhone bankCode bankLinkNumber
		 * bankProvince bankCity customerId priv sign idNo transType
		 * 
		 */

		/**
		 * 
		 * bankCity=%E9%BE%99%E5%B2%A9&
		 * bankCode=01050000&
		 * bankLinkNumber=0000&
		 * bankProvince=%E7%A6%8F%E5%BB%BA&
		 * merchantNo=100010004001
		 * &orderAmount=100&
		 * orderCurrency=CNY&
		 * orderNo=HXOUT20180717131647177&
		 * orderTime=20180717131647&
		 * payeeAcctNo=6227001882870185470&
		 * payeeName=%E8%B0%A2%E5%8F%91%E8%8D%A3&
		 * receiveUrl=http%3A%2F%2Foption.wode99.com%2FApi%2FTTrade%2FPayrollSuccess&
		 * signType=MD5&
		 * transType=T1&
		 * signKey=md5key
		 */

		Map<String, String> sParam = Maps.newTreeMap();

		String bankCity = URLEncoder.encode(settleOrder.getAccountCity());
		sParam.put("bankCity", bankCity);
		
		String bankCode = "01020000";
		sParam.put("bankCode", bankCode);
		
		String bankLinkNumber = settleOrder.getBankNo();
		sParam.put("bankLinkNumber", bankLinkNumber);
		
		String bankProvince = URLEncoder.encode(settleOrder.getAccountProvice());
		sParam.put("bankProvince", bankProvince);
		
		String merchantNo = "NB20190118005";
		sParam.put("merchantNo", merchantNo);
		
		String orderAmount = settleOrder.getPostalAmount().subtract(settleOrder.getServiceAmount()).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
		sParam.put("orderAmount", orderAmount);
		
		String orderCurrency = "CNY";
		sParam.put("orderCurrency", orderCurrency);
		
		String orderNo = settleOrder.getOrderNumber();
		sParam.put("orderNo", orderNo);
		
		String orderTime = DateUtil.getDate() + DateUtil.getTime();
		sParam.put("orderTime", orderTime);
		
		String payeeName = URLEncoder.encode(settleOrder.getAccountName());
		sParam.put("payeeName", payeeName);
		
		String payeeAcctNo = settleOrder.getMerchantBindBank();
		sParam.put("payeeAcctNo", payeeAcctNo);
		
		String receiveUrl = URLEncoder.encode("www.ysfpolypay.cn/getway/kjpay/callback");
		sParam.put("receiveUrl", receiveUrl);
		
		String signType = "MD5";
		sParam.put("signType", signType);
		
		String transType = "D0";
		sParam.put("transType", transType);
		
		String priv = URLEncoder.encode("1");
		sParam.put("priv", priv);

		// 获取签名
		String sign = getSign(sParam);

		sParam.put("sign", sign);
		sParam.put("version", "1.0.0");
		sParam.put("payeePhone", "13800138000");
		sParam.put("customerId", "123");
		sParam.put("idNo", "320926195511175276");
		
		sParam.put("payeeName", settleOrder.getAccountName());
		sParam.put("bankCity", settleOrder.getAccountCity());
		sParam.put("bankProvince", settleOrder.getAccountProvice());
		sParam.put("payeeName", settleOrder.getAccountName());
		sParam.put("receiveUrl", "www.ysfpolypay.cn/getway/kjpay/callback");
		sParam.put("priv", "1");
		

		HttpRequestDetailVo httpGet = HttpClientUtil.httpPost(basePath, null, sParam);

		String resultAsString = httpGet.getResultAsString();

		Map result = (Map) JSONUtils.parse(resultAsString);
		
		/**
		 * 
		 * customerId=00b3b7f4-d655-4301-bc3e-8672c028f5f3&
		 * merchantNo=300010007001&
		 * orderAmount=1500.00&
		 * orderCurrency=CNY&
			orderFee=3.00&
			orderNo=2018071816161873&
			orderTime=20180718170110&
			payeeAcctNo=622208XXXX02578696&
			payeeName=%E5%88%98%E4%BC%E6%B9%96&
			respCode=000000&
			signType=MD5&
			transType=OUT_PAY&
			transactionId=2018071826666&
			transactionTime=20180718170057&
			signKey=md5key

		 */
		String status = result.get("respCode").toString();

		if ("000000".equals(status)) {
			result.put("status", "1");
		} else {
			result.put("status", "0");
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> placeOrder(MerchantPlaceOrder placeOrder) {

		String basePath = "http://gateway.futonglink.com/ftpay-gateway-web/trans/ftdfpay.htm";

		/**
		 * 
		 * version receiveUrl signType merchantNo orderTime orderNo orderAmount
		 * orderCurrency payeeName payeeAcctNo payeePhone bankCode bankLinkNumber
		 * bankProvince bankCity customerId priv sign idNo transType
		 * 
		 */

		/**
		 * 
		 * bankCity=%E9%BE%99%E5%B2%A9&
		 * bankCode=01050000&
		 * bankLinkNumber=0000&
		 * bankProvince=%E7%A6%8F%E5%BB%BA&
		 * merchantNo=100010004001
		 * &orderAmount=100&
		 * orderCurrency=CNY&
		 * orderNo=HXOUT20180717131647177&
		 * orderTime=20180717131647&
		 * payeeAcctNo=6227001882870185470&
		 * payeeName=%E8%B0%A2%E5%8F%91%E8%8D%A3&
		 * receiveUrl=http%3A%2F%2Foption.wode99.com%2FApi%2FTTrade%2FPayrollSuccess&
		 * signType=MD5&
		 * transType=T1&
		 * signKey=md5key
		 */

		Map<String, String> sParam = Maps.newTreeMap();

		String bankCity = URLEncoder.encode(placeOrder.getAccountCity());
		sParam.put("bankCity", bankCity);
		
		String bankCode = "01020000";
		sParam.put("bankCode", bankCode);
		
		String bankLinkNumber = placeOrder.getBankNo();
		sParam.put("bankLinkNumber", bankLinkNumber);
		
		String bankProvince = URLEncoder.encode(placeOrder.getAccountProvice());
		sParam.put("bankProvince", bankProvince);
		
		String merchantNo = "NB20190118005";
		sParam.put("merchantNo", merchantNo);
		
		String orderAmount = placeOrder.getPayAmount().subtract(placeOrder.getServiceAmount()).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
		sParam.put("orderAmount", orderAmount);
		
		String orderCurrency = "CNY";
		sParam.put("orderCurrency", orderCurrency);
		
		String orderNo = placeOrder.getOrderNumber();
		sParam.put("orderNo", orderNo);
		
		String orderTime = DateUtil.getDate() + DateUtil.getTime();
		sParam.put("orderTime", orderTime);
		
		String payeeName = URLEncoder.encode(placeOrder.getAccountName());
		sParam.put("payeeName", payeeName);
		
		String payeeAcctNo = placeOrder.getBankNumber();
		sParam.put("payeeAcctNo", payeeAcctNo);
		
		String receiveUrl = URLEncoder.encode("www.ysfpolypay.cn/getway/kjpay/callback");
		sParam.put("receiveUrl", receiveUrl);
		
		String signType = "DM5";
		sParam.put("signType", signType);
		
		String transType = "D0";
		sParam.put("transType", transType);
		
		String priv = URLEncoder.encode("1");
		sParam.put("priv", priv);

		// 获取签名
		String sign = getSign(sParam);

		sParam.put("sign", sign);
		sParam.put("version", "1.0.0");
		sParam.put("payeePhone", "13800138000");
		sParam.put("customerId", "123");
		sParam.put("idNo", "320926195511175276");
		
		sParam.put("payeeName", placeOrder.getAccountName());
		sParam.put("bankCity", placeOrder.getAccountCity());
		sParam.put("bankProvince", placeOrder.getAccountProvice());
		sParam.put("payeeName", placeOrder.getAccountName());
		sParam.put("receiveUrl", "www.ysfpolypay.cn/getway/kjpay/callback");
		sParam.put("priv", "1");
		

		HttpRequestDetailVo httpGet = HttpClientUtil.httpPost(basePath, null, sParam);

		String resultAsString = httpGet.getResultAsString();

		Map result = (Map) JSONUtils.parse(resultAsString);
		
		/**
		 * 
		 * customerId=00b3b7f4-d655-4301-bc3e-8672c028f5f3&
		 * merchantNo=300010007001&
		 * orderAmount=1500.00&
		 * orderCurrency=CNY&
			orderFee=3.00&
			orderNo=2018071816161873&
			orderTime=20180718170110&
			payeeAcctNo=622208XXXX02578696&
			payeeName=%E5%88%98%E4%BC%E6%B9%96&
			respCode=000000&
			signType=MD5&
			transType=OUT_PAY&
			transactionId=2018071826666&
			transactionTime=20180718170057&
			signKey=md5key

		 */
		String status = result.get("respCode").toString();

		if ("000000".equals(status)) {
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
			result += "&signKey=" + "1BF9CBD5389F36F55A7C3F0FCE8D8F84";

			// 进行MD5加密
			result = DigestUtils.md5Hex(result).toUpperCase();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	@Override
	public Map<String, Object> taskPayOrderNumber(String orderNumber, Date date) {
		return null;

	}

}
