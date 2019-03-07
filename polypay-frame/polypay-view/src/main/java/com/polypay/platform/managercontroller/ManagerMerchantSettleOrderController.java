package com.polypay.platform.managercontroller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.Channel;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.SystemConstans;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.service.IChannelService;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.service.ISystemConstsService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantSettleOrderVO;

@Controller
public class ManagerMerchantSettleOrderController extends BaseController<MerchantSettleOrderVO> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantSettleOrderController.class);

	@Autowired
	private IMerchantSettleOrderService merchantSettleOrderService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	@Autowired
	private ISystemConstsService systemConstsService;
	
	
	@Autowired
	private IChannelService channelService;

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;
	

	@RequestMapping("/merchantmanager/settle/order/list")
	@ResponseBody
	public ServiceResponse listMerchantSettleOrder() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantSettleOrderVO> pageList = null;

			MerchantSettleOrderVO merchantSettleOrderVO = new MerchantSettleOrderVO();

			merchantSettleOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));

			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");

			Date[] datas;
			if (!StringUtils.isEmpty(createTime)) {
				datas = DateUtils.changeDate(createTime);
				merchantSettleOrderVO.setcBeginTime(datas[0]);
				merchantSettleOrderVO.setcEndTime(datas[1]);
			}

			if (!StringUtils.isEmpty(successTime)) {
				datas = DateUtils.changeDate(successTime);
				merchantSettleOrderVO.setsBeginTime(datas[0]);
				merchantSettleOrderVO.setsEndTime(datas[1]);
			}

			String status = getRequest().getParameter("status");

			if (!StringUtils.isEmpty(status)) {
				merchantSettleOrderVO.setStatus(Integer.parseInt(status));
			}
			
			String merchantId = getRequest().getParameter("merchantId");
			if (!StringUtils.isEmpty(merchantId)) {
				merchantSettleOrderVO.setMerchantId(merchantId);;
			}

			pageList = merchantSettleOrderService.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO);
			Page<MerchantSettleOrderVO> pageData = getPageData(pageList);
			response = ResponseUtils.buildResult(pageData);
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		} catch (Exception e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		}
		return response;

	}

	@GetMapping("managermerchant/settle/all")
	@ResponseBody
	public ServiceResponse allMerchantRecharge() throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantMainDateVO merchantGroupDate = merchantSettleOrderService.managerAllMerchantSettleOrder();
		response.setData(merchantGroupDate);

		return response;
	}

	@RequestMapping("merchantmanager/settle/query")
	public String getMerchantAccountInfo(@RequestParam("id") Integer id, Map<String, Object> result)
			throws ServiceException {

		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService.selectByPrimaryKey(id);
		result.put("settleorder", selectByPrimaryKey);

		return "adminmanager/managermerchantsettleaudit";

	}

	@RequestMapping("merchantmanager/settleorder/audit")
	@ResponseBody
	public ServiceResponse settleOrder(MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService
				.selectByPrimaryKey(merchantSettleOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		// 异步执行该订单修改该订单状态
		executorService.execute(() -> submitSettleOrder(selectByPrimaryKey));

		response.setMessage("提交成功");

		return response;
	}

	@RequestMapping("merchantmanager/settleorder/fail")
	@ResponseBody
	public ServiceResponse failSettleOrder(MerchantSettleOrderVO merchantSettleOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService
				.selectByPrimaryKey(merchantSettleOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		selectByPrimaryKey.setDescreption("系统审核不通过");
		selectByPrimaryKey.setHandlePeople(MerchantUtils.getMerchant().getAccountName());
		// 回滚订单
		rollBackSettlerOrder(selectByPrimaryKey);

		response.setMessage("提交成功");

		return response;
	}

	private void submitSettleOrder(MerchantSettleOrder merchantSettleOrder) {

		Object status = null;
		try {

			MerchantSettleOrder selectByPrimaryKey;
			synchronized (merchantSettleOrder.getMerchantId().intern()) {

				selectByPrimaryKey = merchantSettleOrderService.selectByPrimaryKey(merchantSettleOrder.getId());

				if (null == selectByPrimaryKey) {
					return;
				}

				if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
					return;
				}

				
				MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
				merchantInfo.setUuid(merchantSettleOrder.getMerchantId());
				MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);
				
				Channel channel = channelService.selectByPrimaryKey(merchantInfoByUUID.getChannelId());
				
				Class<?> payBean = Class.forName(channel.getBean());
				IPayChannel paychannel = (IPayChannel) payBean.newInstance();

				// {"status":1,"msg":"代付申请成功，系统处理中","serial":"代付订单号"}
				// {"status":0,"msg":"代付失败"}

				String settleAmount = systemConstsService.getConsts(SystemConstans.SETTLE_AMOUNT).getConstsValue();
				selectByPrimaryKey.setServiceAmount(new BigDecimal(settleAmount));
				Map<String, Object> result = paychannel.settleOrder(selectByPrimaryKey);

				status = result.get("status");
	

				// 返回结果失败 回滚订单
				if (status.toString().equals("0")) {

					// 回滚
					rollBackSettlerOrder(merchantSettleOrder);
					return;
				}
				
				// 未支付状态返回
				if (status.toString().equals("2")) {
					return;
				}

				// 成功修改订单状态
				merchantSettleOrder.setStatus(OrderStatusConsts.HANDLE);
				
				merchantSettleOrder.setHandlePeople("admin");

				merchantSettleOrderService.updateByPrimaryKeySelective(merchantSettleOrder);
			}

		} catch (Exception e) {
			// 回滚
			try {
				if(null!=status&&"1".equals(status.toString()))
				{
					return;
				}
				rollBackSettlerOrder(merchantSettleOrder);
			} catch (ServiceException e1) {
				log.error(" settle order fail: " + merchantSettleOrder.getMerchantId() + " - "
						+ merchantSettleOrder.getId());

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

}
