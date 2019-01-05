package com.polypay.platform.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantSettleOrderVO;

public interface IMerchantSettleOrderService extends IBaseService<MerchantSettleOrder> {

	PageList<MerchantSettleOrderVO> listMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException;

}