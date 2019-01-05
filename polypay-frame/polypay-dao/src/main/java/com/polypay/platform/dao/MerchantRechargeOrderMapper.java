package com.polypay.platform.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

public interface MerchantRechargeOrderMapper extends BaseMapper<MerchantRechargeOrder> {

	PageList<MerchantRechargeOrderVO> listMerchantRechargeOrder(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantPlaceOrderVO);

	MerchantRechargeOrder getOrderByMerchantOrderNumber(String merchantOrderNumber);
	
	MerchantRechargeOrder getOrderByOrderNumber(String merchantOrderNumber);

}
