package com.polypay.platform.service;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantFrezzon;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantFrezzService extends IBaseService<MerchantFrezzon>{

	PageList<MerchantFrezzon> listMerchantFrezz(PageBounds pageBounds, MerchantFrezzon merchantFrezzon) throws ServiceException;

	List<MerchantFrezzon> unFrezzMerchantList() throws ServiceException;

	String allMerchantFrezzAmount(String uuid) throws ServiceException;

}
