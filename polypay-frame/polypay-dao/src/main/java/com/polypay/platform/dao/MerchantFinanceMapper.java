package com.polypay.platform.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantFinance;

import java.util.List;

public interface MerchantFinanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantFinance record);

    int insertSelective(MerchantFinance record);

    MerchantFinance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantFinance record);

    int updateByPrimaryKey(MerchantFinance record);

	MerchantFinance getMerchantFinanceByUUID(String merchantUUID);

	PageList<MerchantFinance> listMerchantFinance(PageBounds pageBounds, MerchantFinance merchantFinance);

	MerchantFinance managerAllMerchantFinance();

	MerchantFinance allProxyMerchantMerchantFinance(String proxyId);

	List<MerchantFinance> listFindMerchantFinance(List<String> mids);
}