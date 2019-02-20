package com.polypay.platform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.polypay.platform.bean.PayType;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.PayTypeMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IPayTypeService;

@Service
public class PayTypeService implements IPayTypeService {

	@Autowired
	private PayTypeMapper PayTypeMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			PayTypeMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(PayType record) throws ServiceException {
		try {
			PayTypeMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(PayType record) throws ServiceException {
		try {
			PayTypeMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public PayType selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			PayType selectByPrimaryKey = PayTypeMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(PayType record) throws ServiceException {
		try {
			return PayTypeMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKey(PayType record) throws ServiceException {
		try {
			return PayTypeMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public PayType getRateByLevelAndChannel(Integer payLevel, String payChannel) throws ServiceException {
		try {
			Map<String,Object> param = Maps.newHashMap();
			param.put("payLevel", payLevel);
			param.put("payChannel", payChannel);
			return PayTypeMapper.getRateByLevelAndChannel(param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<PayType> listPayType(Integer payLevel) throws ServiceException {
		try {
			return PayTypeMapper.listPayType(payLevel);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<PayType> list(PayType payType) {
		return PayTypeMapper.list(payType);
	}

}
