package com.polypay.platform.dao;

import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantAccountBindbank;
import com.polypay.platform.vo.MerchantAccountBindbankVO;

public interface MerchantAccountBindbankMapper extends BaseMapper<MerchantAccountBindbank> {

	MerchantAccountBindbank selectMerchantBindBankByID(Map<String, Object> param);

	PageList<MerchantAccountBindbankVO> listMerchantBindBank(PageBounds pageBounds, MerchantAccountBindbankVO param);

	void reverseBankStatus(String uuid);

}