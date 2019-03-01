package com.polypay.platform.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantApi;

public interface MerchantApiMapper extends BaseMapper<MerchantApi> {

	MerchantApi getMerchantApiByUUID(String merchantUUID);

	void updateMerchantApi(MerchantApi merchantApi);

	PageList<MerchantApi> listMerchantApi(PageBounds pageBounds, MerchantApi merchantApi);

}