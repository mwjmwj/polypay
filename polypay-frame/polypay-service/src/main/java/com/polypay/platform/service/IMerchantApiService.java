package com.polypay.platform.service;

import com.polypay.platform.bean.MerchantApi;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantApiService extends IBaseService<MerchantApi> {

	MerchantApi getMerchantApiByUUID(String merchantUUID) throws ServiceException;

	void updateMerchantApi(MerchantApi merchantApi) throws ServiceException;
}