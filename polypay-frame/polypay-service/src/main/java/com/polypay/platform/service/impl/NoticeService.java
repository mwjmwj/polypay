package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.polypay.platform.bean.Notice;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.NoticeMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.INoticeService;

@Service
public class NoticeService implements INoticeService {

	@Autowired
	private NoticeMapper NoticeMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			NoticeMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(Notice record) throws ServiceException {
		try {
			NoticeMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(Notice record) throws ServiceException {
		try {
			NoticeMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public Notice selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			Notice selectByPrimaryKey = NoticeMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(Notice record) throws ServiceException {
		try {
			NoticeMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(Notice record) throws ServiceException {
		try {
			NoticeMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

}
