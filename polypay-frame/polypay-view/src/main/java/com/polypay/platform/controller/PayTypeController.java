package com.polypay.platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.PayType;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IPayTypeService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@Controller
@RequestMapping("paytype")
public class PayTypeController {
	
	@Autowired
	private IPayTypeService payTypeService;
	
	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;
	
	
	@RequestMapping("list")
	@ResponseBody
	public ServiceResponse listPayType() throws ServiceException
	{
		ServiceResponse response = new ServiceResponse();
		MerchantAccountInfo merchant = MerchantUtils.getMerchant();
		List<PayType> list = payTypeService.listPayType(merchant.getPayLevel());
		response.setData(list);
		
		return response;
	}
	
	@RequestMapping("paytype_add")
	public String addView() {
		return "adminmanager/paytype/paytype_add";
	}
	
	@RequestMapping("add")
	@ResponseBody
	public ServiceResponse add(PayType payType){
		ServiceResponse response = new ServiceResponse();
		try {
			payTypeService.insert(payType);
			response.setMessage("新增成功！！");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			response.setStatus(RequestStatus.FAILED.getStatus());
			response.setMessage("新增失败！！");
		}
		return response;
	}
	
	@RequestMapping("update")
	@ResponseBody
	public ServiceResponse update(PayType payType) throws ServiceException {
		ServiceResponse response = new ServiceResponse();
		int insert = payTypeService.updateByPrimaryKey(payType);
		if(insert > 0) {
			response.setMessage("修改成功！！");
		}else {
			response.setStatus(RequestStatus.FAILED.getStatus());
			response.setMessage("修改失败！！");
		}
		return response;
	}
	
	@RequestMapping("paytype_update/{id}")
	public String updateById(@PathVariable Integer id,ModelMap modelMap) throws ServiceException {
		PayType payType = payTypeService.selectByPrimaryKey(id);
		
		MerchantAccountInfoVO merchantAccountInfoVO = new MerchantAccountInfoVO();
		merchantAccountInfoVO.setUuid(payType.getMerchantId());
		MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantAccountInfoVO);
		payType.setMerchantName(null!=merchantInfoByUUID?merchantInfoByUUID.getAccountName():null);
		modelMap.put("item", payType);
		return "adminmanager/paytype/paytype_edit";
	}
	
	@RequestMapping("delete/{id}")
	@ResponseBody
	public ServiceResponse delete(@PathVariable Integer id) throws ServiceException {
		ServiceResponse response = new ServiceResponse();
		PayType payType = payTypeService.selectByPrimaryKey(id);
		payType.setStatus(1);
		int updateByPrimaryKey = payTypeService.updateByPrimaryKey(payType);
		if(updateByPrimaryKey > 0) {
			response.setMessage("删除成功！！");
		}else {
			response.setStatus(RequestStatus.FAILED.getStatus());
			response.setMessage("删除失败！！");
		}
		return response;
	}
	
}
