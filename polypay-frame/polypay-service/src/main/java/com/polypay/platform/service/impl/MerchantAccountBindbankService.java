package com.polypay.platform.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantAccountBindbank;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantAccountBindbankMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountBindbankService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantAccountBindbankVO;

@Service
public class MerchantAccountBindbankService implements IMerchantAccountBindbankService {

	@Autowired
	private MerchantAccountBindbankMapper merchantAccountBindbankMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantAccountBindbankMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantAccountBindbank record) throws ServiceException {
		try {
			merchantAccountBindbankMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantAccountBindbank record) throws ServiceException {
		try {
			merchantAccountBindbankMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantAccountBindbank selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantAccountBindbank selectByPrimaryKey = merchantAccountBindbankMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantAccountBindbank record) throws ServiceException {
		try {
			merchantAccountBindbankMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantAccountBindbank record) throws ServiceException {
		try {
			merchantAccountBindbankMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}
	
	@Override
	public MerchantAccountBindbank selectMerchantBindBankByID(Integer id) throws ServiceException {
		try {
			MerchantAccountInfo merchantAccountInfo = MerchantUtils.getMerchant();
			Map<String,Object> param =Maps.newHashMap();
			param.put("id", id);
			param.put("merchantId", merchantAccountInfo.getUuid());
			MerchantAccountBindbank selectByPrimaryKey = merchantAccountBindbankMapper.selectMerchantBindBankByID(param);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public PageList<MerchantAccountBindbankVO> listMerchantBindBank(PageBounds pageBounds,
			MerchantAccountBindbankVO param) throws ServiceException{
		try {
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			param.setMerchantId(merchant.getUuid());
			return merchantAccountBindbankMapper.listMerchantBindBank(pageBounds,param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public void reverseBankStatus() throws ServiceException {
		try {
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			merchantAccountBindbankMapper.reverseBankStatus(merchant.getUuid());
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
