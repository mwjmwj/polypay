package com.polypay.platform.vo;

import java.math.BigDecimal;

public class MerchantMainDateVO extends VO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1726777628442877709L;

	private String merchantTodayOrderNumber;
	
	private String merchantAllOrderNumber;
	
	private BigDecimal merchantTodayRechargeAmount;
	
	private BigDecimal merchantAllRechargeAmount;
	
	private String createTime;
	
	public String getMerchantTodayOrderNumber() {
		return merchantTodayOrderNumber;
	}

	public void setMerchantTodayOrderNumber(String merchantTodayOrderNumber) {
		this.merchantTodayOrderNumber = merchantTodayOrderNumber;
	}

	public String getMerchantAllOrderNumber() {
		return merchantAllOrderNumber;
	}

	public void setMerchantAllOrderNumber(String merchantAllOrderNumber) {
		this.merchantAllOrderNumber = merchantAllOrderNumber;
	}

	public BigDecimal getMerchantTodayRechargeAmount() {
		return merchantTodayRechargeAmount;
	}

	public void setMerchantTodayRechargeAmount(BigDecimal merchantTodayRechargeAmount) {
		this.merchantTodayRechargeAmount = merchantTodayRechargeAmount;
	}

	public BigDecimal getMerchantAllRechargeAmount() {
		return merchantAllRechargeAmount;
	}

	public void setMerchantAllRechargeAmount(BigDecimal merchantAllRechargeAmount) {
		this.merchantAllRechargeAmount = merchantAllRechargeAmount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
