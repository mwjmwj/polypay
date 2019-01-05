package com.polypay.platform.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantApi;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantApiService;
import com.polypay.platform.utils.MerchantUtils;

@RestController
public class MerchantApiController {

	@Autowired
	private IMerchantApiService merchantApiService;

	@GetMapping("/merchant/api/get")
	public ServiceResponse getMerchantApi(HttpServletRequest request) throws ServiceException {
		ServiceResponse response = new ServiceResponse();
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantApi merchantApi = merchantApiService.getMerchantApiByUUID(merchant.getUuid());
		response.setData(merchantApi);
		return response;
	}

	@RequestMapping("/merchant/api/update")
	public ServiceResponse updateMerchantApi(@RequestBody MerchantApi merchantApi, HttpServletRequest request)
			throws ServiceException {
		ServiceResponse response = new ServiceResponse();
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantApi oldMerchantApi = merchantApiService.getMerchantApiByUUID(merchant.getUuid());

		if (null == oldMerchantApi) {
			ResponseUtils.exception(response, "Api信息为空!", RequestStatus.FAILED.getStatus());
			return response;
		}

		// 修改md5 key
		if (!StringUtils.isEmpty(merchantApi.getMd5Key())) {
			oldMerchantApi.setMd5Key(merchantApi.getMd5Key());
		}

		// 修改秘钥
		if (!StringUtils.isEmpty(merchantApi.getSecretKey())) {
			oldMerchantApi.setMd5Key(merchantApi.getSecretKey());
		}
		oldMerchantApi.setUpdateTime(new Date());
		merchantApiService.updateByPrimaryKeySelective(oldMerchantApi);
		response.setMessage("修改成功");
		return response;
	}

}
