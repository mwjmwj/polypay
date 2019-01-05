package com.polypay.platform.dao;

import java.util.Map;

import com.polypay.platform.bean.PayType;

public interface PayTypeMapper extends BaseMapper<PayType>{

	PayType getRateByLevelAndChannel(Map<String, Object> param);

}