package com.polypay.platform.dao;

import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.vo.MerchantAccountInfoVO;

public interface MerchantAccountInfoMapper extends BaseMapper<MerchantAccountInfo> {

	MerchantAccountInfo getMerchantInfo(MerchantAccountInfoVO merchantInfo);
	
	MerchantAccountInfo getMerchantInfoByUUID(MerchantAccountInfoVO merchantInfo);
	

}