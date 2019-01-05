package com.polypay.platform.consts;

public interface MerchantAccountInfoStatusConsts {
	
	/**
	 * 待审核
	 */
	int PRE_AUDIT = 1;
	
	/**
	 * 审核通过
	 */
	int SUCCESS = 0;
	
	/**
	 * 审核失败
	 */
	int FAIL = -1;
	
	/**
	 * 取消
	 */
	int CANCEL = -10;
	
	/**
	 * 删除
	 */
	int DELETE = -20;

}
