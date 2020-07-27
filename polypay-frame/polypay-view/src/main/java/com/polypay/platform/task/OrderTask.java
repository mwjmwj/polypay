package com.polypay.platform.task;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.*;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.service.*;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.MD5;
import com.polypay.platform.vo.MerchantAccountInfoVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class OrderTask {

	private Logger log = LoggerFactory.getLogger(OrderTask.class);

	@Autowired
	private IMerchantSettleOrderService merchantSettleOrderService;

	@Autowired
	private IMerchantPlaceOrderService merchantPlaceOrderService;

	private ReentrantLock mainlock = new ReentrantLock();

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	@Autowired
	private IChannelService channelService;
	
	@Autowired
	private IMerchantApiService merchantApiService;

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;

	@Scheduled(cron = "0 */1 * * * ?")
	public void executor() {
		final ReentrantLock lock = mainlock;
		lock.lock();

		try {

			List<MerchantSettleOrder> settlts = merchantSettleOrderService.listHandleOrder();

			List<MerchantPlaceOrder> places = merchantPlaceOrderService.listHandleOrder();

			if (!CollectionUtils.isEmpty(settlts)) {
				settlts.forEach(sorder -> executorSettleOrder(sorder));

			}
			if (!CollectionUtils.isEmpty(places)) {
				places.forEach(porder -> executorPlaceOrder(porder));
			}

			log.debug("结算订单 轮循结束");
//			{"status":1,"msg":"代付成功","serial":"代付订单号","total_fee":"代付金额"}
//			{"status":2,"msg":"代付处理中"}，{"status":0,"msg":"代付失败"}
		} finally {
			lock.unlock();
		}

	}

	private void executorPlaceOrder(MerchantPlaceOrder porder) {

		synchronized (porder.getOrderNumber().intern()) {

			try {
				MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
				merchantInfo.setUuid(porder.getMerchantId());
				MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

				Channel channel = channelService.selectByPrimaryKey(merchantInfoByUUID.getChannelId());

				Class<?> payBean = Class.forName(channel.getBean());
				IPayChannel paychannel = (IPayChannel) payBean.newInstance();

				Map<String, Object> result = paychannel.taskPayOrderNumber(porder.getOrderNumber(),
						porder.getHandlerTime());

				if (null == result) {
					return;
				}

				Object status = result.get("status");

				if (null == status) {
					return;
				}

				// 失敗
				if ("0".equals(status)) {
					
					rollBackPlaceOrder(porder);
					filaPlacePayNotify1(porder);
					return;
				}

				if ("1".equals(status)) {
					porder.setStatus(OrderStatusConsts.SUCCESS);
					porder.setArriveAmount(porder.getPayAmount().subtract(porder.getServiceAmount()));
					porder.setHandlerTime(new Date());
					merchantPlaceOrderService.updateByPrimaryKeySelective(porder);
					filaPlacePayNotify1(porder);
				}
			} catch (ServiceException e) {
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}

	}

	private void executorSettleOrder(MerchantSettleOrder sorder) {

		synchronized (sorder.getOrderNumber().intern()) {
			try {
				MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
				merchantInfo.setUuid(sorder.getMerchantId());
				MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

				Channel channel = channelService.selectByPrimaryKey(merchantInfoByUUID.getChannelId());

				Class<?> payBean = Class.forName(channel.getBean());
				IPayChannel paychannel = (IPayChannel) payBean.newInstance();

				Map<String, Object> result = paychannel.taskPayOrderNumber(sorder.getOrderNumber(),
						sorder.getPayTime());

				if (null == result) {
					return;
				}

				Object status = result.get("status");

				if (null == status) {
					return;
				}

				// 失敗
				if ("0".equals(status.toString())) {
					
					rollBackSettlerOrder(sorder);
					filaPlacePayNotify(sorder);
					
					return;
				}

				if ("1".equals(status.toString())) {
					sorder.setStatus(OrderStatusConsts.SUCCESS);

					sorder.setPayTime(new Date());
					filaPlacePayNotify(sorder);
					sorder.setArrivalAmount(sorder.getPostalAmount().subtract(sorder.getServiceAmount()));
					merchantSettleOrderService.updateByPrimaryKeySelective(sorder);
				}

			} catch (ServiceException e) {
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}
	
	
	private void filaPlacePayNotify(MerchantSettleOrder merchantSettleOrder) throws ServiceException {
		
		
		Map<String, String> param = Maps.newHashMap();
		List<String> keys = Lists.newArrayList();
		
		param.put("merchant_id", merchantSettleOrder.getMerchantId());
		keys.add("merchant_id");
		
		param.put("order_no", merchantSettleOrder.getMerchantOrderNumber());
		keys.add("order_no");
		
		param.put("amount", merchantSettleOrder.getPostalAmount().toString());
		keys.add("amount");
		
		String status = "";
		if(merchantSettleOrder.getStatus().equals(OrderStatusConsts.FAIL))
		{
			status = "fail";
		}
		if(merchantSettleOrder.getStatus().equals(OrderStatusConsts.SUCCESS))
		{
			status = "success";
		}
		
		param.put("status", status);
		keys.add("status");  // 1 支付中   0 成功  -1  失败
		
		param.put("time", System.currentTimeMillis()+"");
		keys.add("time");
		
		MerchantApi merchantApiByUUID = merchantApiService.getMerchantApiByUUID(merchantSettleOrder.getMerchantId());
		
		String signParam = getUrl(param, keys);
		signParam += "&api_key="+merchantApiByUUID.getSecretKey();
		String sign = MD5.md5(signParam);
		
		param.put("sign", sign);
		
		HttpClientUtil.httpPost(merchantSettleOrder.getCallUrl(), null, param);
		
	}
	
	/**
	 *  代付异步
	 * @param merchantSettleOrder
	 * @throws ServiceException
	 */
	private void filaPlacePayNotify1(MerchantPlaceOrder merchantSettleOrder) throws ServiceException {
		
		
		Map<String, String> param = Maps.newHashMap();
		List<String> keys = Lists.newArrayList();
		
		param.put("merchant_id", merchantSettleOrder.getMerchantId());
		keys.add("merchant_id");
		
		param.put("order_no", merchantSettleOrder.getMerchantOrderNumber());
		keys.add("order_no");
		
		param.put("amount", merchantSettleOrder.getPayAmount().toString());
		keys.add("amount");
		
		String status = "";
		if(merchantSettleOrder.getStatus().equals(OrderStatusConsts.FAIL))
		{
			status = "fail";
		}
		if(merchantSettleOrder.getStatus().equals(OrderStatusConsts.SUCCESS))
		{
			status = "success";
		}
		
		param.put("status", status);
		keys.add("status");  // 1 支付中   0 成功  -1  失败
		
		param.put("time", System.currentTimeMillis()+"");
		keys.add("time");
		
		MerchantApi merchantApiByUUID = merchantApiService.getMerchantApiByUUID(merchantSettleOrder.getMerchantId());
		
		String signParam = getUrl(param, keys);
		signParam += "&api_key="+merchantApiByUUID.getSecretKey();
		String sign = MD5.md5(signParam);
		
		param.put("sign", sign);
		
		HttpClientUtil.httpPost(merchantSettleOrder.getCallUrl(), null, param);
		
	}

	
	private String getUrl(Map<String, String> signMap, List<String> keys)
	{
		StringBuffer newSign = new StringBuffer();
		keys.forEach(key -> {
			newSign.append(key).append("=").append(signMap.get(key)).append("&");
		});

		if (StringUtils.isEmpty(newSign.toString())) {
			return null;
		}
		newSign.delete(newSign.length() - 1, newSign.length());
		return newSign.toString();
	}
	


	// 同步回滚订单
	private void rollBackSettlerOrder(MerchantSettleOrder merchantSettleOrder) throws ServiceException {
		synchronized (merchantSettleOrder.getMerchantId().intern()) {

			MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService
					.selectByPrimaryKey(merchantSettleOrder.getId());
			if (selectByPrimaryKey.getStatus().equals(OrderStatusConsts.SUBMIT)
					|| selectByPrimaryKey.getStatus().equals(OrderStatusConsts.HANDLE)) {
				// 回滚金额
				MerchantFinance merchantFinance = merchantFinanceService
						.getMerchantFinanceByUUID(merchantSettleOrder.getMerchantId());
				merchantFinance
						.setBlanceAmount(merchantFinance.getBlanceAmount().add(merchantSettleOrder.getPostalAmount()));
				merchantSettleOrder.setStatus(OrderStatusConsts.FAIL);
				merchantSettleOrderService.updateByPrimaryKeySelective(merchantSettleOrder);
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance,merchantSettleOrder.getPostalAmount(),new BigDecimal(0),"add");

			}
		}
	}

	// 同步回滚订单
	private void rollBackPlaceOrder(MerchantPlaceOrder merchantPlaceOrder) throws ServiceException {
		synchronized (merchantPlaceOrder.getMerchantId().intern()) {

			MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService
					.selectByPrimaryKey(merchantPlaceOrder.getId());
			if (selectByPrimaryKey.getStatus().equals(OrderStatusConsts.SUBMIT)
					|| selectByPrimaryKey.getStatus().equals(OrderStatusConsts.HANDLE)) {
				// 回滚金额
				MerchantFinance merchantFinance = merchantFinanceService
						.getMerchantFinanceByUUID(merchantPlaceOrder.getMerchantId());
				merchantFinance
						.setBlanceAmount(merchantFinance.getBlanceAmount().add(merchantPlaceOrder.getPayAmount()));
				merchantPlaceOrder.setStatus(OrderStatusConsts.FAIL);
				merchantPlaceOrder.setHandlerTime(new Date());
				merchantPlaceOrder.setDescreption("订单提交操作异常！");
				merchantPlaceOrder.setHandlerName("系统");

				merchantPlaceOrderService.updateByPrimaryKeySelective(merchantPlaceOrder);
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance,merchantPlaceOrder.getPayAmount(),new BigDecimal(0),"add");
			}
		}
	}

}
