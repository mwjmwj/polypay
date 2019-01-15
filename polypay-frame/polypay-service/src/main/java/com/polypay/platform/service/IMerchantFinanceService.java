package com.polypay.platform.service;

import java.util.List;
import java.util.Map;

import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantFinanceService extends IBaseService<MerchantFinance>{
	MerchantFinance getMerchantFinanceByUUID(String merchantUUID) throws ServiceException;

	List<Map<String,Object>> listMerchantFinance(String uuid) throws ServiceException;

}
