package com.polypay.platform.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantBillService extends IBaseService<MerchantBill>{

	PageList<MerchantBill> listMerchantBill(PageBounds pageBounds,
			MerchantBill merchantBill) throws ServiceException;
	
}
