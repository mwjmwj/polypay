package com.polypay.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantBillMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantBillService;

@Service
public class MerchantBillService implements IMerchantBillService {

	@Autowired
	private MerchantBillMapper merchantBillMapper;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantBillMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantBill record) throws ServiceException {
		try {
			merchantBillMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantBill record) throws ServiceException {
		try {
			merchantBillMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantBill selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantBill selectByPrimaryKey = merchantBillMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantBill record) throws ServiceException {
		try {
			merchantBillMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantBill record) throws ServiceException {
		try {
			merchantBillMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public PageList<MerchantBill> listMerchantBill(PageBounds pageBounds, MerchantBill merchantBill)
			throws ServiceException {
		try {
			return merchantBillMapper.listMerchantBill(pageBounds,merchantBill);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
