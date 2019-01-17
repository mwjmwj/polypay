package com.polypay.platform.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantBill;
import com.polypay.platform.dao.MerchantBillMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.utils.DateUtils;

@Component
public class MerchantBillTask {
	
	private Logger log = LoggerFactory.getLogger(MerchantBillTask.class);
	
	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;
	
	@Autowired
	private IMerchantPlaceOrderService merchantPlaceOrderService;
	
	@Autowired
	private IMerchantSettleOrderService merchantSettleOrderService;

	@Autowired
	private MerchantBillMapper merchantBillMapper;
	
	@Scheduled(cron = "0 0 1 1-5 * ?")
	public void generatorMerchantMonthBill() throws ServiceException {
		
		
		log.info("开始生成月账单");
		//  校验账单是否已经生成
		if(!checkBill())
		{
			
			log.info("月账单已生成");
			return;
		}
		
		List<MerchantBill> merchantRechargeMonthBill = merchantRechargeOrderService.getMerchantRechargeMonthBill();
		List<MerchantBill> merchantPlaceMonthBill = merchantPlaceOrderService.getMerchantPlaceMonthBill();
		List<MerchantBill> merchantSettleMonthBill = merchantSettleOrderService.getMerchantSettleMonthBill();
		
		
		buildBill(merchantRechargeMonthBill,merchantPlaceMonthBill,merchantSettleMonthBill);
		
		
		
		log.info("月账单生成 成功");
		
	}


	@Transactional(rollbackFor=Exception.class)
	private void buildBill(List<MerchantBill> merchantRechargeMonthBill, List<MerchantBill> merchantPlaceMonthBill,
			List<MerchantBill> merchantSettleMonthBill) {
		try {
			
			Map<String,MerchantBill> rechargeMaps = Maps.newHashMap();
			Map<String,MerchantBill> placeMaps = Maps.newHashMap();
			Map<String,MerchantBill> settleMaps = Maps.newHashMap();
			
			
			Set<String> merchantIds = new HashSet<String>();
			merchantRechargeMonthBill.forEach(r->{rechargeMaps.put(r.getMerchantId(), r);merchantIds.add(r.getMerchantId());});
			merchantPlaceMonthBill.forEach(r->{placeMaps.put(r.getMerchantId(), r);merchantIds.add(r.getMerchantId());});
			merchantSettleMonthBill.forEach(r->{settleMaps.put(r.getMerchantId(), r);merchantIds.add(r.getMerchantId());});
			
			List<MerchantBill> needBill = Lists.newArrayList();
		
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, -1);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH )+1;
			
			merchantIds.forEach(uuid->{
				MerchantBill mBill = new MerchantBill();
				mBill.setMerchantId(uuid);
				mBill.setBillName(year+"年"+month+"月账单");
				mBill.setCreateTime(new Date());
				
				MerchantBill merchantBill = rechargeMaps.get(uuid);
				if(null!=merchantBill)
				{
					mBill.setRechargeAmount(merchantBill.getRechargeAmount());
					mBill.setRechargeNumber(merchantBill.getRechargeNumber());
					mBill.setRechargeServiceAmount(merchantBill.getRechargeServiceAmount());
				}
				
				merchantBill = placeMaps.get(uuid);
				if(null!=merchantBill)
				{
					mBill.setPlaceAmount(merchantBill.getPlaceAmount());
					mBill.setPlaceNumber(merchantBill.getPlaceNumber());
					mBill.setPlaceServiceAmount(merchantBill.getPlaceServiceAmount());
				}
				
				merchantBill = settleMaps.get(uuid);
				if(null!=merchantBill)
				{
					mBill.setSettleAmount(merchantBill.getSettleAmount());
					mBill.setSettleNumber(merchantBill.getSettleNumber());
					mBill.setSettleServiceAmount(merchantBill.getSettleServiceAmount());
				}
				
				needBill.add(mBill);
			});
			
			if(CollectionUtils.isEmpty(needBill))
			{
				return;
			}
			
			merchantBillMapper.batchInsert(needBill);
			
		}catch (Exception e) {
		}
		
	}


	private boolean checkBill() {
		
		Map<String,Object> param = Maps.newHashMap();
		param.put("beginTime", DateUtils.getMonthBegin());
		List<MerchantBill> beforeMonthBill = merchantBillMapper.getBeforeMonthBill(param);
		
		if(CollectionUtils.isNotEmpty(beforeMonthBill))
		{
			return false;
		}
		
		return true;
	}

}
