package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFrezzon;
import com.polypay.platform.dao.MerchantFrezzonMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFrezzService;
import com.polypay.platform.utils.MerchantUtils;

@Service
public class MerchantFrezzService implements IMerchantFrezzService {

	@Autowired
	private MerchantFrezzonMapper merchantFrezzonMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			return merchantFrezzonMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int insert(MerchantFrezzon record) throws ServiceException {
		try {
			return merchantFrezzonMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int insertSelective(MerchantFrezzon record) throws ServiceException {
		try {
			return merchantFrezzonMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public MerchantFrezzon selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			return merchantFrezzonMapper.selectByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantFrezzon record) throws ServiceException {
		try {
			return merchantFrezzonMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public int updateByPrimaryKey(MerchantFrezzon record) throws ServiceException {
		try {
			return merchantFrezzonMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public PageList<MerchantFrezzon> listMerchantFrezz(PageBounds pageBounds, MerchantFrezzon merchantFrezzon)
			throws ServiceException {
		try {
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			merchantFrezzon.setMerchantId(merchant.getUuid());
			return merchantFrezzonMapper.listMerchantFrezz(pageBounds,merchantFrezzon);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

}
