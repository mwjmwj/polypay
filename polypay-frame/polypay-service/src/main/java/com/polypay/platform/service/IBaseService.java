package com.polypay.platform.service;

import com.polypay.platform.exception.ServiceException;

public interface IBaseService<T> {
	int deleteByPrimaryKey(Integer id) throws ServiceException;

	int insert(T record) throws ServiceException;

	int insertSelective(T record) throws ServiceException;

	T selectByPrimaryKey(Integer id) throws ServiceException;

	int updateByPrimaryKeySelective(T record) throws ServiceException;

	int updateByPrimaryKey(T record) throws ServiceException;
}
