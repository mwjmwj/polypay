package com.polypay.platform.vo;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantRechargeOrderVO extends VO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6702717868548674242L;

	private Integer id;

	private String orderNumber;

	private String merchantOrderNumber;

	private String bankOrderNumber;

	private String merchantId;

	private Integer type;

	private BigDecimal payAmount;

	private BigDecimal serviceAmount;

	private BigDecimal arrivalAmount;

	private Date createTime;

	private Date successTime;

	private String payChannel;

	private String payBank;

	private String descreption;

	private Integer status;

	private Date beginTime;

	private Date endTime;

	private Date cBeginTime;

	private Date cEndTime;

	private Date sBeginTime;

	private Date sEndTime;

	private String tradeType;

	private String accountMobileNumber;

	private String accountBankNumber;

	private String accountName;
	
	private String proxyId;
	
	private String payCode;
	
	private Integer count;
	
	private String cTime;

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getAccountMobileNumber() {
		return accountMobileNumber;
	}

	public void setAccountMobileNumber(String accountMobileNumber) {
		this.accountMobileNumber = accountMobileNumber;
	}

	public String getAccountBankNumber() {
		return accountBankNumber;
	}

	public void setAccountBankNumber(String accountBankNumber) {
		this.accountBankNumber = accountBankNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Date getcBeginTime() {
		return cBeginTime;
	}

	public void setcBeginTime(Date cBeginTime) {
		this.cBeginTime = cBeginTime;
	}

	public Date getcEndTime() {
		return cEndTime;
	}

	public void setcEndTime(Date cEndTime) {
		this.cEndTime = cEndTime;
	}

	public Date getsBeginTime() {
		return sBeginTime;
	}

	public void setsBeginTime(Date sBeginTime) {
		this.sBeginTime = sBeginTime;
	}

	public String getMerchantOrderNumber() {
		return merchantOrderNumber;
	}

	public void setMerchantOrderNumber(String merchantOrderNumber) {
		this.merchantOrderNumber = merchantOrderNumber;
	}

	public String getBankOrderNumber() {
		return bankOrderNumber;
	}

	public void setBankOrderNumber(String bankOrderNumber) {
		this.bankOrderNumber = bankOrderNumber;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber == null ? null : orderNumber.trim();
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId == null ? null : merchantId.trim();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public BigDecimal getArrivalAmount() {
		return arrivalAmount;
	}

	public void setArrivalAmount(BigDecimal arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel == null ? null : payChannel.trim();
	}

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank == null ? null : payBank.trim();
	}

	public String getDescreption() {
		return descreption;
	}

	public void setDescreption(String descreption) {
		this.descreption = descreption == null ? null : descreption.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getsEndTime() {
		return sEndTime;
	}

	public void setsEndTime(Date sEndTime) {
		this.sEndTime = sEndTime;
	}

	public String getProxyId() {
		return proxyId;
	}

	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getcTime() {
		return cTime;
	}

	public void setcTime(String cTime) {
		this.cTime = cTime;
	}
}