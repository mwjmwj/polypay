package com.polypay.platform.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountBindbank;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountBindbankService;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.utils.BankUtils;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantAccountBindbankVO;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@RestController
public class MerchantBindBankController {
	
	@Autowired
	private IMerchantAccountBindbankService merchantAccountBindbankService;
	
	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;
	
	
	@RequestMapping("/merchant/bindbank/save")
	public ServiceResponse saveBindBank(@RequestBody MerchantAccountBindbankVO merchantAccountBindbankVO)throws ServiceException
	{
		ServiceResponse response =  new ServiceResponse();
		String accountNumber = merchantAccountBindbankVO.getAccountNumber();
		
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
		MerchantAccountBindbank merchantAccountBindbank =new MerchantAccountBindbank();
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		BeanUtils.copyProperties(merchantAccountBindbankVO, merchantAccountBindbank);
		merchantAccountBindbank.setMerchantId(merchant.getUuid());
		merchantAccountBindbankService.insertSelective(merchantAccountBindbank);
		response.setMessage("綁定成功");
		return response;
	}
	
	@GetMapping("/merchant/bindbank/getname")
	public ServiceResponse getBankName(@Param("cardnumber") String cardnumber)
	{
		ServiceResponse response =  new ServiceResponse();
		String bankName = BankUtils.getname(cardnumber);
		response.setData(bankName);
		return response;
	}

	
	
}
