package com.polypay.platform.paychannel;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import com.hfb.merchant.df.model.DfPay;
import com.hfb.merchant.df.model.QueryResult;
import com.hfb.merchant.df.sercret.CertUtil;
import com.hfb.merchant.df.util.ModelPayUtil;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.pay.common.Config;
import com.polypay.platform.pay.util.DateUtil;
import com.polypay.platform.pay.util.ParamUtil;

public class HFBPayChannel implements IPayChannel {

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
			String payUrl = "https://cashier.61moke.cn/paygate/v1/web/pay";

			String version = "v1";
			String merchantNo = Config.getInstance().getMerchantNo();
			String channelNo = Config.getInstance().getChannelNo();
			String tranTime = DateUtil.getDate() + DateUtil.getTime();
			String currency = "CNY";

			// 交易流水
			Object order_number = param.get("we_order_number");
			String tranSerialNum = order_number.toString();

			// 银行编码
			String bankId = "01020000";

			// 卡类型
			String cardType = "01";

			// 以分为单位计算
			Object pay_amount = param.get("pay_amount");
			Integer total_amount = new BigDecimal(pay_amount.toString()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(new BigDecimal(100)).intValue();

			String amount = total_amount.toString();

			String bizType = "08";

			String goodsName = "手机";

			String goodsInfo = "";

			String goodsNum = "";

			String notifyUrl = param.get("call_back").toString();

			String returnUrl = "www.xxx.com";

			String buyerName = "张三";

			String buyerId = "12";

			String contact = "18880808080";

			String valid = "";

			String ip = "127.0.0.1";

			String referer = "";

			String remark = "";

			// 银行卡号
			String YUL1 = param.get("bank_no").toString();
			//

			// 组织交易报文
			transMap.put("merchantNo", merchantNo);
//			keys.add(merchantNo);
			transMap.put("version", version);
			transMap.put("channelNo", channelNo);
			transMap.put("tranTime", tranTime);
			transMap.put("notifyUrl", transMap.get("notifyUrl"));
			transMap.put("returnUrl", transMap.get("returnUrl"));
			transMap.put("currency", currency);

			transMap.put("tranSerialNum", tranSerialNum);

			transMap.put("bankId", bankId);
			transMap.put("cardType", cardType);
			transMap.put("amount", amount);
			transMap.put("bizType", bizType);
			transMap.put("goodsName", goodsName);
			transMap.put("goodsInfo", goodsInfo);
			transMap.put("goodsNum", goodsNum);
			transMap.put("notifyUrl", notifyUrl);
			transMap.put("returnUrl", returnUrl);
			transMap.put("buyerId", buyerId);
			transMap.put("valid", valid);
			transMap.put("ip", ip);
			transMap.put("referer", referer);
			transMap.put("remark", remark);
			transMap.put("YUL1", YUL1);

			// 敏感信息加密
			transMap.put("buyerName", com.polypay.platform.pay.secret.CertUtil.getInstance().encrypt(buyerName));
			transMap.put("contact", com.polypay.platform.pay.secret.CertUtil.getInstance().encrypt(contact));

			// 组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			// 签名
			String sign = com.polypay.platform.pay.secret.CertUtil.getInstance().sign(signMsg);
			// 将签名放入交易map中
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

	/**
	 * 
	 * 回调参数 merchantNo tranSerialNum paySerialNo amount rtnCode rtnMsg settDate
	 * remark YUL1 sign
	 */
	@Override
	public Map<String, Object> checkOrder(HttpServletRequest request) {

		Map<String, String> signP = Maps.newTreeMap();

		// 商户号（上游）
		String merchantNo = getParameter(request, "merchantNo");
		signP.put("merchantNo", merchantNo);

		// 订单号
		String tranSerialNum = getParameter(request, "tranSerialNum");
		signP.put("tranSerialNum", tranSerialNum);
		
		//1
		String channelNo = getParameter(request, "channelNo");
//		signP.put("channelNo", channelNo);

		// 平台订单号
		String paySerialNo = getParameter(request, "paySerialNo");
		signP.put("paySerialNo", paySerialNo);

		// 金额
		String amount = getParameter(request, "amount");
		signP.put("amount", amount);
		
		//2
		String tranCode = getParameter(request, "tranCode");
//		signP.put("tranCode", tranCode);
		
		//3
		String version = getParameter(request, "version");
//		signP.put("version", version);

		// 0000 成功 其他失败
		// 状态码
		String rtnCode = getParameter(request, "rtnCode");
		signP.put("rtnCode", rtnCode);

		// 消息
		String rtnMsg = getParameter(request, "rtnMsg");
		signP.put("rtnMsg", rtnMsg);

		// 时间
		String settDate = getParameter(request, "settDate");
		signP.put("settDate", settDate);

		// 备注
		String remark = getParameter(request, "remark");
		signP.put("remark", remark);

		// 预留字段 （入参为 银行卡号）
		String YUL1 = getParameter(request, "YUL1");
		signP.put("YUL1", YUL1);

		// 签名
		String sign = getParameter(request, "sign");

		Map<String, Object> result = Maps.newTreeMap();

		// 组织签名字符串
		String signMsg = ParamUtil.getSignMsg(signP);

		// 签名
		
		boolean verify = false;
		try {
			verify = com.polypay.platform.pay.secret.CertUtil.getInstance().verify(signMsg,sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!verify) {
			result.put("status", "-10");
			return result;
		}

		result.put("total_fee", new BigDecimal(amount).divide(new BigDecimal(100)));
		result.put("orderno", tranSerialNum);
		result.put("channel", "WY");

		// 0000 success
		result.put("status", rtnCode);

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
	 * 
	 * merchantNo	
		version 	
		channelNo 	
		tranCode 	
		tranFlow 	
		tranDate 	
		tranTime 	
		accNo 		
		accName 	
		 bankAgentId
		currency 	
		bankName 	
		amount 		
		remark 		
		 ext1		
		ext2 		
		YUL1 		
		YUL2 		
		 YUL3 		
		NOTICEURL 	

	 */

	@Override
	public Map<String, Object> settleOrder(MerchantSettleOrder settleOrder) {

		// 商户号
		String merchantNo = "S20190311046026";

		// 测试商户的公钥私钥这两个文件在本项目的src目录下certs中

		// 私钥文件路径
		String privateKey = HFBPayChannel.class.getResource("/").getPath() + "CS20190311046026_20190311175715836.pfx";
		// 公钥文件路径
		String publicKey = HFBPayChannel.class.getResource("/").getPath() + "SS20190311046026_20190311175715836.cer";
		// 密钥密码
		String KeyPass = "666888";

		// 交易流水号
		String tranFlow = settleOrder.getOrderNumber();
		// 交易日期
		String tranDate = DateUtil.getDate();
		// 交易时间
		String tranTime = DateUtil.getTime();
		// 收款账号
		String accNo = settleOrder.getMerchantBindBank();
		// 收款账户名
		String accName = settleOrder.getAccountName();
		// 账户联行号
		String bankAgentId = settleOrder.getBankNo();
		// 收款行名称
		String bankName = settleOrder.getBankName();
		
		// 交易金额
		String amount = settleOrder.getPostalAmount().subtract(settleOrder.getServiceAmount()).multiply(new BigDecimal(100))
				.setScale(0, BigDecimal.ROUND_HALF_DOWN).toString();
		// 摘要
		String remark = "代付";
		// 扩展字段
		String ext1 = "1";
		// 扩展字段
		String ext2 = "2";
		// 预留字段
		String yUL1 = "1";
		// 预留字段
		String yUL2 = "2";
		// 预留字段
		String yUL3 = "3";
		// 后台通知地址
		String NOTICEURL = "https://demo.zhuozhoups.com/paydemo-1671/notify.do";

		/**
		 * 切换正式环境商户号需要到正式环境商户后台 安全中心--证书管理 功能中下载正式并启用商户证书秘钥替换掉DEMO中的证书秘钥 进入证书管理页面下载证步骤
		 * 1、点击 生成授权码按钮 生成授权码并复制 2、点击 授权码后的生成新证书按钮跳转到证书生成页面
		 * 3、输入上一步操作的授权码、商户号、密钥(证书生成页面密钥及为私钥证书密码)等相关信息 4、点击提交可下载公私钥证书
		 * 5、下载完证书后回到证书管理列表，在列表上查看刚才下载的证书 6、点击启用操作 启用刚才下载的证书
		 */
		// 加密工具类的创建
		CertUtil certUtil = new CertUtil(publicKey, privateKey, KeyPass, true);
		String status = null;
		try {
			Map<String, String> map;
			// 对数据进行封装
			DfPay dfPay = new DfPay(merchantNo, tranFlow, tranDate, tranTime, accNo, accName, bankAgentId, bankName,
					amount, remark, ext1, ext2, yUL1, yUL2, yUL3, NOTICEURL);

			// 对发送的信息，进行加密，加签，发送至合付宝平台，并对返回的信息内容进行解析，验签操作

			map = ModelPayUtil.sendModelPay(certUtil, dfPay, "https://cashier.61moke.cn/paygate/v1/dfpay");

			System.out.println(map);

			status = map.get("rtnCode");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> resultM = Maps.newHashMap();

		// 处理中或者成功不用管 其他失败直接回滚订单
		if ("0000".equals(status) || "0002".equals(status)) {
			resultM.put("status", "1");
		} else {
			resultM.put("status", "0");
		}

		return resultM;
	}

	@Override
	public Map<String, Object> placeOrder(MerchantPlaceOrder placeOrder) {
		// 商户号
		String merchantNo = "S20190311046026";

		// 测试商户的公钥私钥这两个文件在本项目的src目录下certs中

		// 私钥文件路径
		String privateKey = HFBPayChannel.class.getResource("/").getPath() + "CS20190311046026_20190311175715836.pfx";
		// 公钥文件路径
		String publicKey = HFBPayChannel.class.getResource("/").getPath() + "SS20190311046026_20190311175715836.cer";
		// 密钥密码
		String KeyPass = "666888";

		// 交易流水号
		String tranFlow = placeOrder.getOrderNumber();
		// 交易日期
		String tranDate = DateUtil.getDate();
		// 交易时间
		String tranTime = DateUtil.getTime();
		// 收款账号
		String accNo = placeOrder.getBankNumber();
		// 收款账户名
		String accName = placeOrder.getAccountName();
		// 账户联行号
		String bankAgentId = placeOrder.getBankNo();
		// 收款行名称
		String bankName = placeOrder.getBankName();
		// 交易金额
		String amount = placeOrder.getPayAmount().subtract(placeOrder.getServiceAmount()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_DOWN)
				.toString();
		// 摘要
		String remark = "代付";
		// 扩展字段
		String ext1 = "1";
		// 扩展字段
		String ext2 = "2";
		// 预留字段
		String yUL1 = "1";
		// 预留字段
		String yUL2 = "2";
		// 预留字段
		String yUL3 = "3";
		// 后台通知地址
		String NOTICEURL = "https://demo.zhuozhoups.com/paydemo-1671/notify.do";

		/**
		 * 切换正式环境商户号需要到正式环境商户后台 安全中心--证书管理 功能中下载正式并启用商户证书秘钥替换掉DEMO中的证书秘钥 进入证书管理页面下载证步骤
		 * 1、点击 生成授权码按钮 生成授权码并复制 2、点击 授权码后的生成新证书按钮跳转到证书生成页面
		 * 3、输入上一步操作的授权码、商户号、密钥(证书生成页面密钥及为私钥证书密码)等相关信息 4、点击提交可下载公私钥证书
		 * 5、下载完证书后回到证书管理列表，在列表上查看刚才下载的证书 6、点击启用操作 启用刚才下载的证书
		 */
		// 加密工具类的创建
		CertUtil certUtil = new CertUtil(publicKey, privateKey, KeyPass, true);
		String status = null;
		try {
			Map<String, String> map;
			// 对数据进行封装
			DfPay dfPay = new DfPay(merchantNo, tranFlow, tranDate, tranTime, accNo, accName, bankAgentId, bankName,
					amount, remark, ext1, ext2, yUL1, yUL2, yUL3, NOTICEURL);

			// 对发送的信息，进行加密，加签，发送至合付宝平台，并对返回的信息内容进行解析，验签操作

			map = ModelPayUtil.sendModelPay(certUtil, dfPay, "https://cashier.61moke.cn/paygate/v1/dfpay");

			System.out.println(map);
			status = map.get("rtnCode");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> resultM = Maps.newHashMap();

		// 处理中或者成功不用管 其他失败直接回滚订单
		if ("0000".equals(status) || "0002".equals(status)) {
			resultM.put("status", "1");
		} else {
			resultM.put("status", "0");
		}

		return resultM;
	}

	
	@Override
	public Map<String, Object> taskPayOrderNumber(String orderNumber,Date handleTime) {

		// 商户号
		String merchantNo = "S20190311046026";

		// 测试商户的公钥私钥这两个文件在本项目的src目录下certs中

		// 私钥文件路径
		String privateKey = HFBPayChannel.class.getResource("/").getPath() + "CS20190311046026_20190311175715836.pfx";
		// 公钥文件路径
		String publicKey = HFBPayChannel.class.getResource("/").getPath() + "SS20190311046026_20190311175715836.cer";
		// 密钥密码
		String KeyPass = "666888";

		// 交易流水号
		String tranFlow = orderNumber;
		// 交易日期
		String tranDate = DateUtil.getDate();
		// 交易时间
		String tranTime = DateUtil.getTime();
		// 原交易流水号
		String oriTranFlow = orderNumber;
		
		String format = new SimpleDateFormat("yyyyMMdd").format(handleTime);
		// 原交易日期
		String oriTranDate = format;

		/**
		 * 切换正式环境商户号需要到正式环境商户后台 安全中心--证书管理 功能中下载正式并启用商户证书秘钥替换掉DEMO中的证书秘钥 进入证书管理页面下载证步骤
		 * 1、点击 生成授权码按钮 生成授权码并复制 2、点击 授权码后的生成新证书按钮跳转到证书生成页面
		 * 3、输入上一步操作的授权码、商户号、密钥(证书生成页面密钥及为私钥证书密码)等相关信息 4、点击提交可下载公私钥证书
		 * 5、下载完证书后回到证书管理列表，在列表上查看刚才下载的证书 6、点击启用操作 启用刚才下载的证书
		 */
		// 加密工具类的创建
		CertUtil certUtil = new CertUtil(publicKey, privateKey, KeyPass, true);

		// 对数据进行封装
		QueryResult queryResult;
		Map<String, Object> result = Maps.newHashMap();
		try {
			queryResult = new QueryResult(merchantNo, tranFlow, tranDate, tranTime, oriTranFlow, oriTranDate);
			// 对发送的信息，进行加密，加签，发送至合付宝平台，并对返回的信息内容进行解析，验签操作
			Map<String, String> map = ModelPayUtil.sendModelPay(certUtil, queryResult,
					"https://cashier.61moke.cn/paygate/v1/dfpay");
			String rtnCode = map.get("rtnCode").toString();
			String oriRtnCode = map.get("oriRtnCode").toString();

			if ("0000".equals(rtnCode) && "0000".equals(oriRtnCode)) // 成功
			{
				result.put("status", "1");
				
			} else if ("0000".equals(rtnCode) // 订单 fail
					&& (!"0000".equals(oriRtnCode) || null != oriRtnCode || !"0030".equals(oriRtnCode)
							|| !"0002".equals(oriRtnCode) || !"0003".equals(oriRtnCode) || !"9999".equals(oriRtnCode))) // 失败
			{
				result.put("status", "0");
			} else if ("0000".equals(rtnCode) && (null == oriRtnCode || "0030".equals(oriRtnCode)
					|| "0002".equals(oriRtnCode) || "0003".equals(oriRtnCode) || "9999".equals(oriRtnCode))) // 待支付
			{
				result.put("status", "2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

}
