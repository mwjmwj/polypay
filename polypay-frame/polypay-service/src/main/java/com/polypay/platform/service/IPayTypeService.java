package com.polypay.platform.service;

import com.polypay.platform.bean.PayType;
import com.polypay.platform.exception.ServiceException;

public interface IPayTypeService extends IBaseService<PayType>{

	PayType getRateByLevelAndChannel(Integer payLevel, String payChannel) throws ServiceException;

}