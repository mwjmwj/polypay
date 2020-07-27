package com.polypay.platform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantLoginLog;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantLoginLogMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantLoginLogSerivce;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantLoginLogVO;

@Service
public class MerchantLoginLogSerivce implements IMerchantLoginLogSerivce {

	@Autowired
	private MerchantLoginLogMapper merchantLoginLogMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantLoginLogMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantLoginLog record) throws ServiceException {
		try {
			merchantLoginLogMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantLoginLog record) throws ServiceException {
		try {
			merchantLoginLogMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantLoginLog selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantLoginLog selectByPrimaryKey = merchantLoginLogMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantLoginLog record) throws ServiceException {
		try {
			merchantLoginLogMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantLoginLog record) throws ServiceException {
		try {
			merchantLoginLogMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public PageList<MerchantLoginLogVO> listMerchantLoginLog(PageBounds pageBounds, MerchantLoginLogVO param) throws ServiceException {
		try {
			
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			param.setMerchantId(merchant.getUuid());
			return merchantLoginLogMapper.listMerchantLoginLog(pageBounds,param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}
	
	@Override
	public PageList<MerchantLoginLogVO> listManagerMerchantLoginLog(PageBounds pageBounds, MerchantLoginLogVO param) throws ServiceException {
		try {
		
			return merchantLoginLogMapper.listMerchantLoginLog(pageBounds,param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}


}
