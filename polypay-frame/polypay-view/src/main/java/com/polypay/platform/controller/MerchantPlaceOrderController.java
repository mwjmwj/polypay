package com.polypay.platform.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

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
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantPlaceAccountBindbank;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.SystemConsts;
import com.polypay.platform.consts.MerchantOrderTypeConsts;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.SystemConstans;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantPlaceAccountBindbankService;
import com.polypay.platform.service.IMerchantPlaceOrderService;
import com.polypay.platform.service.ISystemConstsService;
import com.polypay.platform.utils.DateUtil;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.utils.RandomUtils;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantPlaceAccountBindbankVO;
import com.polypay.platform.vo.MerchantPlaceOrderVO;

@Controller
public class MerchantPlaceOrderController extends BaseController<MerchantPlaceOrderVO>{
	
	private Logger log = LoggerFactory.getLogger(MerchantPlaceOrderController.class);

	@Autowired
	private IMerchantPlaceOrderService merchantPlaceOrderService;
	
	@Autowired
	private IMerchantFinanceService merchantFinanceService;
	
	@Autowired
	private IMerchantPlaceAccountBindbankService merchantPlaceAccountBindbankService;
	
	@Autowired
	private ISystemConstsService systemConstsService;
	
	@RequestMapping("/merchant/place/order/list")
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
			if(!StringUtils.isEmpty(createTime))
			{
				datas = DateUtils.changeDate(createTime);
				merchantPlaceOrderVO.setcBeginTime(datas[0]);
				merchantPlaceOrderVO.setcEndTime(datas[1]);
			}
			
			if(!StringUtils.isEmpty(successTime))
			{
				datas = DateUtils.changeDate(successTime);
				merchantPlaceOrderVO.setsBeginTime(datas[0]);
				merchantPlaceOrderVO.setsEndTime(datas[1]);
			}
			
			String status = getRequest().getParameter("status");
			
			if(!StringUtils.isEmpty(status))
			{
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
	
	@RequestMapping("merchant/place/order/pre")
	public String merchantPlaceAmount(Map<String,Object> result) throws ServiceException {
		
		PageBounds pageBounds = this.getPageBounds();
		MerchantPlaceAccountBindbankVO param  = new MerchantPlaceAccountBindbankVO();
		PageList<MerchantPlaceAccountBindbankVO> listMerchantBindBank = merchantPlaceAccountBindbankService.listMerchantBindBank(pageBounds, param);
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchant.getUuid());
		result.put("merchantfinance", merchantFinance);
		result.put("bank", listMerchantBindBank);
		
		return "admin/merchantplacepayorder";
	}
	
	
	@RequestMapping("/merchant/placepay/order")
	@ResponseBody
	public  ServiceResponse settleOrderGenerator(MerchantPlaceOrderVO merchantPlaceOrderVO)
			throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		MerchantPlaceOrder merchantPlaceOrder = null;
		try {

			MerchantAccountInfo merchant = MerchantUtils.getMerchant();
			if (null == merchant) {
				ResponseUtils.exception(response, "未登录", RequestStatus.FAILED.getStatus());
				return response;
			}

			// 行级锁
			synchronized (merchant.getUuid().intern()) {

				MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchant.getUuid());

				if (null == merchantFinance) {
					ResponseUtils.exception(response, "未有財務信息", RequestStatus.FAILED.getStatus());
					return response;
				}

				// 结算必须有结算金额
				BigDecimal settleAmount = merchantPlaceOrderVO.getPayAmount();
				if (null == settleAmount) {
					ResponseUtils.exception(response, "请输入结算金额", RequestStatus.FAILED.getStatus());
					return response;
				}

				// 结算必须有支付密码
				String payPassword = merchantPlaceOrderVO.getPayPassword();
				if (null == payPassword) {
					ResponseUtils.exception(response, "请输入支付密码", RequestStatus.FAILED.getStatus());
					return response;
				}

				String reallyPayPassword = merchantFinance.getPayPassword();

				if (!payPassword.equals(reallyPayPassword)) {
					ResponseUtils.exception(response, "支付密码错误", RequestStatus.FAILED.getStatus());
					return response;
				}

				BigDecimal blanceAmount = merchantFinance.getBlanceAmount();
				if (blanceAmount.compareTo(settleAmount) < 0) {
					ResponseUtils.exception(response, "余额不足", RequestStatus.FAILED.getStatus());
					return response;
				}

				Integer merchantBindBankId = merchantPlaceOrderVO.getMerchantPlaceBindBankId();
				if (null == merchantBindBankId) {
					ResponseUtils.exception(response, "请选择提现银行卡", RequestStatus.FAILED.getStatus());
					return response;
				}

				MerchantPlaceAccountBindbank merchantAccountBindbank = merchantPlaceAccountBindbankService
						.selectMerchantBindBankByID(merchantBindBankId);
				if (null == merchantAccountBindbank) {
					ResponseUtils.exception(response, "银行卡信息缺失", RequestStatus.FAILED.getStatus());
					return response;
				}
				merchantPlaceOrderVO.setMerchantId(merchant.getUuid());
				merchantPlaceOrder = generatorPlaceOrder(merchantPlaceOrderVO,merchantAccountBindbank);
				// 扣除余额
				merchantFinance.setBlanceAmount(blanceAmount.subtract(settleAmount));
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
			}
			//final MerchantPlaceOrder asyncMerchantPlaceOrder = merchantPlaceOrder;

