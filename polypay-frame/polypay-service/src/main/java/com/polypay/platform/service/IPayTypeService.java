package com.polypay.platform.service;

import java.util.List;

import com.polypay.platform.bean.PayType;
import com.polypay.platform.exception.ServiceException;

public interface IPayTypeService extends IBaseService<PayType>{

	PayType getRateByLevelAndChannel(Integer payLevel, String payChannel) throws ServiceException;

	List<PayType> listPayType(Integer payLevel) throws ServiceException;

	List<PayType> list(PayType payType);
}