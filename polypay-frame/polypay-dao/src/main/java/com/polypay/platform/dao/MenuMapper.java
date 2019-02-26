package com.polypay.platform.dao;

import java.util.List;

import com.polypay.platform.bean.Menu;

public interface MenuMapper extends BaseMapper<Menu>{
    int deleteByPrimaryKey(Integer menuId);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer menuId);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

	List<Menu> getMenusByRoleId(Integer roleId);
}