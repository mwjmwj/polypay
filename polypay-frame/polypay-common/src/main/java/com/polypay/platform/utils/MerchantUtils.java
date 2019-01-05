package com.polypay.platform.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.consts.MerchantGlobaKeyConsts;

public class MerchantUtils {
	
	public static MerchantAccountInfo getMerchant()
	{
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return (MerchantAccountInfo) request.getSession().getAttribute(MerchantGlobaKeyConsts.USER);
	}

}
