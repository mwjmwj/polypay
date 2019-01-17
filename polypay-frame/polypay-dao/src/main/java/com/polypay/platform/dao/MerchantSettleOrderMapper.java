package com.polypay.platform.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.vo.MerchantSettleOrderVO;

public interface MerchantSettleOrderMapper extends BaseMapper<MerchantSettleOrder> {

	PageList<MerchantSettleOrderVO> listMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO);

	List<MerchantBill> getMerchantSettleMonthBill(Map<String, Object> param);

}