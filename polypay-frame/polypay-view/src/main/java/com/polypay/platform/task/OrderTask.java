package com.polypay.platform.task;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.polypay.platform.bean.Channel;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.service.IChannelService;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@Component
public class OrderTask {

	private Logger log = LoggerFactory.getLogger(MerchantUnFrezzTask.class);

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

			Map<String, Object> result = paychannel.taskPayOrderNumber(porder.getOrderNumber());

			Object status = result.get("status");

			if (null == status) {
				return;
			}

				// 失敗
				if ("0".equals(status)) {
					rollBackPlaceOrder(porder);
					return;
				}

				if ("1".equals(status)) {
					porder.setStatus(OrderStatusConsts.SUCCESS);
					porder.setArriveAmount(porder.getPayAmount().subtract(porder.getServiceAmount()));
					porder.setHandlerTime(new Date());
					merchantPlaceOrderService.updateByPrimaryKeySelective(porder);
				}
			} catch (ServiceException e) {
			}catch (ClassNotFoundException e) {
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

				Map<String, Object> result = paychannel.taskPayOrderNumber(sorder.getOrderNumber());

				Object status = result.get("status");

				if (null == status) {
					return;
				}

				// 失敗
				if ("0".equals(status.toString())) {
					rollBackSettlerOrder(sorder);
					return;
				}

				if ("1".equals(status.toString())) {
					sorder.setStatus(OrderStatusConsts.SUCCESS);

					sorder.setPayTime(new Date());
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
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
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
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
			}
		}
	}

}
