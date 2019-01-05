package com.polypay.platform.dao;

import com.polypay.platform.bean.MerchantVerify;

public interface MerchantVerifyMapper extends BaseMapper<MerchantVerify> {

	MerchantVerify queryMerchantVerifyCode(MerchantVerify tbVerifycodeVO);

}