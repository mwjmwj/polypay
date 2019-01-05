package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantSettleOrderMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.vo.MerchantSettleOrderVO;

@Service
public class MerchantSettleOrderService implements IMerchantSettleOrderService {

	@Autowired
	private MerchantSettleOrderMapper merchantSettleOrderMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantSettleOrderMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantSettleOrder record) throws ServiceException {
		try {
			merchantSettleOrderMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantSettleOrder record) throws ServiceException {
		try {
			merchantSettleOrderMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantSettleOrder selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantSettleOrder record) throws ServiceException {
		try {
			merchantSettleOrderMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantSettleOrder record) throws ServiceException {
		try {
			merchantSettleOrderMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public PageList<MerchantSettleOrderVO> listMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException {
		PageList<MerchantSettleOrderVO> result;
		try {
			result = merchantSettleOrderMapper.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return result;
	}

}
