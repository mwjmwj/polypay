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
import com.polypay.platform.bean.MerchantAccountBindbank;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.bean.SystemConsts;
import com.polypay.platform.consts.MerchantFinanceStatusConsts;
import com.polypay.platform.consts.MerchantOrderTypeConsts;
import com.polypay.platform.consts.OrderStatusConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.SystemConstans;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountBindbankService;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantSettleOrderService;
import com.polypay.platform.service.ISystemConstsService;
import com.polypay.platform.utils.DateUtil;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.utils.RandomUtils;
import com.polypay.platform.vo.MerchantAccountBindbankVO;
import com.polypay.platform.vo.MerchantMainDateVO;
import com.polypay.platform.vo.MerchantSettleOrderVO;

@Controller
public class MerchantSettleOrderController extends BaseController<MerchantSettleOrderVO> {

	private Logger log = LoggerFactory.getLogger(MerchantSettleOrderController.class);

	@Autowired
	private IMerchantSettleOrderService merchantSettleOrderService;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	@Autowired
	private IMerchantAccountBindbankService merchantAccountBindbankService;
	
	@Autowired
	private ISystemConstsService systemConstsService;

	@RequestMapping("/merchant/settle/order/list")
	@ResponseBody
	public ServiceResponse listMerchantSettleOrder()
			throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantSettleOrderVO> pageList = null;
			
			MerchantSettleOrderVO merchantSettleOrderVO = new MerchantSettleOrderVO();
			
			
			merchantSettleOrderVO.setOrderNumber(getRequest().getParameter("orderNumber"));
			
			String createTime = getRequest().getParameter("beginTime");
			String successTime = getRequest().getParameter("endTime");
			
			Date[] datas;
			if(!StringUtils.isEmpty(createTime))
			{
				datas = DateUtils.changeDate(createTime);
				merchantSettleOrderVO.setcBeginTime(datas[0]);
				merchantSettleOrderVO.setcEndTime(datas[1]);
			}
			
			if(!StringUtils.isEmpty(successTime))
			{
				datas = DateUtils.changeDate(successTime);
				merchantSettleOrderVO.setsBeginTime(datas[0]);
				merchantSettleOrderVO.setsEndTime(datas[1]);
			}
			
				String status = getRequest().getParameter("status");
			
			if(!StringUtils.isEmpty(status))
			{
				merchantSettleOrderVO.setStatus(Integer.parseInt(status));
			}
			
			pageList = merchantSettleOrderService.listMerchantSettleOrder(pageBounds, merchantSettleOrderVO );
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

	@RequestMapping("/merchant/settle/order")
	@ResponseBody
	public  ServiceResponse settleOrderGenerator(MerchantSettleOrderVO merchantSettleOrderVO)
			throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		MerchantSettleOrder merchantSettleOrder = null;
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
				
				if (merchantFinance.getStatus().equals(MerchantFinanceStatusConsts.FREEZE)) {
					ResponseUtils.exception(response, "财务已冻结", RequestStatus.FAILED.getStatus());
					return response;
				}


				// 结算必须有结算金额
				BigDecimal settleAmount = merchantSettleOrderVO.getSettleAmount();
				if (null == settleAmount) {
					ResponseUtils.exception(response, "请输入结算金额", RequestStatus.FAILED.getStatus());
					return response;
				}
				
				if(settleAmount.compareTo(new BigDecimal(10))<0)
				{
					ResponseUtils.exception(response, "最低金额10", RequestStatus.FAILED.getStatus());
					return response;
				}
				
				// 结算必须有支付密码
				String payPassword = merchantSettleOrderVO.getPayPassword();
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
				
				Integer channelId = merchant.getChannelId();
				BigDecimal serviceAmount;
				
