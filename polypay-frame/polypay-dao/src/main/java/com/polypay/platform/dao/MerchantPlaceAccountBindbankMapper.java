package com.polypay.platform.dao;

import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantPlaceAccountBindbank;
import com.polypay.platform.vo.MerchantPlaceAccountBindbankVO;

public interface MerchantPlaceAccountBindbankMapper extends BaseMapper<MerchantPlaceAccountBindbank> {

	MerchantPlaceAccountBindbank selectMerchantBindBankByID(Map<String, Object> param);

	PageList<MerchantPlaceAccountBindbankVO> listMerchantBindBank(PageBounds pageBounds, MerchantPlaceAccountBindbankVO param);

	void reverseBankStatus(String uuid);

}