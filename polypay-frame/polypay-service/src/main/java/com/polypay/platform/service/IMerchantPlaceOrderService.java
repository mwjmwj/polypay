package com.polypay.platform.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

public interface IMerchantPlaceOrderService extends IBaseService<MerchantPlaceOrder> {

	PageList<MerchantPlaceOrderVO> listMerchantPlaceOrder(PageBounds pageBounds,
			MerchantPlaceOrderVO merchantPlaceOrderVO) throws ServiceException;

}