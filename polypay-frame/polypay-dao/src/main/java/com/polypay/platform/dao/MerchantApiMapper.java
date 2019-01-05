package com.polypay.platform.dao;

import com.polypay.platform.bean.MerchantApi;

public interface MerchantApiMapper extends BaseMapper<MerchantApi> {

	MerchantApi getMerchantApiByUUID(String merchantUUID);

	void updateMerchantApi(MerchantApi merchantApi);

}