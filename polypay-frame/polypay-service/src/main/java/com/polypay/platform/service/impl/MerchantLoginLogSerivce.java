package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.polypay.platform.bean.MerchantLoginLog;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantLoginLogMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantLoginLogSerivce;

@Service
public class MerchantLoginLogSerivce implements IMerchantLoginLogSerivce {

	@Autowired
	private MerchantLoginLogMapper MerchantLoginLogMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantLoginLogMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantLoginLog record) throws ServiceException {
		try {
			MerchantLoginLogMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantLoginLog record) throws ServiceException {
		try {
			MerchantLoginLogMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantLoginLog selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantLoginLog selectByPrimaryKey = MerchantLoginLogMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantLoginLog record) throws ServiceException {
		try {
			MerchantLoginLogMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantLoginLog record) throws ServiceException {
		try {
			MerchantLoginLogMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

}
