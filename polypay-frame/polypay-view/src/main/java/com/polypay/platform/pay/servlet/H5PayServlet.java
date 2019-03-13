package com.polypay.platform.pay.servlet;

import java.io.IOException;
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
public class H5PayServlet extends HttpServlet{

	private static final Logger logger = Logger.getLogger(H5PayServlet.class);
	
	@SuppressWarnings("unchecked")
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String redirectPath = "send.jsp";
		String msg = "处理失败，请重试";
		
		try{
			//利用treeMap对参数按key值进行排序
			Map<String, String> transMap = ParamUtil.getParamMap(request);
			System.out.println("get trans param:" + transMap);
			
			String payUrl = request.getParameter("url");
			String version = request.getParameter("version");
			String merchantNo = transMap.get("merchantNo");
			//String merchantNo = Config.getInstance().getMerchantNo();
			String channelNo = Config.getInstance().getH5ChannelNo();
			String tranTime = DateUtil.getDate() + DateUtil.getTime();
			String currency = "CNY";
			String cardNum = transMap.get("cardNum");
			String buyerName = transMap.get("buyerName");
			String contact = transMap.get("contact");
			
			//组织交易报文
			transMap.put("merchantNo", merchantNo);
			transMap.put("version", version);
			transMap.put("channelNo", channelNo);
//			transMap.put("tranTime", tranTime);
			transMap.put("tranTime", "20171214131432");
			transMap.put("notifyUrl", transMap.get("notifyUrl"));
			transMap.put("returnUrl", transMap.get("returnUrl"));
			transMap.put("currency", currency);
			
			//敏感信息加密
			//transMap.put("cardNum", CertUtil.getInstance().encrypt(cardNum));
			transMap.put("buyerName", CertUtil.getInstance().encrypt(buyerName));
			transMap.put("contact", CertUtil.getInstance().encrypt(contact));
			
			
			//组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			logger.info("签名源串：" + signMsg);
			
			//签名
			String sign = CertUtil.getInstance().sign(signMsg);
			logger.info("签名：" + sign);
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
