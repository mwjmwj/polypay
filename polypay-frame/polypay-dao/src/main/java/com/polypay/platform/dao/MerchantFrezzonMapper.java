package com.polypay.platform.dao;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantFrezzon;

public interface MerchantFrezzonMapper extends BaseMapper<MerchantFrezzon>{

	PageList<MerchantFrezzon> listMerchantFrezz(PageBounds pageBounds, MerchantFrezzon merchantFrezzon);

	List<MerchantFrezzon> unFrezzMerchantList();

	String allMerchantFrezzAmount(String uuid);

}