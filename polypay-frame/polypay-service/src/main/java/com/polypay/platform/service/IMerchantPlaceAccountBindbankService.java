package com.polypay.platform.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantPlaceAccountBindbank;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantPlaceAccountBindbankVO;

public interface IMerchantPlaceAccountBindbankService extends IBaseService<MerchantPlaceAccountBindbank> {

	MerchantPlaceAccountBindbank selectMerchantBindBankByID(Integer id) throws ServiceException;

	PageList<MerchantPlaceAccountBindbankVO> listMerchantBindBank(PageBounds pageBounds, MerchantPlaceAccountBindbankVO param) throws ServiceException;

	void reverseBankStatus() throws ServiceException;

}
