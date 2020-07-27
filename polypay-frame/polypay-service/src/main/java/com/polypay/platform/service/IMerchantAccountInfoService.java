package com.polypay.platform.service;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.Menu;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantAccountInfoVO;

public interface IMerchantAccountInfoService extends IBaseService<MerchantAccountInfo> {

	MerchantAccountInfo getMerchantInfo(MerchantAccountInfoVO merchantInfo) throws ServiceException;

	void registerAndSave(MerchantAccountInfoVO requestMerchantInfo) throws ServiceException;
	
	MerchantAccountInfo getMerchantInfoByUUID(MerchantAccountInfoVO merchantInfo) throws ServiceException;
	
	List<Menu> getMerchantMenu(Integer roleId) throws ServiceException;

	PageList<MerchantAccountInfoVO> listMerchantAccountInfo(PageBounds pageBounds, MerchantAccountInfoVO param) throws ServiceException;

	List<String> listMerchantAccountInfoByProxy(String proxyId) throws ServiceException;

}