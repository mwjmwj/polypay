package com.polypay.platform.managercontroller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

@Controller
public class ManagerMerchantRechargeOrderController extends BaseController<MerchantRechargeOrderVO> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantRechargeOrderController.class);

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;

	@RequestMapping("/merchantmanager/recharge/order/list")
	@ResponseBody
	public ServiceResponse listMerchantRechargeOrder(HttpServletRequest request) throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {

			MerchantRechargeOrderVO merchantRechargeOrderVO = new MerchantRechargeOrderVO();
			String status = request.getParameter("status");

			if (!StringUtils.isEmpty(status)) {
				merchantRechargeOrderVO.setStatus(Integer.parseInt(status));
			}

			merchantRechargeOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));

			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");

			Date[] datas;
			if (!StringUtils.isEmpty(createTime)) {
				datas = DateUtils.changeDate(createTime);
				merchantRechargeOrderVO.setcBeginTime(datas[0]);
				merchantRechargeOrderVO.setcEndTime(datas[1]);
			}

			if (!StringUtils.isEmpty(successTime)) {
				datas = DateUtils.changeDate(successTime);
				merchantRechargeOrderVO.setsBeginTime(datas[0]);
				merchantRechargeOrderVO.setsEndTime(datas[1]);
			}
			
			String merchantId = getRequest().getParameter("merchantId");
			if (!StringUtils.isEmpty(merchantId)) {
				merchantRechargeOrderVO.setMerchantId(merchantId);;
			}

			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantRechargeOrderVO> pageList = null;
			pageList = merchantRechargeOrderService.listMerchantRechargeOrder(pageBounds, merchantRechargeOrderVO);
			Page<MerchantRechargeOrderVO> pageData = getPageData(pageList);
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

	@GetMapping("managermerchant/recharge/all")
	@ResponseBody
	public ServiceResponse allMerchantRecharge() throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantMainDateVO merchantGroupDate = merchantRechargeOrderService.managerAllMerchantRechargeOrder();
		response.setData(merchantGroupDate);

		return response;
	}

}
