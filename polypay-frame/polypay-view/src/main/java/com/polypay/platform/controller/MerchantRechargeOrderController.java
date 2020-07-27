package com.polypay.platform.controller;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
public class MerchantRechargeOrderController extends BaseController<MerchantRechargeOrderVO> {

	private Logger log = LoggerFactory.getLogger(MerchantRechargeOrderController.class);

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;

	@RequestMapping("/merchant/recharge/order/list")
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
			
			merchantRechargeOrderVO.setMerchantOrderNumber(getRequest().getParameter("morderNumber"));
			
			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");
			
			Date[] datas;
			if(!StringUtils.isEmpty(createTime))
			{
				datas = DateUtils.changeDate(createTime);
				merchantRechargeOrderVO.setcBeginTime(datas[0]);
				merchantRechargeOrderVO.setcEndTime(datas[1]);
			}
			
			if(!StringUtils.isEmpty(successTime))
			{
				datas = DateUtils.changeDate(successTime);
				merchantRechargeOrderVO.setsBeginTime(datas[0]);
				merchantRechargeOrderVO.setsEndTime(datas[1]);
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
	
	@GetMapping("merchant/recharge/query")
	public String queryMerchantRechargeOrder(@RequestParam(name="id") Integer id,Map<String,Object> result) throws ServiceException
	{
		MerchantRechargeOrder selectByPrimaryKey = merchantRechargeOrderService.selectByPrimaryKey(id);
		result.put("rechargeorder", selectByPrimaryKey);
		return "admin/merchantrechargeedit";
	}
	
	@GetMapping("merchant/recharge/all")
	@ResponseBody
	public ServiceResponse allMerchantRecharge() throws ServiceException
	{
		ServiceResponse response = new ServiceResponse();
		
		MerchantMainDateVO merchantGroupDate = merchantRechargeOrderService.getMerchantGroupDate(MerchantUtils.getMerchant().getUuid());
		response.setData(merchantGroupDate);
		
		return response;
	}

	
	
}
