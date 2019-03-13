package com.polypay.platform.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

public interface MerchantPlaceOrderMapper extends BaseMapper<MerchantPlaceOrder> {

	PageList<MerchantPlaceOrderVO> listMerchantPlaceOrder(PageBounds pageBounds,
			MerchantPlaceOrderVO merchantPlaceOrderVO);

	List<MerchantBill> getMerchantPlaceMonthBill(Map<String, Object> param);

	MerchantMainDateVO allMerchantPlace(String uuid);
	
	MerchantMainDateVO managerAllMerchantPlaceOrder();

	List<MerchantPlaceOrder> listHandleOrder();

	PageList<MerchantPlaceOrderVO> listProxyMerchantPlaceOrder(PageBounds pageBounds,
			MerchantPlaceOrderVO merchantPlaceOrderVO);

	MerchantPlaceOrder getPlaceOrderByOrderNo(String orderNo);

}