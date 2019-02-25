package com.polypay.platform.task;

import java.math.BigDecimal;
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
import org.springframework.transaction.annotation.Transactional;

import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantFrezzon;
import com.polypay.platform.bean.MerchantRechargeOrder;
import com.polypay.platform.bean.PayType;
import com.polypay.platform.bean.SystemConsts;
import com.polypay.platform.consts.MerchantFinanceStatusConsts;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.SystemConstans;
import com.polypay.platform.controller.OpenApiController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantFrezzService;
import com.polypay.platform.service.IMerchantRechargeOrderService;
import com.polypay.platform.service.IPayTypeService;
import com.polypay.platform.service.ISystemConstsService;
import com.polypay.platform.utils.DateUtils;

@Component
public class RechargeOrderTask {

	private ReentrantLock mainlock = new ReentrantLock();

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	@Autowired
	private IMerchantRechargeOrderService merchantRechargeOrderService;

	@Autowired
	private IPayTypeService payTypeService;

	private static final String FREZZ_RATE = "FREZZ_RATE";

	private Logger log = LoggerFactory.getLogger(OpenApiController.class);

	@Autowired
	private ISystemConstsService systemConstsService;

	@Autowired
	private IMerchantFrezzService merchantFrezzService;

	@Scheduled(cron = "0 */1 * * * ?")
	public void executor() {
		final ReentrantLock lock = mainlock;
		lock.lock();

		try {

			List<MerchantRechargeOrder> recharges = merchantRechargeOrderService.listHandleOrder();
			if (!CollectionUtils.isEmpty(recharges)) {
				recharges.forEach(rorder -> {
					try {
						executorRechargeOrder(rorder);
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| ServiceException e) {
						e.printStackTrace();
					}
				});
			}

		} catch (ServiceException e1) {
			e1.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private void executorRechargeOrder(MerchantRechargeOrder rorder)
			throws ServiceException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		synchronized (rorder.getOrderNumber().intern()) {

			SystemConsts consts = systemConstsService.getConsts(SystemConstans.SMART_RECHARGE_BEAN);

			String constsValue = consts.getConstsValue();

			Class<?> payBean = Class.forName(constsValue);

			IPayChannel paychannel = (IPayChannel) payBean.newInstance();

			Map<String, Object> order = paychannel.getOrder(rorder.getOrderNumber());

			// {"status":1,"msg":"成功订单","sdorderno":"商户订单号","total_fee":"订单金额","sdpayno":"平台订单号"}
			// {"status":0,"msg":"失败订单"}

			// 失败订单 直接回调商户 并修改订单状态
			if (!"1".equals(order.get("status"))) {
				if (!(OrderStatusConsts.SUBMIT == rorder.getStatus().intValue())) {
					return;
				}
				rorder.setStatus(OrderStatusConsts.FAIL);
				merchantRechargeOrderService.updateByPrimaryKeySelective(rorder);
				return;
			}

			String merchantId = rorder.getMerchantId();

			// 原有资金
			BigDecimal resourceAmount;
			// 原冻结资金
			BigDecimal resourceFrezzAmount;

			// 订单金额
			BigDecimal payAmount;
			// 费率
			Double rate;
			// 手续费
			BigDecimal poundage;
			// 到账金额
			BigDecimal arrivalAmount;

			// 冻结金额
			BigDecimal frezzAmount;

			MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchantId);

			// 冻结费率
			SystemConsts frezzRate = systemConstsService.getConsts(FREZZ_RATE);
			String frezzrate = frezzRate.getConstsValue();

			if (null == merchantFinance) {
				return;

			}

			resourceAmount = merchantFinance.getBlanceAmount();
			if (null == resourceAmount) {
				resourceAmount = new BigDecimal(0);
			}
			resourceFrezzAmount = merchantFinance.getFronzeAmount();
			if (null == resourceFrezzAmount) {
				resourceFrezzAmount = new BigDecimal(0);
			}

			rate = getRate(rorder);

			payAmount = new BigDecimal(order.get("total_fee").toString());
			rorder.setPayAmount(payAmount);

			poundage = payAmount.multiply(new BigDecimal(rate)).setScale(4, BigDecimal.ROUND_HALF_UP);

			arrivalAmount = payAmount.subtract(poundage);

			frezzAmount = arrivalAmount.multiply(new BigDecimal(frezzrate)).setScale(4, BigDecimal.ROUND_HALF_UP);

			Integer cstatus = rorder.getStatus();
			if (OrderStatusConsts.SUBMIT == cstatus) {
				// 统一入库
				endInsertOrder(rorder, resourceAmount, resourceFrezzAmount, poundage, arrivalAmount, frezzAmount,
						merchantFinance);
			}

		}
	}

	private Double getRate(MerchantRechargeOrder orderByMerchantOrderNumber) throws ServiceException {

		PayType payType = payTypeService.getPayTypeChannel(orderByMerchantOrderNumber.getMerchantId(), "WY");
		if (null == payType) {
			return 0.02;
		}
		return payType.getRate() / 1000.0;
	}

	@Transactional(rollbackFor = Exception.class)
	private void endInsertOrder(MerchantRechargeOrder orderByMerchantOrderNumber, BigDecimal resourceAmount,
			BigDecimal resourceFrezzAmount, BigDecimal poundage, BigDecimal arrivalAmount, BigDecimal frezzAmount,
			MerchantFinance merchantFinance) throws ServiceException {

		try {
			// 修改订单入库
			saveRechargeOrder(orderByMerchantOrderNumber, poundage, arrivalAmount);

			MerchantFrezzon merchantFrezzon = new MerchantFrezzon();
			merchantFrezzon.setAmount(frezzAmount);
			merchantFrezzon.setFrezzTime(new Date());
			merchantFrezzon.setArrivalTime(DateUtils.getAfterDayDate("1"));
			merchantFrezzon.setStatus(MerchantFinanceStatusConsts.FREEZE);

			merchantFrezzService.insertSelective(merchantFrezzon);
			merchantFinance.setBlanceAmount(resourceAmount.add(arrivalAmount.subtract(frezzAmount)));
			merchantFinance.setFronzeAmount(resourceFrezzAmount.add(frezzAmount));
			merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
		} catch (Exception e) {
			log.error("save order back error");
		}
	}

	private void saveRechargeOrder(MerchantRechargeOrder orderByMerchantOrderNumber, BigDecimal serviceAmount,
			BigDecimal arrivalAmount) throws ServiceException {
		orderByMerchantOrderNumber.setStatus(OrderStatusConsts.SUCCESS);
		orderByMerchantOrderNumber.setServiceAmount(serviceAmount);
		orderByMerchantOrderNumber.setSuccessTime(new Date());
		orderByMerchantOrderNumber.setArrivalAmount(arrivalAmount);
		merchantRechargeOrderService.updateByPrimaryKeySelective(orderByMerchantOrderNumber);
	}

}
