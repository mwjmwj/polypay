package com.polypay.platform.service;

import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantAccountInfoVO;

public interface IMerchantAccountInfoService extends IBaseService<MerchantAccountInfo> {

	MerchantAccountInfo getMerchantInfo(MerchantAccountInfoVO merchantInfo) throws ServiceException;

	void registerAndSave(MerchantAccountInfoVO requestMerchantInfo) throws ServiceException;
	
	MerchantAccountInfo getMerchantInfoByUUID(MerchantAccountInfoVO merchantInfo) throws ServiceException;

}