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

import com.alibaba.druid.support.json.JSONUtils;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.utils.MD5;

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

			String baseUrl = "http://api.yundesun.com/apisettlequery?";

//			customerid={value}&serial={value}&reqtime={value}&{apikey}

			String costomerid = "10989";
			String serial = porder.getOrderNumber();
			String reqtime = DateUtils.getOrderTime();
			String apikey = "95de2d2d4b2dc95efb9e9c8981dd7743a110a438";
			StringBuffer signp = new StringBuffer();
			signp.append("customerid=" + costomerid).append("&serial=" + serial).append("&reqtime=" + reqtime)
					.append("&" + apikey);
			String sign = MD5.md5(signp.toString());

			baseUrl += signp.toString() + "&sign=" + sign;

//			{"status":1,"msg":"代付成功","serial":"代付订单号","total_fee":"代付金额"}
//			{"status":2,"msg":"代付处理中"}
//			{"status":0,"msg":"代付失败"}
			HttpRequestDetailVo httpGet = HttpClientUtil.httpGet(baseUrl);

			Map result = (Map) JSONUtils.parse(httpGet.getResultAsString());

			Object status = result.get("status");

			if (null == status) {
				return;
			}

			try {
				// 失敗
				if ("0".equals(status)) {
					rollBackPlaceOrder(porder);
					return;
				}

				if ("1".equals(status)) {
					porder.setStatus(OrderStatusConsts.SUCCESS);
					porder.setArriveAmount(porder.getPayAmount().subtract(porder.getServiceAmount()));
					merchantPlaceOrderService.updateByPrimaryKeySelective(porder);
				}
			} catch (ServiceException e) {
			}
		}

	}

	private void executorSettleOrder(MerchantSettleOrder sorder) {

		synchronized (sorder.getOrderNumber().intern()) {
			String baseUrl = "http://api.yundesun.com/apisettlequery?";

//			customerid={value}&serial={value}&reqtime={value}&{apikey}

			String costomerid = "10989";
			String serial = sorder.getOrderNumber();
			String reqtime = DateUtils.getOrderTime();
			String apikey = "95de2d2d4b2dc95efb9e9c8981dd7743a110a438";
			StringBuffer signp = new StringBuffer();
			signp.append("customerid=" + costomerid).append("&serial=" + serial).append("&reqtime=" + reqtime)
					.append("&" + apikey);
			String sign = MD5.md5(signp.toString());

			baseUrl += signp.toString() + "&sign=" + sign;

//			{"status":1,"msg":"代付成功","serial":"代付订单号","total_fee":"代付金额"}
//			{"status":2,"msg":"代付处理中"}
//			{"status":0,"msg":"代付失败"}
			HttpRequestDetailVo httpGet = HttpClientUtil.httpGet(baseUrl);

			Map result = (Map) JSONUtils.parse(httpGet.getResultAsString());

			Object status = result.get("status");

			if (null == status) {
				return;
			}

			try {
				// 失敗
				if ("0".equals(status.toString())) {
					rollBackSettlerOrder(sorder);
					return;
				}

				if ("1".equals(status.toString())) {
					sorder.setStatus(OrderStatusConsts.SUCCESS);
					
					sorder.setArrivalAmount(sorder.getPostalAmount().subtract(sorder.getServiceAmount()));
					merchantSettleOrderService.updateByPrimaryKeySelective(sorder);
				}
			} catch (ServiceException e) {
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
