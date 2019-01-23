package com.polypay.platform.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.consts.MerchantHelpPayConsts;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@Controller
public class MerchantAccountInfoController {

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;

	@RequestMapping("merchant/accountinfo")
	public String getMerchantAccountInfo(Map<String, Object> result) throws ServiceException {
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		MerchantAccountInfoVO param = new MerchantAccountInfoVO();
		param.setUuid(merchant.getUuid());
		MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(param);
		result.put("merchantAccount", merchantInfoByUUID);
		return "admin/merchantaccountinfo";
	}

	@RequestMapping("merchant/accountinfo/update")
	@ResponseBody
	public String updateMerchantAccountInfo(MerchantAccountInfo uMerchant) throws ServiceException {
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();

		uMerchant.setId(merchant.getId());

		if ("on".equals(uMerchant.getHelppayoff())) {
			uMerchant.setHelppayStatus(MerchantHelpPayConsts.OPEN_HELP_PAY);
		} else {
			uMerchant.setHelppayStatus(MerchantHelpPayConsts.CLOSE_HELP_PAY);
		}

		merchantAccountInfoService.updateByPrimaryKeySelective(uMerchant);

		return "success";
	}

}
