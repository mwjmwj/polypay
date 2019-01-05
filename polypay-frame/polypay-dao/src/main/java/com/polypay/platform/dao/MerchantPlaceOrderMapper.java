package com.polypay.platform.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

public interface MerchantPlaceOrderMapper extends BaseMapper<MerchantPlaceOrder> {

	PageList<MerchantPlaceOrderVO> listMerchantPlaceOrder(PageBounds pageBounds,
			MerchantPlaceOrderVO merchantPlaceOrderVO);

}