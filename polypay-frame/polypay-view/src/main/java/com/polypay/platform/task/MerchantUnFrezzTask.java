package com.polypay.platform.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantFrezzon;
import com.polypay.platform.consts.MerchantFinanceStatusConsts;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantFrezzService;

@Component
public class MerchantUnFrezzTask {
	
	private Logger log = LoggerFactory.getLogger(MerchantUnFrezzTask.class);

	
	@Autowired
	private IMerchantFrezzService merchantFrezzService;
	
	
	private ReentrantLock mainlock = new ReentrantLock();
	
	@Autowired
	private IMerchantFinanceService merchantFinanceService;
	
	@Scheduled(cron="0 */1 * * * ?")
	public void UnFrezzMerchant() throws ServiceException
	{
		log.debug("开始解冻任务");
		List<MerchantFrezzon> frezzList = merchantFrezzService.unFrezzMerchantList();
		final ReentrantLock lock = mainlock;
		lock.lock();
		try {
			List<MerchantFrezzon> needFrezz = Lists.newArrayList();
			frezzList.forEach(frezz->{
				
				if(frezz.getArrivalTime().before(new Date()))
				{
					needFrezz.add(frezz);
				}
				
			});
			
			if(CollectionUtils.isEmpty(needFrezz))
			{
				return;
			}
			
			MerchantFinance merchantFinanceByUUID;
			
			
			for (MerchantFrezzon merchantFrezzon : needFrezz) {
				
				merchantFinanceByUUID = merchantFinanceService.getMerchantFinanceByUUID(merchantFrezzon.getMerchantId());
				
				if(null==merchantFinanceByUUID)
				{
					return;
				}
				
				if(MerchantFinanceStatusConsts.FREEZE == merchantFinanceByUUID.getStatus())
				{
					return;
				}

				merchantFinanceByUUID.setBlanceAmount(merchantFinanceByUUID.getBlanceAmount().add(merchantFrezzon.getAmount()));
				
				BigDecimal subtract = merchantFinanceByUUID.getFronzeAmount().subtract(merchantFrezzon.getAmount());
				if(subtract.compareTo(new BigDecimal(0))<0)
				{
					subtract = new BigDecimal(0);
				}
				
				merchantFinanceByUUID.setFronzeAmount(subtract);
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinanceByUUID);
				
				merchantFrezzon.setStatus(MerchantFinanceStatusConsts.USABLE);
				merchantFrezzon.setReallyArrivalTime(new Date());
				merchantFrezzService.updateByPrimaryKeySelective(merchantFrezzon);
			}
			
		}finally {
			lock.unlock();
		}
		
	
		
	}
	

}
