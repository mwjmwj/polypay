package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantFinanceMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;

@Service
public class MerchantFinanceService implements IMerchantFinanceService {
	
	@Autowired
	private MerchantFinanceMapper merchantFinanceMapper;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantFinanceMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantFinance record) throws ServiceException {
		try {
			merchantFinanceMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantFinance record) throws ServiceException {
		try {
			merchantFinanceMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantFinance selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantFinance selectByPrimaryKey = merchantFinanceMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantFinance record) throws ServiceException {
		try {
			merchantFinanceMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantFinance record) throws ServiceException {
		try {
			merchantFinanceMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}
	

	@Override
	public MerchantFinance getMerchantFinanceByUUID(String merchantUUID) throws ServiceException {

		try {
			return merchantFinanceMapper.getMerchantFinanceByUUID(merchantUUID);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
