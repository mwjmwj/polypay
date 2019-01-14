package com.polypay.platform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantRechargeOrderMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;

@Service
public class MerchantRechargeOrderService implements IMerchantRechargeOrderService {

	@Autowired
	private MerchantRechargeOrderMapper merchantRechargeOrderMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantRechargeOrderMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantRechargeOrder record) throws ServiceException {
		try {
			merchantRechargeOrderMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantRechargeOrder record) throws ServiceException {
		try {
			merchantRechargeOrderMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantRechargeOrder selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantRechargeOrder selectByPrimaryKey = merchantRechargeOrderMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantRechargeOrder record) throws ServiceException {
		try {
			merchantRechargeOrderMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantRechargeOrder record) throws ServiceException {
		try {
			merchantRechargeOrderMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public PageList<MerchantRechargeOrderVO> listMerchantRechargeOrder(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantRechargeOrderVO) throws ServiceException {
		PageList<MerchantRechargeOrderVO> result;
		try {
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			merchantRechargeOrderVO.setMerchantId(merchant.getUuid());
			result = merchantRechargeOrderMapper.listMerchantRechargeOrder(pageBounds, merchantRechargeOrderVO);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return result;
	}

	@Override
	public MerchantRechargeOrder getOrderByMerchantOrderNumber(String merchantOrderNumber) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.getOrderByMerchantOrderNumber(merchantOrderNumber);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantRechargeOrder getOrderByOrderNumber(String merchantOrderNumber) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.getOrderByOrderNumber(merchantOrderNumber);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantMainDateVO getMerchantGroupDate(String merchantUUID) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.getMerchantGroupDate(merchantUUID);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<MerchantMainDateVO> allTimeMerchantOrder(String merchantUUID) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.allTimeMerchantOrder(merchantUUID);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
