package com.polypay.platform.dao;

import com.polypay.platform.bean.MerchantCompany;

public interface MerchantCompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantCompany record);

    int insertSelective(MerchantCompany record);

    MerchantCompany selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantCompany record);

    int updateByPrimaryKey(MerchantCompany record);
}