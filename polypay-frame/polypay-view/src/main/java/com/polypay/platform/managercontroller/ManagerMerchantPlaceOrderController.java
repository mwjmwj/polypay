package com.polypay.platform.managercontroller;

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
import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantPlaceAccountBindbank;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.consts.MerchantOrderTypeConsts;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.utils.DateUtil;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.utils.RandomUtils;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

@Controller
public class ManagerMerchantPlaceOrderController extends BaseController<MerchantPlaceOrderVO> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantPlaceOrderController.class);

	@Autowired
	private IMerchantPlaceOrderService merchantPlaceOrderService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	private ExecutorService executorService = Executors.newFixedThreadPool(5);

	@RequestMapping("/merchantmanager/place/order/list")
	@ResponseBody
	public ServiceResponse listMerchantPlaceOrder() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			MerchantPlaceOrderVO merchantPlaceOrderVO = new MerchantPlaceOrderVO();
			PageList<MerchantPlaceOrderVO> pageList = null;

			merchantPlaceOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));

			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");

			Date[] datas;
			if (!StringUtils.isEmpty(createTime)) {
				datas = DateUtils.changeDate(createTime);
				merchantPlaceOrderVO.setcBeginTime(datas[0]);
				merchantPlaceOrderVO.setcEndTime(datas[1]);
			}

			if (!StringUtils.isEmpty(successTime)) {
				datas = DateUtils.changeDate(successTime);
				merchantPlaceOrderVO.setsBeginTime(datas[0]);
				merchantPlaceOrderVO.setsEndTime(datas[1]);
			}

			String status = getRequest().getParameter("status");

			if (!StringUtils.isEmpty(status)) {
				merchantPlaceOrderVO.setStatus(Integer.parseInt(status));
			}

			pageList = merchantPlaceOrderService.listMerchantPlaceOrder(pageBounds, merchantPlaceOrderVO);
			Page<MerchantPlaceOrderVO> pageData = getPageData(pageList);
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

	@GetMapping("managermerchant/place/all")
	@ResponseBody
	public ServiceResponse allMerchantPlace() throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantMainDateVO merchantGroupDate = merchantPlaceOrderService.managerAllMerchantPlaceOrder();
		response.setData(merchantGroupDate);

		return response;
	}

	@RequestMapping("merchantmanager/place/query")
	public String getMerchantAccountInfo(@RequestParam("id") Integer id, Map<String, Object> result)
			throws ServiceException {

		MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService.selectByPrimaryKey(id);
		result.put("placeorder", selectByPrimaryKey);

		return "adminmanager/managermerchantplaceaudit";

	}

	@RequestMapping("merchantmanager/placeorder/audit")
	@ResponseBody
	public ServiceResponse placeOrder(MerchantPlaceOrderVO merchantplaceOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService
				.selectByPrimaryKey(merchantplaceOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		// 异步执行该订单修改该订单状态
		executorService.execute(() -> submitPlaceOrder(selectByPrimaryKey));

		response.setMessage("提交成功");

		return response;
	}

	@RequestMapping("merchantmanager/placeorder/fail")
	@ResponseBody
	public ServiceResponse failplaceOrder(MerchantPlaceOrderVO merchantplaceOrderVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();

		MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService
				.selectByPrimaryKey(merchantplaceOrderVO.getId());

		if (null == selectByPrimaryKey) {
			ResponseUtils.exception(response, "订单不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
			ResponseUtils.exception(response, "订单状态非待审核!", RequestStatus.FAILED.getStatus());
			return response;
		}

		// 回滚订单
		rollBackPlaceOrder(selectByPrimaryKey);

		response.setMessage("提交成功");

		return response;
	}

	private void submitPlaceOrder(MerchantPlaceOrder merchantPlaceOrder) {

		try {

			MerchantPlaceOrder selectByPrimaryKey;
			synchronized (merchantPlaceOrder.getMerchantId().intern()) {

				selectByPrimaryKey = merchantPlaceOrderService.selectByPrimaryKey(merchantPlaceOrder.getId());

				if (null == selectByPrimaryKey) {
					return;
				}

				if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
					return;
				}

				Thread.currentThread().sleep(5000);

				// 调第三方
				HttpRequestDetailVo httpGet = HttpClientUtil.httpGet("");

				// 处理返回结果
				Map parseObject = JSON.parseObject(httpGet.getResultAsString(), Map.class);
				Object status = parseObject.get("status");

				// 返回结果失败 回滚订单
				if (null == status || !status.toString().equals("200")) {

					// 回滚
					rollBackPlaceOrder(merchantPlaceOrder);
					return;
				}

				// 成功修改订单状态
				merchantPlaceOrder.setStatus(OrderStatusConsts.SUCCESS);
				merchantPlaceOrderService.updateByPrimaryKeySelective(merchantPlaceOrder);
			}
		} catch (Exception e) {
			// 回滚
			try {
				rollBackPlaceOrder(merchantPlaceOrder);
			} catch (ServiceException e1) {
				log.error(" settle order fail: " + merchantPlaceOrder.getMerchantId() + " - "
						+ merchantPlaceOrder.getId());

			}

		}
	}

	// 同步回滚订单
	private void rollBackPlaceOrder(MerchantPlaceOrder merchantPlaceOrder) throws ServiceException {
		synchronized (merchantPlaceOrder.getMerchantId().intern()) {

			MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService
					.selectByPrimaryKey(merchantPlaceOrder.getId());
			if (selectByPrimaryKey.getStatus().equals(OrderStatusConsts.SUBMIT)) {
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

	private MerchantPlaceOrder generatorPlaceOrder(MerchantPlaceOrderVO merchantPlaceOrderVO,
			MerchantPlaceAccountBindbank merchantAccountBindbank) throws ServiceException {

		MerchantPlaceOrder merchantPlaceOrder = new MerchantPlaceOrder();
		String currentOrder = DateUtil.getCurrentDate();
		String orderNumber = "P" + currentOrder + RandomUtils.random(6);
		merchantPlaceOrder.setOrderNumber(orderNumber); // merchantAccountBindbank

		merchantPlaceOrder.setBankName(merchantAccountBindbank.getBankName());
		merchantPlaceOrder.setBankNumber(merchantAccountBindbank.getAccountNumber());
		merchantPlaceOrder.setBranchName(merchantAccountBindbank.getBranchName());

		merchantPlaceOrder.setMerchantId(merchantPlaceOrderVO.getMerchantId());
		merchantPlaceOrder.setStatus(OrderStatusConsts.SUBMIT);
		merchantPlaceOrder.setCreateTime(new Date());
		merchantPlaceOrder.setPayAmount(merchantPlaceOrderVO.getPayAmount());
		merchantPlaceOrder.setType(MerchantOrderTypeConsts.PLACE_ORDER);
		merchantPlaceOrderService.insertSelective(merchantPlaceOrder);

		return merchantPlaceOrder;
	}

}
