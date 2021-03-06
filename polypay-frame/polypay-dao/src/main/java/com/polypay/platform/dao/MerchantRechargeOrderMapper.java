package com.polypay.platform.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.vo.MerchantAllRechargeVO;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

import java.util.List;
import java.util.Map;

public interface MerchantRechargeOrderMapper extends BaseMapper<MerchantRechargeOrder> {

	PageList<MerchantRechargeOrderVO> listMerchantRechargeOrder(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantPlaceOrderVO);

	MerchantRechargeOrder getOrderByMerchantOrderNumber(Map<String,Object> param);
	
	MerchantRechargeOrder getOrderByOrderNumber(String merchantOrderNumber);

	MerchantMainDateVO getMerchantGroupDate(Map<String,Object> param);

	List<MerchantMainDateVO> allTimeMerchantOrder(String merchantUUID);

	List<MerchantBill> getMerchantRechargeMonthBill(Map<String,Object> param);
	
	MerchantMainDateVO managerAllMerchantRechargeOrder();

	List<MerchantRechargeOrder> listHandleOrder();

	PageList<MerchantRechargeOrderVO> listProxyMerchantRechargeOrder(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantRechargeOrderVO);

	MerchantMainDateVO getTodayMerchantOrder(MerchantRechargeOrderVO param);

	MerchantMainDateVO sumProxyMerchantRechargeOrder(String uuid);

	MerchantMainDateVO sumTodayProxyMerchantRechargeOrder(MerchantRechargeOrderVO param);

	PageList<MerchantAllRechargeVO> listMerchantrechargeall(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantRechargeOrderVO);

	PageList<MerchantRechargeOrderVO> listMerchantrechargeall1(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantRechargeOrderVO);

}
