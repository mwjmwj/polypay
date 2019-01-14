package com.polypay.platform.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantLoginLog;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantLoginLogVO;

public interface IMerchantLoginLogSerivce extends IBaseService<MerchantLoginLog> {

	PageList<MerchantLoginLogVO> listMerchantLoginLog(PageBounds pageBounds, MerchantLoginLogVO param) throws ServiceException;

}