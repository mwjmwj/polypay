package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.polypay.platform.bean.MerchantVerify;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantVerifyMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantVerifyService;

@Service
public class MerchantVerifyService implements IMerchantVerifyService {

	@Autowired
	private MerchantVerifyMapper merchantVerifyMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantVerifyMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantVerify record) throws ServiceException {
		try {
			merchantVerifyMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantVerify record) throws ServiceException {
		try {
			merchantVerifyMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantVerify selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantVerify selectByPrimaryKey = merchantVerifyMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantVerify record) throws ServiceException {
		try {
			merchantVerifyMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantVerify record) throws ServiceException {
		try {
			merchantVerifyMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantVerify queryMerchantVerifyCode(MerchantVerify tbVerifycodeVO) throws ServiceException {
		MerchantVerify resultVerify;
		try {
			resultVerify = merchantVerifyMapper.queryMerchantVerifyCode(tbVerifycodeVO);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return resultVerify;
	}

}
