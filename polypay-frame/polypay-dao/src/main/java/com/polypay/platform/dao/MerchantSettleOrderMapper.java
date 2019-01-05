package com.polypay.platform.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.vo.MerchantSettleOrderVO;

public interface MerchantSettleOrderMapper extends BaseMapper<MerchantSettleOrder> {

	PageList<MerchantSettleOrderVO> listMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO);

}