			// 异步发起请求第三方提现
			//executorService.submit(() -> submitPlaceOrder(asyncMerchantPlaceOrder));
			response.setMessage("订单提交成功");
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());

			if (null != merchantPlaceOrder) {
				merchantPlaceOrder.setStatus(OrderStatusConsts.FAIL);
				merchantPlaceOrderService.updateByPrimaryKeySelective(merchantPlaceOrder);
			}

			throw new ServiceException("提现订单生成失败" + e.getMessage(), RequestStatus.FAILED.getStatus());
		}

		return response;

	}
	
	
	
	@GetMapping("merchant/place/query")
	public String queryMerchantPlaceOrder(@RequestParam(name="id") Integer id,Map<String,Object> result) throws ServiceException
	{
		MerchantPlaceOrder selectByPrimaryKey = merchantPlaceOrderService.selectByPrimaryKey(id);
		result.put("placeorder", selectByPrimaryKey);
		return "admin/merchantplaceedit";
	}
	
	
	@GetMapping("merchant/place/all")
	@ResponseBody
	public ServiceResponse allMerchantPlace() throws ServiceException
	{
		ServiceResponse response = new ServiceResponse();
		
		MerchantMainDateVO merchantGroupDate = merchantPlaceOrderService.allMerchantPlace(MerchantUtils.getMerchant().getUuid());
		response.setData(merchantGroupDate);
		
		return response;
	}
	
	
	
	
	private MerchantPlaceOrder generatorPlaceOrder(MerchantPlaceOrderVO merchantPlaceOrderVO, MerchantPlaceAccountBindbank merchantAccountBindbank)
			throws ServiceException {

		MerchantPlaceOrder merchantPlaceOrder = new MerchantPlaceOrder();
		String currentOrder = DateUtil.getCurrentDate();
		String orderNumber = "P" + currentOrder + RandomUtils.random(6);
		merchantPlaceOrder.setOrderNumber(orderNumber); //merchantAccountBindbank

		merchantPlaceOrder.setBankNumber(merchantAccountBindbank.getAccountNumber());
		merchantPlaceOrder.setBankCode(merchantAccountBindbank.getBankCode());
		merchantPlaceOrder.setBankName(merchantAccountBindbank.getBankName());
		merchantPlaceOrder.setBranchBankName(merchantAccountBindbank.getBranchName());
		
		merchantPlaceOrder.setAccountName(merchantAccountBindbank.getAccountName());
		merchantPlaceOrder.setAccountProvice(merchantAccountBindbank.getProvice());
		merchantPlaceOrder.setAccountCity(merchantAccountBindbank.getCity());
		
		merchantPlaceOrder.setMerchantId(merchantPlaceOrderVO.getMerchantId());
		merchantPlaceOrder.setStatus(OrderStatusConsts.SUBMIT);
		merchantPlaceOrder.setCreateTime(new Date());
		merchantPlaceOrder.setPayAmount(merchantPlaceOrderVO.getPayAmount());
		
		SystemConsts consts = systemConstsService.getConsts(SystemConstans.SETTLE_AMOUNT);
		merchantPlaceOrder.setServiceAmount(new BigDecimal(consts.getConstsValue()));
		
		merchantPlaceOrder.setType(MerchantOrderTypeConsts.SETTLE_ORDER);
		
		merchantPlaceOrder.setType(MerchantOrderTypeConsts.PLACE_ORDER);
		merchantPlaceOrderService.insertSelective(merchantPlaceOrder);

		return merchantPlaceOrder;
	}
	
}
