package com.polypay.platform.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.dao.MerchantFinanceMapper;
import com.polypay.platform.dao.MerchantPlaceOrderMapper;
import com.polypay.platform.dao.MerchantRechargeOrderMapper;
import com.polypay.platform.dao.MerchantSettleOrderMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.vo.MerchantPlaceOrderVO;
import com.polypay.platform.vo.MerchantRechargeOrderVO;
import com.polypay.platform.vo.MerchantSettleOrderVO;

@Service
public class MerchantFinanceService implements IMerchantFinanceService {
	
	private final static String CREATE_TIME = "createTime";
	private final static String AMOUNT = "amount";
	
	private final static String TYPE = "type";
	private final static Integer ADD = 0;
	private final static Integer SUBTRACT = -1;
	private final static String MESSAGE = "message";
	
	
	private final static String ID = "id";
	private final static String ORDER_TYPE = "orderType";
	
	@Autowired
	private MerchantFinanceMapper merchantFinanceMapper;

	@Autowired
	private MerchantRechargeOrderMapper merchantRechargeOrderMapper;

	@Autowired
	private MerchantSettleOrderMapper merchantSettleOrderMapper;

	@Autowired
	private MerchantPlaceOrderMapper merchantPlaceOrderMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantFinanceMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantFinance record) throws ServiceException {
		try {
			merchantFinanceMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantFinance record) throws ServiceException {
		try {
			merchantFinanceMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantFinance selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantFinance selectByPrimaryKey = merchantFinanceMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantFinance record) throws ServiceException {
		try {
			synchronized (record.getMerchantId().intern()) {
				
				merchantFinanceMapper.updateByPrimaryKeySelective(record);
			}
			
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantFinance record) throws ServiceException {
		try {
			merchantFinanceMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantFinance getMerchantFinanceByUUID(String merchantUUID) throws ServiceException {

		try {
			return merchantFinanceMapper.getMerchantFinanceByUUID(merchantUUID);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<Map<String, Object>> listMerchantFinance(String uuid) throws ServiceException {
		PageList<MerchantRechargeOrderVO> merchantRechargeOrders = getMerchantRechargeOrder(uuid);
		PageList<MerchantPlaceOrderVO> merchantPlaceOrders = getMerchantPlaceOrder(uuid);
		PageList<MerchantSettleOrderVO> merchatSettlerOrders = getMerchatSettlerOrder(uuid);

		return buildResult(merchantRechargeOrders, merchantPlaceOrders, merchatSettlerOrders);
	}

	@SuppressWarnings("rawtypes")
	private List<Map<String, Object>> buildResult(PageList<MerchantRechargeOrderVO> merchantRechargeOrders,
			PageList<MerchantPlaceOrderVO> merchantPlaceOrders, PageList<MerchantSettleOrderVO> merchatSettlerOrders) {
		List<Map<String, Object>> result = Lists.newArrayList();
		Map<String,Object> data = null;
		
		
		for (MerchantRechargeOrderVO entry : merchantRechargeOrders) {
			data = Maps.newHashMap();
			data.put(CREATE_TIME, entry.getCreateTime());
			data.put(AMOUNT, entry.getArrivalAmount());
			data.put(MESSAGE, entry.getDescreption());
			data.put(TYPE, ADD);
			data.put(ID, entry.getId());
			data.put(ORDER_TYPE, entry.getType());
			
			result.add(data);
		}
		
		for (MerchantPlaceOrderVO entry : merchantPlaceOrders) {
			data = Maps.newHashMap();
			data.put(CREATE_TIME, entry.getCreateTime());
			data.put(AMOUNT, entry.getPayAmount());
			data.put(MESSAGE, entry.getDescreption());
			data.put(TYPE, SUBTRACT);
			data.put(ID, entry.getId());
			data.put(ORDER_TYPE, entry.getType());
			
			result.add(data);
		}
		
		for (MerchantSettleOrderVO entry : merchatSettlerOrders) {
			data = Maps.newHashMap();
			data.put(CREATE_TIME, entry.getCreateTime());
			data.put(AMOUNT, entry.getPostalAmount());
			data.put(MESSAGE, entry.getDescreption());
			data.put(TYPE, SUBTRACT);
			data.put(ID, entry.getId());
			data.put(ORDER_TYPE, entry.getType());
			
			result.add(data);
		}
		
		Collections.sort(result, new Comparator<Map>() {
			@Override
			public int compare(Map o1, Map o2) {
				Date d1 = (Date) o1.get(CREATE_TIME);
				Date d2 = (Date) o2.get(CREATE_TIME);
				return d2.compareTo(d1);
			}
		});
		
		return result;
	}

	private PageList<MerchantSettleOrderVO> getMerchatSettlerOrder(String uuid) {
		MerchantSettleOrderVO merchantSettleOrderVO = new MerchantSettleOrderVO();
		merchantSettleOrderVO.setMerchantId(uuid);
		merchantSettleOrderVO.setBeginTime(DateUtils.getMonthTime(-1));
		merchantSettleOrderVO.setEndTime(DateUtils.getNewDate());
		merchantSettleOrderVO.setStatus(OrderStatusConsts.SUCCESS);
		return merchantSettleOrderMapper.listMerchantSettleOrder(getPageBound(), merchantSettleOrderVO);
	}

	private PageList<MerchantPlaceOrderVO> getMerchantPlaceOrder(String uuid) {

		MerchantPlaceOrderVO merchantPlaceOrderVO = new MerchantPlaceOrderVO();
		merchantPlaceOrderVO.setMerchantId(uuid);
		merchantPlaceOrderVO.setBeginTime(DateUtils.getMonthTime(-1));
		merchantPlaceOrderVO.setEndTime(DateUtils.getNewDate());
		merchantPlaceOrderVO.setStatus(OrderStatusConsts.SUCCESS);
		return merchantPlaceOrderMapper.listMerchantPlaceOrder(getPageBound(), merchantPlaceOrderVO);
	}

	private PageList<MerchantRechargeOrderVO> getMerchantRechargeOrder(String uuid) {

		MerchantRechargeOrderVO merchantRechargeOrderVO = new MerchantRechargeOrderVO();
		merchantRechargeOrderVO.setMerchantId(uuid);
		merchantRechargeOrderVO.setBeginTime(DateUtils.getMonthTime(-1));
		merchantRechargeOrderVO.setEndTime(DateUtils.getNewDate());
		merchantRechargeOrderVO.setStatus(OrderStatusConsts.SUCCESS);
		return merchantRechargeOrderMapper.listMerchantRechargeOrder(getPageBound(), merchantRechargeOrderVO);
	}

	public PageBounds getPageBound() {
		// 页面上显示的行数
		int defaultRows = 15;
		// 页数
		int defalutPage = 1;
		return new PageBounds(defalutPage, defaultRows, true);
	}

	@Override
	public PageList<MerchantFinance> listMerchantFinance(PageBounds pageBounds, MerchantFinance merchantFinance)
			throws ServiceException {
		try {
			return merchantFinanceMapper.listMerchantFinance(pageBounds, merchantFinance);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantFinance allMerchantFinance() throws ServiceException {
		try {
			return merchantFinanceMapper.managerAllMerchantFinance();
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantFinance allProxyMerchantMerchantFinance(String uuid) throws ServiceException {
		try {
			return merchantFinanceMapper.allProxyMerchantMerchantFinance(uuid);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<MerchantFinance> listFindMerchantFinance(List<String> mids) throws ServiceException {
		try {
			return merchantFinanceMapper.listFindMerchantFinance(mids);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantFinance record, BigDecimal arrivalAmount, BigDecimal frezzAmount,String type)
			throws ServiceException {
		try {
			synchronized (record.getMerchantId().intern()) {
				
				MerchantFinance selectByPrimaryKey = merchantFinanceMapper.selectByPrimaryKey(record.getId());
				if("add".equals(type))
				{
				selectByPrimaryKey.setBlanceAmount(selectByPrimaryKey.getBlanceAmount().add(arrivalAmount).subtract(frezzAmount));
				selectByPrimaryKey.setFronzeAmount(selectByPrimaryKey.getFronzeAmount().add(frezzAmount));
				}
				else if ("del".equals(type))
				{
					selectByPrimaryKey.setBlanceAmount(selectByPrimaryKey.getBlanceAmount().subtract(arrivalAmount));
					
				}
				merchantFinanceMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
			}
			
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}


}
