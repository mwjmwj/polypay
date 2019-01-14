package com.polypay.platform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantLoginLogSerivce;
import com.polypay.platform.vo.MerchantLoginLogVO;

@Controller
public class MerchantLoginLogController extends BaseController<MerchantLoginLogVO>{

	private final static Logger log = LoggerFactory.getLogger(MerchantLoginLogController.class);
	
	@Autowired
	private IMerchantLoginLogSerivce merchantLoginLogSerivce;
	
	@RequestMapping("merchant/loginlog/list")
	@ResponseBody
	public ServiceResponse listMerchantLoginLog() throws ServiceException
	{
		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantLoginLogVO> pageList = null;
			MerchantLoginLogVO param = new MerchantLoginLogVO();
			pageList = merchantLoginLogSerivce.listMerchantLoginLog(pageBounds, param);
			Page<MerchantLoginLogVO> pageData = getPageData(pageList);
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
