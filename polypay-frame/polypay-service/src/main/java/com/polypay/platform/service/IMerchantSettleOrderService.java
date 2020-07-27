package com.polypay.platform.service;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantSettleOrderVO;

public interface IMerchantSettleOrderService extends IBaseService<MerchantSettleOrder> {

	PageList<MerchantSettleOrderVO> listMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException;

	List<MerchantBill> getMerchantSettleMonthBill() throws ServiceException;

	MerchantMainDateVO allMerchantSettle(String uuid) throws ServiceException;

	MerchantMainDateVO managerAllMerchantSettleOrder() throws ServiceException;

	List<MerchantSettleOrder> listHandleOrder();

	PageList<MerchantSettleOrderVO> listProxyMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException;

	MerchantSettleOrder getSettleOrderByOrderNo(String orderNo) throws ServiceException;

	PageList<MerchantSettleOrderVO> listMerchantSettleOrder1(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException;

}