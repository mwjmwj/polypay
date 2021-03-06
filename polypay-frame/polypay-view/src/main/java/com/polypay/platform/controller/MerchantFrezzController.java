package com.polypay.platform.controller;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantFrezzon;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFrezzService;
import com.polypay.platform.utils.DateUtils;

@Controller
public class MerchantFrezzController extends BaseController<MerchantFrezzon>{
	
	private final static Logger log = LoggerFactory.getLogger(MerchantFrezzController.class);
	
	@Autowired
	private IMerchantFrezzService merchantFrezzService;
	
	@RequestMapping("merchant/frezz/list")
	@ResponseBody
	public ServiceResponse listMerchantFrezz() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			MerchantFrezzon merchantFrezzon = new MerchantFrezzon();
			PageList<MerchantFrezzon> pageList = null;
			merchantFrezzon.setOrderNumber(getRequest().getParameter("orderNumber"));
			
			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");
			
			Date[] datas;
			if(!StringUtils.isEmpty(createTime))
			{
				datas = DateUtils.changeDate(createTime);
				merchantFrezzon.setcBeginTime(datas[0]);
				merchantFrezzon.setcEndTime(datas[1]);
			}
			
			if(!StringUtils.isEmpty(successTime))
			{
				datas = DateUtils.changeDate(successTime);
				merchantFrezzon.setsBeginTime(datas[0]);
				merchantFrezzon.setsEndTime(datas[1]);
			}
			
			pageList = merchantFrezzService.listMerchantFrezz(pageBounds, merchantFrezzon);
			Page<MerchantFrezzon> pageData = getPageData(pageList);
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
	
	@RequestMapping("merchant/frezz/query")
	public String queryMerchantFrezz(@RequestParam("id")Integer id,Map<String,Object> result) throws ServiceException
	{
		MerchantFrezzon selectByPrimaryKey = merchantFrezzService.selectByPrimaryKey(id);
		result.put("merchantFrezz", selectByPrimaryKey);
		return "admin/merchantfrezzedit";
	}

}
