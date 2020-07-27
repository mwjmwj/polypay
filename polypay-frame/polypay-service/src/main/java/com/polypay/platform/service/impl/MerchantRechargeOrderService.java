package com.polypay.platform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.RoleConsts;
import com.polypay.platform.dao.MerchantRechargeOrderMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantAllRechargeVO;
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
		PageList<MerchantRechargeOrderVO> result = null;
		try {
			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			
			if (RoleConsts.MERCHANT.equals(merchant.getRoleId())) {
				merchantRechargeOrderVO.setMerchantId(merchant.getUuid());
				result = merchantRechargeOrderMapper.listMerchantRechargeOrder(pageBounds, merchantRechargeOrderVO);
			} else if (RoleConsts.MANAGER.equals(merchant.getRoleId())) {
				result = merchantRechargeOrderMapper.listMerchantRechargeOrder(pageBounds, merchantRechargeOrderVO);
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return result;
	}

	@Override
	public MerchantRechargeOrder getOrderByMerchantOrderNumber(Map<String,Object> param) throws ServiceException {
		try {
			
			return merchantRechargeOrderMapper.getOrderByMerchantOrderNumber(param);
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
			Map<String,Object> param = Maps.newHashMap();
			param.put("merchantUUID", merchantUUID);
			return merchantRechargeOrderMapper.getMerchantGroupDate(param);
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

	@Override
	public List<MerchantBill> getMerchantRechargeMonthBill() throws ServiceException {
		try {
			Map<String,Object> param = Maps.newHashMap();
			param.put("beginTime", DateUtils.getBeforeMonthBegin());
			param.put("endTime", DateUtils.getBeforeMonthEnd());
			return merchantRechargeOrderMapper.getMerchantRechargeMonthBill(param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantMainDateVO managerAllMerchantRechargeOrder() throws ServiceException {
		
		try {
			return merchantRechargeOrderMapper.managerAllMerchantRechargeOrder();
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<MerchantRechargeOrder> listHandleOrder() throws ServiceException {
		try {
			return merchantRechargeOrderMapper.listHandleOrder();
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public PageList<MerchantRechargeOrderVO> listProxyMerchantRechargeOrder(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantRechargeOrderVO) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.listProxyMerchantRechargeOrder(pageBounds,merchantRechargeOrderVO);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantMainDateVO getTodayMerchantOrder(MerchantRechargeOrderVO param) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.getTodayMerchantOrder(param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantMainDateVO sumProxyMerchantRechargeOrder(String uuid) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.sumProxyMerchantRechargeOrder(uuid);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		
	}

	@Override
	public MerchantMainDateVO sumTodayProxyMerchantRechargeOrder(MerchantRechargeOrderVO param) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.sumTodayProxyMerchantRechargeOrder(param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public PageList<MerchantAllRechargeVO> listMerchantrechargeall(PageBounds pageBounds,
			MerchantRechargeOrderVO merchantRechargeOrderVO) throws ServiceException {
		try {
			return merchantRechargeOrderMapper.listMerchantrechargeall(pageBounds,merchantRechargeOrderVO);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
