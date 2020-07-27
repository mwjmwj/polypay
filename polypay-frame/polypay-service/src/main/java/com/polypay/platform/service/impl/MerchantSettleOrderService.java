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
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.RoleConsts;
import com.polypay.platform.dao.MerchantSettleOrderMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantMainDateVO;
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
		PageList<MerchantSettleOrderVO> result = null;
		try {

			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			if (RoleConsts.MERCHANT.equals(merchant.getRoleId())) {
				merchantSettleOrderVO.setMerchantId(MerchantUtils.getMerchant().getUuid());
				result = merchantSettleOrderMapper.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO);
			} else if (RoleConsts.MANAGER.equals(merchant.getRoleId())) {
				result = merchantSettleOrderMapper.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO);
			}
			else if (RoleConsts.PROXY.equals(merchant.getRoleId())) {
				merchantSettleOrderVO.setMerchantId(MerchantUtils.getMerchant().getUuid());
				result = merchantSettleOrderMapper.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO);
			}

		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return result;
	}

	@Override
	public List<MerchantBill> getMerchantSettleMonthBill() throws ServiceException {
		try {
			Map<String, Object> param = Maps.newHashMap();
			param.put("beginTime", DateUtils.getBeforeMonthBegin());
			param.put("endTime", DateUtils.getBeforeMonthEnd());
			return merchantSettleOrderMapper.getMerchantSettleMonthBill(param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantMainDateVO allMerchantSettle(String uuid) throws ServiceException {
		try {
			return merchantSettleOrderMapper.allMerchantSettle(uuid);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantMainDateVO managerAllMerchantSettleOrder() throws ServiceException {
		try {
			return merchantSettleOrderMapper.managerAllMerchantSettleOrder();
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<MerchantSettleOrder> listHandleOrder() {
		return merchantSettleOrderMapper.listHandleOrder();
	}

	@Override
	public PageList<MerchantSettleOrderVO> listProxyMerchantSettleOrder(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException {
		try {
			return merchantSettleOrderMapper.listProxyMerchantSettleOrder(pageBounds,merchantSettleOrderVO);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantSettleOrder getSettleOrderByOrderNo(String orderNo) throws ServiceException {
		try {
			return merchantSettleOrderMapper.getSettleOrderByOrderNo(orderNo);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}


	@Override
	public PageList<MerchantSettleOrderVO> listMerchantSettleOrder1(PageBounds pageBounds,
			MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException {
		try {
			return merchantSettleOrderMapper.listProxyMerchantSettleOrder1(pageBounds,merchantSettleOrderVO);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
