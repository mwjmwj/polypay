package com.polypay.platform.service;

import com.polypay.platform.bean.MerchantVerify;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantVerifyService extends IBaseService<MerchantVerify> {

	MerchantVerify queryMerchantVerifyCode(MerchantVerify tbVerifycodeVO) throws ServiceException;

}