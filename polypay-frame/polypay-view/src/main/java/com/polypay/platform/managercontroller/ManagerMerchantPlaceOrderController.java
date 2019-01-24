package com.polypay.platform.managercontroller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

@Controller
public class ManagerMerchantPlaceOrderController extends BaseController<MerchantPlaceOrderVO>{
	
	private Logger log = LoggerFactory.getLogger(ManagerMerchantPlaceOrderController.class);

	@Autowired
	private IMerchantPlaceOrderService merchantPlaceOrderService;

	@RequestMapping("/merchantmanager/place/order/list")
	@ResponseBody
	public ServiceResponse listMerchantPlaceOrder() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			 MerchantPlaceOrderVO merchantPlaceOrderVO = new MerchantPlaceOrderVO();
			PageList<MerchantPlaceOrderVO> pageList = null;
			
			
			merchantPlaceOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));
			
			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");
			
			Date[] datas;
			if(!StringUtils.isEmpty(createTime))
			{
				datas = DateUtils.changeDate(createTime);
				merchantPlaceOrderVO.setcBeginTime(datas[0]);
				merchantPlaceOrderVO.setcEndTime(datas[1]);
			}
			
			if(!StringUtils.isEmpty(successTime))
			{
				datas = DateUtils.changeDate(successTime);
				merchantPlaceOrderVO.setsBeginTime(datas[0]);
				merchantPlaceOrderVO.setsEndTime(datas[1]);
			}
			
			String status = getRequest().getParameter("status");
			
			if(!StringUtils.isEmpty(status))
			{
				merchantPlaceOrderVO.setStatus(Integer.parseInt(status));
			}
			
			pageList = merchantPlaceOrderService.listMerchantPlaceOrder(pageBounds, merchantPlaceOrderVO);
			Page<MerchantPlaceOrderVO> pageData = getPageData(pageList);
			response = ResponseUtils.buildResult(pageData);
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		} catch (Exception e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		}
		return response;

	}
	
}
