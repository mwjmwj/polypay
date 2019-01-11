package com.polypay.platform.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantAccountBindbank;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantAccountBindbankVO;

public interface IMerchantAccountBindbankService extends IBaseService<MerchantAccountBindbank> {

	MerchantAccountBindbank selectMerchantBindBankByID(Integer id) throws ServiceException;

	PageList<MerchantAccountBindbankVO> listMerchantBindBank(PageBounds pageBounds, MerchantAccountBindbankVO param) throws ServiceException;

	void reverseBankStatus() throws ServiceException;

}
