package com.polypay.platform.dao;

import java.util.Map;

import com.polypay.platform.bean.MerchantAccountBindbank;

public interface MerchantAccountBindbankMapper extends BaseMapper<MerchantAccountBindbank> {

	MerchantAccountBindbank selectMerchantBindBankByID(Map<String, Object> param);

}