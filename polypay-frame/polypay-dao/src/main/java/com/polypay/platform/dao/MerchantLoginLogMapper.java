package com.polypay.platform.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.bean.MerchantLoginLog;
import com.polypay.platform.vo.MerchantLoginLogVO;

public interface MerchantLoginLogMapper extends BaseMapper<MerchantLoginLog> {

	PageList<MerchantLoginLogVO> listMerchantLoginLog(PageBounds pageBounds, MerchantLoginLogVO param);

}