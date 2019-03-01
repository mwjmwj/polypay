package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantApi;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantApiMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantApiService;

@Service
public class MerchantApiService implements IMerchantApiService {

	@Autowired
	private MerchantApiMapper merchantApiMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantApiMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantApi record) throws ServiceException {
		try {
			merchantApiMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantApi record) throws ServiceException {
		try {
			merchantApiMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantApi selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantApi selectByPrimaryKey = merchantApiMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantApi record) throws ServiceException {
		try {
			merchantApiMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantApi record) throws ServiceException {
		try {
			merchantApiMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}
	

	@Override
	public MerchantApi getMerchantApiByUUID(String merchantUUID) throws ServiceException {
		try {
			return merchantApiMapper.getMerchantApiByUUID(merchantUUID);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public void updateMerchantApi(MerchantApi merchantApi) throws ServiceException {
		try {
			merchantApiMapper.updateMerchantApi(merchantApi);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public PageList<MerchantApi> listMerchantApi(PageBounds pageBounds, MerchantApi merchantApi)
			throws ServiceException {
		try {
			return merchantApiMapper.listMerchantApi(pageBounds,merchantApi);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}


}
