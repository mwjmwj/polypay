package com.polypay.platform.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.vo.MerchantAccountInfoVO;

public interface MerchantAccountInfoMapper extends BaseMapper<MerchantAccountInfo> {

	MerchantAccountInfo getMerchantInfo(MerchantAccountInfoVO merchantInfo);
	
	MerchantAccountInfo getMerchantInfoByUUID(MerchantAccountInfoVO merchantInfo);

	PageList<MerchantAccountInfoVO> listMerchantBindBank(PageBounds pageBounds, MerchantAccountInfoVO param);
	

}