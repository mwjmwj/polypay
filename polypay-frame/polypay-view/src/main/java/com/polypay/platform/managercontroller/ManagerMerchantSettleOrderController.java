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
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.vo.MerchantSettleOrderVO;

@Controller
public class ManagerMerchantSettleOrderController extends BaseController<MerchantSettleOrderVO> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantSettleOrderController.class);

	@Autowired
	private IMerchantSettleOrderService merchantSettleOrderService;

	@RequestMapping("/merchantmanager/settle/order/list")
	@ResponseBody
	public ServiceResponse listMerchantSettleOrder()
			throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantSettleOrderVO> pageList = null;
			
			MerchantSettleOrderVO merchantSettleOrderVO = new MerchantSettleOrderVO();
			
			
			merchantSettleOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));
			
			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");
			
			Date[] datas;
			if(!StringUtils.isEmpty(createTime))
			{
				datas = DateUtils.changeDate(createTime);
				merchantSettleOrderVO.setcBeginTime(datas[0]);
				merchantSettleOrderVO.setcEndTime(datas[1]);
			}
			
			if(!StringUtils.isEmpty(successTime))
			{
				datas = DateUtils.changeDate(successTime);
				merchantSettleOrderVO.setsBeginTime(datas[0]);
				merchantSettleOrderVO.setsEndTime(datas[1]);
			}
			
				String status = getRequest().getParameter("status");
			
			if(!StringUtils.isEmpty(status))
			{
				merchantSettleOrderVO.setStatus(Integer.parseInt(status));
			}
			
			pageList = merchantSettleOrderService.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO );
			Page<MerchantSettleOrderVO> pageData = getPageData(pageList);
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
