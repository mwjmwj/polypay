package com.polypay.platform.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantFinanceService extends IBaseService<MerchantFinance>{
	MerchantFinance getMerchantFinanceByUUID(String merchantUUID) throws ServiceException;

	List<Map<String,Object>> listMerchantFinance(String uuid) throws ServiceException;

	PageList<MerchantFinance> listMerchantFinance(PageBounds pageBounds, MerchantFinance merchantFinance) throws ServiceException;

	MerchantFinance allMerchantFinance() throws ServiceException;

	MerchantFinance allProxyMerchantMerchantFinance(String uuid) throws ServiceException;

	List<MerchantFinance> listFindMerchantFinance(List<String> mids) throws ServiceException;
	
	int updateByPrimaryKeySelective(MerchantFinance record,BigDecimal frez,BigDecimal arr,String type) throws ServiceException;

}
