package com.polypay.platform.pay.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.polypay.platform.pay.util.ParamUtil;

/**
 * 通知
 * @author psl
 * @date 2015-12-9
 */
public class NotifyServlet extends HttpServlet{
	private static final Logger logger = Logger.getLogger(NotifyServlet.class);
	
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
			logger.info("=======================================================================");
			logger.info("接收到通知-->" + transMap);
			logger.info("=======================================================================");
			//System.out.println((new StringBuilder()).append(new Date()).append("==================get trans param:").append(transMap).toString());

		}catch(Exception e){
			e.printStackTrace();
			msg = "订单报文处理失败";
		}finally{
			response.getWriter().print("YYYYYY");
		}
	}
	
	
}
