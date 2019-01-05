package com.polypay.platform.service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

public interface IMerchantRechargeOrderService extends IBaseService<MerchantRechargeOrder> {

	PageList<MerchantRechargeOrderVO> listMerchantRechargeOrder(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantPlaceOrderVO) throws ServiceException;

	MerchantRechargeOrder getOrderByMerchantOrderNumber(String merchantOrderNumber) throws ServiceException;
	
	MerchantRechargeOrder getOrderByOrderNumber(String merchantOrderNumber) throws ServiceException;

}