				// 新网支付
				if(channelId == 2)
				{
					SystemConsts consts = systemConstsService.getConsts(SystemConstans.SETTLE_AMOUNT);
					serviceAmount = new BigDecimal(consts.getConstsValue());
					
					if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
						ResponseUtils.exception(response, "余额不足", RequestStatus.FAILED.getStatus());
						return response;
					}
					settleAmount = settleAmount.add(serviceAmount);
					merchantSettleOrderVO.setSettleAmount(settleAmount);
				}else
				{
					if (blanceAmount.compareTo(settleAmount) < 0) {
						ResponseUtils.exception(response, "余额不足", RequestStatus.FAILED.getStatus());
						return response;
					}
				}
				

				Integer merchantBindBankId = merchantSettleOrderVO.getMerchantBindBankId();
				if (null == merchantBindBankId) {
					ResponseUtils.exception(response, "请选择提现银行卡", RequestStatus.FAILED.getStatus());
					return response;
				}

				MerchantAccountBindbank merchantAccountBindbank = merchantAccountBindbankService
						.selectMerchantBindBankByID(merchantBindBankId);
				if (null == merchantAccountBindbank) {
					ResponseUtils.exception(response, "银行卡信息缺失", RequestStatus.FAILED.getStatus());
					return response;
				}
				merchantSettleOrderVO.setMerchantId(merchant.getUuid());
				
				merchantSettleOrder = generatorSettleOrder(merchantSettleOrderVO,merchantAccountBindbank);
				// 扣除余额
				merchantFinance.setBlanceAmount(blanceAmount.subtract(settleAmount));
				merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
			}
			//final MerchantSettleOrder asyncMerchantSettleOrder = merchantSettleOrder;

			// 异步发起请求第三方提现
			//executorService.submit(() -> submitSettleOrder(asyncMerchantSettleOrder));
			response.setMessage("订单提交成功");
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());

			if (null != merchantSettleOrder) {
				merchantSettleOrder.setStatus(OrderStatusConsts.FAIL);
				merchantSettleOrderService.updateByPrimaryKeySelective(merchantSettleOrder);
			}

			throw new ServiceException("提现订单生成失败" + e.getMessage(), RequestStatus.FAILED.getStatus());
		}

		return response;

	}
	
	@RequestMapping("merchant/settle/order/pre")
	public String preSettleOrder(Map<String,Object>result) throws ServiceException
	{
		PageBounds pageBounds = this.getPageBounds();
		MerchantAccountBindbankVO merchantAccountBindbankVO = new MerchantAccountBindbankVO();
		PageList<MerchantAccountBindbankVO> listMerchantBindBank = merchantAccountBindbankService.listMerchantBindBank(pageBounds, merchantAccountBindbankVO);
		
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantFinance merchantFinanceByUUID = merchantFinanceService.getMerchantFinanceByUUID(merchant.getUuid());
		
		result.put("merchantfinance", merchantFinanceByUUID);
		result.put("bank", listMerchantBindBank);
		
		return "admin/merchantsettleapply";
	}
	
	
	@GetMapping("merchant/settle/query")
	public String queryMerchantSettleOrder(@RequestParam(name="id") Integer id,Map<String,Object> result) throws ServiceException
	{
		MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService.selectByPrimaryKey(id);
		result.put("settleorder", selectByPrimaryKey);
		return "admin/merchantsettleedit";
	}
	
	
	
	@GetMapping("merchant/settle/all")
	@ResponseBody
	public ServiceResponse allMerchantRecharge() throws ServiceException
	{
		ServiceResponse response = new ServiceResponse();
		
		MerchantMainDateVO merchantGroupDate = merchantSettleOrderService.allMerchantSettle(MerchantUtils.getMerchant().getUuid());
		response.setData(merchantGroupDate);
		
		return response;
	}
	
	
	

	private MerchantSettleOrder generatorSettleOrder(MerchantSettleOrderVO merchantSettleOrderVO, MerchantAccountBindbank merchantAccountBindbank)
			throws ServiceException {

		MerchantSettleOrder merchantSettleOrder = new MerchantSettleOrder();
		String currentOrder = DateUtil.getCurrentDate();
		String orderNumber = "S" + currentOrder + RandomUtils.random(6);
		merchantSettleOrder.setOrderNumber(orderNumber);

		merchantSettleOrder.setMerchantBindBank(merchantAccountBindbank.getAccountNumber());
		merchantSettleOrder.setBankCode(merchantAccountBindbank.getBankCode());
		merchantSettleOrder.setBankName(merchantAccountBindbank.getBankName());
		merchantSettleOrder.setBranchBankName(merchantAccountBindbank.getBranchName());
		
		merchantSettleOrder.setAccountName(merchantAccountBindbank.getAccountName());
		merchantSettleOrder.setAccountProvice(merchantAccountBindbank.getProvice());
		merchantSettleOrder.setAccountCity(merchantAccountBindbank.getCity());
		
		merchantSettleOrder.setMerchantId(merchantSettleOrderVO.getMerchantId());
		merchantSettleOrder.setStatus(OrderStatusConsts.SUBMIT);
		merchantSettleOrder.setCreateTime(new Date());
		merchantSettleOrder.setPostalAmount(merchantSettleOrderVO.getSettleAmount());
		
		SystemConsts consts = systemConstsService.getConsts(SystemConstans.SETTLE_AMOUNT);
		merchantSettleOrder.setServiceAmount(new BigDecimal(consts.getConstsValue()));
		
		merchantSettleOrder.setType(MerchantOrderTypeConsts.SETTLE_ORDER);
		merchantSettleOrderService.insertSelective(merchantSettleOrder);

		return merchantSettleOrder;
	}

	
}
