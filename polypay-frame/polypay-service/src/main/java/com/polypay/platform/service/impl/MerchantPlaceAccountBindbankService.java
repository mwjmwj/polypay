package com.polypay.platform.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantPlaceAccountBindbank;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantPlaceAccountBindbankMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantPlaceAccountBindbankService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantPlaceAccountBindbankVO;

@Service
public class MerchantPlaceAccountBindbankService implements IMerchantPlaceAccountBindbankService {

	@Autowired
	private MerchantPlaceAccountBindbankMapper merchantPlaceAccountBindbankMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantPlaceAccountBindbankMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantPlaceAccountBindbank record) throws ServiceException {
		try {
			merchantPlaceAccountBindbankMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantPlaceAccountBindbank record) throws ServiceException {
		try {
			merchantPlaceAccountBindbankMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantPlaceAccountBindbank selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantPlaceAccountBindbank selectByPrimaryKey = merchantPlaceAccountBindbankMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantPlaceAccountBindbank record) throws ServiceException {
		try {
			merchantPlaceAccountBindbankMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantPlaceAccountBindbank record) throws ServiceException {
		try {
			merchantPlaceAccountBindbankMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}
	
	@Override
	public MerchantPlaceAccountBindbank selectMerchantBindBankByID(Integer id) throws ServiceException {
		try {
			MerchantAccountInfo merchantAccountInfo = MerchantUtils.getMerchant();
			Map<String,Object> param =Maps.newHashMap();
			param.put("id", id);
			param.put("merchantId", merchantAccountInfo.getUuid());
			MerchantPlaceAccountBindbank selectByPrimaryKey = merchantPlaceAccountBindbankMapper.selectMerchantBindBankByID(param);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public PageList<MerchantPlaceAccountBindbankVO> listMerchantBindBank(PageBounds pageBounds,
			MerchantPlaceAccountBindbankVO param) throws ServiceException{
		try {
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			param.setMerchantId(merchant.getUuid());
			return merchantPlaceAccountBindbankMapper.listMerchantBindBank(pageBounds,param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public void reverseBankStatus() throws ServiceException {
		try {
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			merchantPlaceAccountBindbankMapper.reverseBankStatus(merchant.getUuid());
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
