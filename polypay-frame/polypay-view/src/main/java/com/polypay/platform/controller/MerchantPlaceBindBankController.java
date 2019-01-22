package com.polypay.platform.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountBindbank;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantPlaceAccountBindbank;
import com.polypay.platform.consts.MerchantBindBankConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantPlaceAccountBindbankService;
import com.polypay.platform.utils.BankUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantPlaceAccountBindbankVO;

@Controller
public class MerchantPlaceBindBankController extends BaseController<MerchantPlaceAccountBindbankVO>{
	
	
	private Logger log = LoggerFactory.getLogger(MerchantPlaceBindBankController.class);
	
	@Autowired
	private IMerchantPlaceAccountBindbankService merchantPlaceAccountBindbankService;
	
	@RequestMapping("/merchant/placebindbank/save")
	@ResponseBody
	public ServiceResponse saveBindBank(MerchantPlaceAccountBindbankVO merchantPlaceAccountBindbankVO)throws ServiceException
	{
		ServiceResponse response =  new ServiceResponse();
		String accountNumber = merchantPlaceAccountBindbankVO.getAccountNumber();
		accountNumber = accountNumber.replaceAll(" ", "");
		merchantPlaceAccountBindbankVO.setAccountNumber(accountNumber);
		if(StringUtils.isEmpty(accountNumber))
		{
			ResponseUtils.exception(response, "银行卡号不能为空!", RequestStatus.FAILED.getStatus());
			return response;
		}
		
		if(!BankUtils.checkBankCard(accountNumber)) {
			ResponseUtils.exception(response, "银行卡号不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}
		
		MerchantAccountInfo merchantAccountInfo = MerchantUtils.getMerchant();
		// 校验商户是否存在
		if(null==merchantAccountInfo) {
			ResponseUtils.exception(response, "商户不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}
		
		// 保存商戶銀行卡信息
		MerchantPlaceAccountBindbank MerchantPlaceAccountBindbank =new MerchantPlaceAccountBindbank();
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		BeanUtils.copyProperties(merchantPlaceAccountBindbankVO, MerchantPlaceAccountBindbank);
		MerchantPlaceAccountBindbank.setMerchantId(merchant.getUuid());
		
		String defaultFlag = merchantPlaceAccountBindbankVO.getDefaultFlag();
		
		if("on".equals(defaultFlag))
		{
			
			merchantPlaceAccountBindbankService.reverseBankStatus();
			MerchantPlaceAccountBindbank.setDefaultStatus(MerchantBindBankConsts.DEFAULT_BIND_BANK);
		}else
		{
			MerchantPlaceAccountBindbank.setDefaultStatus(MerchantBindBankConsts.NOT_DEFAULT_BIND_BANK);
		}
		
		
		
		merchantPlaceAccountBindbankService.insertSelective(MerchantPlaceAccountBindbank);
		response.setMessage("綁定成功");
		return response;
	}
	
	@GetMapping("/merchant/placebindbank/getname")
	@ResponseBody
	public ServiceResponse getBankName(@Param("cardnumber") String cardnumber)
	{
		ServiceResponse response =  new ServiceResponse();
		String bankName = BankUtils.getname(cardnumber);
		response.setData(bankName);
		return response;
	}

	
	@RequestMapping("/merchant/placebindbank/list")
	@ResponseBody
	public ServiceResponse listMerchantBindBank()
			throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantPlaceAccountBindbankVO> pageList = null;
			MerchantPlaceAccountBindbankVO param = new MerchantPlaceAccountBindbankVO();
			pageList = merchantPlaceAccountBindbankService.listMerchantBindBank(pageBounds, param);
			Page<MerchantPlaceAccountBindbankVO> pageData = getPageData(pageList);
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
	
	@GetMapping("/merchant/placebindbank/bind/{id}")
	@ResponseBody
	public ServiceResponse bindBankName(@PathVariable("id")Integer id) throws ServiceException
	{
		ServiceResponse response =  new ServiceResponse();
		
		MerchantPlaceAccountBindbank selectMerchantBindBankByID = merchantPlaceAccountBindbankService.selectMerchantBindBankByID(id);
		
		if(null==selectMerchantBindBankByID)
		{
			response.setStatus(RequestStatus.FAILED.getStatus());
			response.setMessage("该银行卡不存在");
			return response;
		}
		
		merchantPlaceAccountBindbankService.reverseBankStatus();
		selectMerchantBindBankByID.setDefaultStatus(MerchantBindBankConsts.DEFAULT_BIND_BANK);
		merchantPlaceAccountBindbankService.updateByPrimaryKeySelective(selectMerchantBindBankByID);
		response.setMessage("綁定成功");
		
		return response;
	}

	
	
}
