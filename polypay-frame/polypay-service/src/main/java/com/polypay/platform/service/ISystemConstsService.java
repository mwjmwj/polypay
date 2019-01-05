package com.polypay.platform.service;

import com.polypay.platform.bean.SystemConsts;
import com.polypay.platform.exception.ServiceException;

public interface ISystemConstsService {

	SystemConsts getConsts(String constsKey) throws ServiceException;

}
