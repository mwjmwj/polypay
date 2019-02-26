package com.polypay.platform.service;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

public interface IMerchantRechargeOrderService extends IBaseService<MerchantRechargeOrder> {

	PageList<MerchantRechargeOrderVO> listMerchantRechargeOrder(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantPlaceOrderVO) throws ServiceException;

	MerchantRechargeOrder getOrderByMerchantOrderNumber(Map<String,Object> param) throws ServiceException;
	
	MerchantRechargeOrder getOrderByOrderNumber(String merchantOrderNumber) throws ServiceException;

	MerchantMainDateVO getMerchantGroupDate(String uuid) throws ServiceException;

	List<MerchantMainDateVO> allTimeMerchantOrder(String uuid) throws ServiceException;

	List<MerchantBill> getMerchantRechargeMonthBill() throws ServiceException;

	MerchantMainDateVO managerAllMerchantRechargeOrder() throws ServiceException;

	List<MerchantRechargeOrder> listHandleOrder() throws ServiceException;

}