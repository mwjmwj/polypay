package com.polypay.platform.managercontroller;

import java.util.Map;

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
import com.polypay.platform.bean.MerchantApi;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantApiService;

@Controller
public class ManagerMerchantApiController extends BaseController<MerchantApi> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantAccountController.class);

	@Autowired
	private IMerchantApiService merchantApiService;

	@RequestMapping("/merchantmanager/api/list")
	@ResponseBody
	public ServiceResponse listMerchantApi() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			MerchantApi merchantApi = new MerchantApi();
			PageList<MerchantApi> pageList = null;

			String merchantId = getRequest().getParameter("merchantId");
			if(!StringUtils.isEmpty(merchantId)){
				merchantApi.setMerchantId(merchantId);
			}
			pageList = merchantApiService.listMerchantApi(pageBounds, merchantApi);
			Page<MerchantApi> pageData = getPageData(pageList);
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

	@RequestMapping("/merchantmanager/api/query")
	public String getMerchantApi(Integer apiId,Map<String,Object> result) throws ServiceException {
		try {
			MerchantApi selectByPrimaryKey = merchantApiService.selectByPrimaryKey(apiId);
			result.put("merchantapi", selectByPrimaryKey);
		} catch (ServiceException e) {
			log.error(e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		}

		return "adminmanager/merchantapi";
	}

}
