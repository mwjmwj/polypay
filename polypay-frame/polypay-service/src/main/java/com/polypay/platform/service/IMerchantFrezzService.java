package com.polypay.platform.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantFrezzon;
import com.polypay.platform.exception.ServiceException;

public interface IMerchantFrezzService extends IBaseService<MerchantFrezzon>{

	PageList<MerchantFrezzon> listMerchantFrezz(PageBounds pageBounds, MerchantFrezzon merchantFrezzon) throws ServiceException;

}
