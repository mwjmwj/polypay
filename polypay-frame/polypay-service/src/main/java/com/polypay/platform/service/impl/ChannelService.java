package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.polypay.platform.bean.Channel;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.ChannelMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IChannelService;


@Service
public class ChannelService implements IChannelService{

	@Autowired
	private ChannelMapper channelMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			channelMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(Channel record) throws ServiceException {
		try {
			channelMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(Channel record) throws ServiceException {
		try {
			channelMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public Channel selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			Channel selectByPrimaryKey = channelMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(Channel record) throws ServiceException {
		try {
			channelMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(Channel record) throws ServiceException {
		try {
			channelMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}
	
	

}
