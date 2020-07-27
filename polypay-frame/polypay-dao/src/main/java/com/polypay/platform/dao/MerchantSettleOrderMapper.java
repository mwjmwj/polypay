package com.polypay.platform.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantSettleOrderVO;

public interface MerchantSettleOrderMapper extends BaseMapper<MerchantSettleOrder> {

	PageList<MerchantSettleOrderVO> listMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO);

	List<MerchantBill> getMerchantSettleMonthBill(Map<String, Object> param);

	MerchantMainDateVO allMerchantSettle(String uuid);

	MerchantMainDateVO managerAllMerchantSettleOrder();

	List<MerchantSettleOrder> listHandleOrder();

	PageList<MerchantSettleOrderVO> listProxyMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO);

	MerchantSettleOrder getSettleOrderByOrderNo(String orderNo);

	PageList<MerchantSettleOrderVO> listProxyMerchantSettleOrder1(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO);
}