package com.polypay.platform.dao;

import java.util.List;
import java.util.Map;

import com.polypay.platform.bean.PayType;

public interface PayTypeMapper extends BaseMapper<PayType>{

	PayType getPayTypeChannel(Map<String, Object> param);

	List<PayType> listPayType(String merchantid);

	List<PayType> list(PayType payType);
}  