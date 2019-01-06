package com.polypay.platform.service;

import java.util.List;

import com.polypay.platform.bean.Menu;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantAccountInfoVO;

public interface IMerchantAccountInfoService extends IBaseService<MerchantAccountInfo> {

	MerchantAccountInfo getMerchantInfo(MerchantAccountInfoVO merchantInfo) throws ServiceException;

	void registerAndSave(MerchantAccountInfoVO requestMerchantInfo) throws ServiceException;
	
	MerchantAccountInfo getMerchantInfoByUUID(MerchantAccountInfoVO merchantInfo) throws ServiceException;
	
	List<Menu> getMerchantMenu(Integer roleId) throws ServiceException;

}