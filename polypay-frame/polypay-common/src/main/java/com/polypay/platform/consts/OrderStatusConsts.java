package com.polypay.platform.consts;

public interface OrderStatusConsts {
	
	/**
	 * 成功
	 */
	int SUCCESS = 0;
	/**
	 * 提交
	 */
	int SUBMIT = 1;
	/**
	 *  失败
	 */
	int FAIL = -1;
	/**
	 * 删除
	 */
	int DELETE = -3;
	/**
	 * 取消
	 */
	int CANCEL = -4;
	
	/**
	 *  处理中
	 */
	int HANDLE = 2;
}
