package com.polypay.platform.service;

import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantFinanceService extends IBaseService<MerchantFinance>{
	MerchantFinance getMerchantFinanceByUUID(String merchantUUID) throws ServiceException;

}
