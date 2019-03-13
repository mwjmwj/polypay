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
import com.polypay.platform.pay.util.ParamUtil;
import com.polypay.platform.pay.util.ResponseUtil;
import com.polypay.platform.pay.util.http.Httpz;

/**
 * 查询
 * @author psl
 * @date 2015-12-9
 */
public class QueryServlet extends HttpServlet{

	private static final Logger logger = Logger.getLogger(QueryServlet.class);
	
	@SuppressWarnings("unchecked")
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String redirectPath = "result.jsp";
		String msg = "处理失败，请重试";
		
		try{
			//利用treeMap对参数按key值进行排序
			Map<String, String> transMap = ParamUtil.getParamMap(request);
			System.out.println("get trans param:" + transMap);

//			String merchantNo = transMap.get("merchantNo");
			String merchantNo = Config.getInstance().getMerchantNo();
			String version = Config.getInstance().getVersion4Pay();
			String channelNo = Config.getInstance().getChannelNo();
			
			//组织交易报文
			transMap.put("merchantNo", merchantNo);
			transMap.put("version", version);
			transMap.put("channelNo", channelNo);
			
			//组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			//签名
			String sign = CertUtil.getInstance().sign(signMsg);
			//将签名放入交易map中
			transMap.put("sign", sign);
			
			if(logger.isDebugEnabled()){
				logger.debug("发送查询请求：" + transMap);
			}
			
			msg = new Httpz().post(Config.getInstance().getOrderQueryUrl(), transMap);
			ResponseUtil.parseResponse(msg);
		}catch(Exception e){
			e.printStackTrace();
			redirectPath = "result.jsp";
			msg = "查询交易处理失败";
		}finally{
			request.setAttribute("errorMsg", msg);
			request.getRequestDispatcher(redirectPath).forward(request, response);
		}
	}
	
}
