package com.polypay.platform.service;

import com.polypay.platform.bean.MerchantAccountBindbank;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantAccountBindbankService extends IBaseService<MerchantAccountBindbank> {

	MerchantAccountBindbank selectMerchantBindBankByID(Integer id) throws ServiceException;

}
