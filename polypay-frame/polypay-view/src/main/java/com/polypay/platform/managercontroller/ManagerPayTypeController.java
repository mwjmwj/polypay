package com.polypay.platform.managercontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.PayType;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IPayTypeService;

@Controller
@RequestMapping("manager")
public class ManagerPayTypeController {
	
	@Autowired
	private IPayTypeService payTypeService;
	
	
	@RequestMapping("paytype/list")
	@ResponseBody
	public ServiceResponse list(PayType payType) throws ServiceException
	{
		ServiceResponse response = new ServiceResponse();
		
		
		List<PayType> list = payTypeService.list(payType);
		response.setData(list);
		
		return response;
	}
	
}
