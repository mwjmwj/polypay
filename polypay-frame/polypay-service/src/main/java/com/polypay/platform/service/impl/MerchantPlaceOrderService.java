package com.polypay.platform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantPlaceOrderMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

@Service
public class MerchantPlaceOrderService implements IMerchantPlaceOrderService {

	@Autowired
	private MerchantPlaceOrderMapper merchantPlaceOrderMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantPlaceOrderMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantPlaceOrder record) throws ServiceException {
		try {
			merchantPlaceOrderMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantPlaceOrder record) throws ServiceException {
		try {
			merchantPlaceOrderMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantPlaceOrder selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantPlaceOrder record) throws ServiceException {
		try {
			merchantPlaceOrderMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantPlaceOrder record) throws ServiceException {
		try {
			merchantPlaceOrderMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.polypay.platform.service.IMerchantPlaceOrderService#listMerchantPlaceOrder(com.github.miemiedev.mybatis.paginator.domain.PageBounds, com.polypay.platform.vo.MerchantPlaceOrderVO)
	 */
	@Override
	public PageList<MerchantPlaceOrderVO> listMerchantPlaceOrder(PageBounds pageBounds,
			MerchantPlaceOrderVO merchantPlaceOrderVO) throws ServiceException {
		PageList<MerchantPlaceOrderVO> result;
		try {
			merchantPlaceOrderVO.setMerchantId(MerchantUtils.getMerchant().getUuid());
			result = merchantPlaceOrderMapper.listMerchantPlaceOrder(pageBounds, merchantPlaceOrderVO);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return result;
	}
	
	@Override
	public List<MerchantBill> getMerchantPlaceMonthBill() throws ServiceException {
		try {
			Map<String,Object> param = Maps.newHashMap();
			param.put("beginTime", DateUtils.getBeforeMonthBegin());
			param.put("endTime", DateUtils.getBeforeMonthEnd());
			return merchantPlaceOrderMapper.getMerchantPlaceMonthBill(param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
