package com.polypay.platform.dao;

import com.polypay.platform.bean.SystemConsts;

public interface SystemConstsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemConsts record);

    int insertSelective(SystemConsts record);

    SystemConsts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemConsts record);

    int updateByPrimaryKey(SystemConsts record);

	SystemConsts getConstsByKey(String apiDoc);
}