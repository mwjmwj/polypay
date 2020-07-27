package com.polypay.platform.service;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.vo.MerchantAllRechargeVO;
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

	PageList<MerchantRechargeOrderVO> listProxyMerchantRechargeOrder(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantRechargeOrderVO) throws ServiceException;
	
	/**
	 *  后台总管理获取今日数据
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	MerchantMainDateVO getTodayMerchantOrder(MerchantRechargeOrderVO param) throws ServiceException;

	/**
	 *   代理后台获取代理商下 商户总订单信息
	 * @param uuid
	 * @return
	 * @throws ServiceException 
	 */
	MerchantMainDateVO sumProxyMerchantRechargeOrder(String uuid) throws ServiceException;

	MerchantMainDateVO sumTodayProxyMerchantRechargeOrder(MerchantRechargeOrderVO param) throws ServiceException;
	
	
	
	/**
	 *  代理商户详细信息总览
	 * @param pageBounds
	 * @param merchantRechargeOrderVO
	 * @return
	 */
	PageList<MerchantAllRechargeVO> listMerchantrechargeall(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantRechargeOrderVO) throws ServiceException;

}