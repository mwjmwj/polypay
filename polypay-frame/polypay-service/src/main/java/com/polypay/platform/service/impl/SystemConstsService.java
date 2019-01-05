package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.polypay.platform.bean.SystemConsts;
import com.polypay.platform.dao.SystemConstsMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.ISystemConstsService;

@Service
public class SystemConstsService implements ISystemConstsService{

	@Autowired
	private SystemConstsMapper systemConstsMapper;

	@Override
	public SystemConsts getConsts(String constsKey) throws ServiceException {
		try {
			return systemConstsMapper.getConstsByKey(constsKey);
		} catch (DataAccessException e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
