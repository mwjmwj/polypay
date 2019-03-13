package com.polypay.platform.pay.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.polypay.platform.pay.common.Config;
import com.polypay.platform.pay.secret.CertUtil;
import com.polypay.platform.pay.util.DateUtil;
import com.polypay.platform.pay.util.ParamUtil;

/**
 * 支付
 * @author psl
 * @date 2015-12-9
 */
public class PayServlet extends HttpServlet{

	private static final Logger logger = Logger.getLogger(PayServlet.class);
	
	@SuppressWarnings("unchecked")
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String redirectPath = "send.jsp";
		String msg = "处理失败，请重试";
		
		List<String> keys = new ArrayList<String>();
		try{
			//利用treeMap对参数按key值进行排序
			Map<String, String> transMap = ParamUtil.getParamMap(request);
			System.out.println("get trans param:" + transMap);
			
			String payUrl = "https://cashier.61moke.com/paygate/v1/web/pay";
			String version = "v1";
			String merchantNo =Config.getInstance().getMerchantNo();
			String channelNo = Config.getInstance().getChannelNo();
			String tranTime = DateUtil.getDate() + DateUtil.getTime();
			String currency = "CNY";
			
			
			// 交易流水
			String tranSerialNum = "1";
			
			// 银行编码
			String bankId = "03080000";
			
			// 卡类型
			String cardType= "01";
			
			String amount  = "10.00";
			
			String bizType = "08";
			
			String goodsName = "手机";
			
			String goodsInfo = "";
			
			String goodsNum = "";
			
			String notifyUrl = "www.xxx.com";
			
			String returnUrl = "www.xxx.com";
			
			String buyerName = "张三";
			
			String buyerId  = "12";
			
			String contact = "18880808080";
			
			String valid = "";
			
			String ip= "127.0.0.1";
			
			String referer = "";
			
			String remark = "";
			
			String YUL1 = "";
			//
			
			//组织交易报文
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
			
			
			//敏感信息加密
			transMap.put("buyerName", CertUtil.getInstance().encrypt(buyerName));
			transMap.put("contact", CertUtil.getInstance().encrypt(contact));
			
			
			//组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			//签名
			String sign = CertUtil.getInstance().sign(signMsg);
			//将签名放入交易map中
			transMap.put("sign", sign);
			
			if(logger.isDebugEnabled()){
				logger.debug("发送支付请求：" + transMap);
			}
			
			request.setAttribute("action", payUrl);
			request.setAttribute("dataMap", transMap);
		}catch(Exception e){
			e.printStackTrace();
			redirectPath = "result.jsp";
			msg = "订单报文处理失败";
		}finally{
			request.setAttribute("errorMsg", msg);
			request.getRequestDispatcher(redirectPath).forward(request, response);
		}
	}
	
}
