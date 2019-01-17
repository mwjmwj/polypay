package com.polypay.platform.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;

public interface MerchantBillMapper extends BaseMapper<MerchantBill>{

	List<MerchantBill> getBeforeMonthBill(Map<String, Object> param);

	void batchInsert(List<MerchantBill> needBill);

	PageList<MerchantBill> listMerchantBill(PageBounds pageBounds, MerchantBill merchantBill);

}