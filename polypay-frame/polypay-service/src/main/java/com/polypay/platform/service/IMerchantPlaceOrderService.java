package com.polypay.platform.service;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

public interface IMerchantPlaceOrderService extends IBaseService<MerchantPlaceOrder> {

	PageList<MerchantPlaceOrderVO> listMerchantPlaceOrder(PageBounds pageBounds,
			MerchantPlaceOrderVO merchantPlaceOrderVO) throws ServiceException;

	List<MerchantBill> getMerchantPlaceMonthBill() throws ServiceException;

	MerchantMainDateVO allMerchantPlace(String uuid) throws ServiceException;

	MerchantMainDateVO managerAllMerchantPlaceOrder() throws ServiceException;

	List<MerchantPlaceOrder> listHandleOrder();

	PageList<MerchantPlaceOrderVO> listProxyMerchantPlaceOrder(PageBounds pageBounds,
			MerchantPlaceOrderVO merchantPlaceOrderVO) throws ServiceException;

	MerchantPlaceOrder getPlaceOrderByOrderNo(String orderNo) throws ServiceException;

}