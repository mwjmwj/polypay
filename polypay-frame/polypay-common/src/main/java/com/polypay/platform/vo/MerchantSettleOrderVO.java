package com.polypay.platform.vo;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantSettleOrderVO extends VO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5051143707256390947L;

	private Integer id;

	private String orderNumber;

	private BigDecimal postalAmount;

	private BigDecimal serviceAmount;

	private BigDecimal arrivalAmount;

	private String merchantBindBank;
	
	private Integer merchantBindBankId;

	private Date createTime;

	private Date payTime;

	private Integer status;

	private Integer type;

	private Integer descreption;

	private String handlePeople;

	private String merchantId;

	private Date beginTime;

	private Date endTime;

	private BigDecimal settleAmount;

	private String payPassword;
	
	private String bankAccountNumber;
	
	private String bankName;
	
    private Date cBeginTime;
    
    private Date cEndTime;
    
    private Date sBeginTime;
    
    private Date sEndTime;
    
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

	public Date getsEndTime() {
		return sEndTime;
	}

	public void setsEndTime(Date sEndTime) {
		this.sEndTime = sEndTime;
	}

	public BigDecimal getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(BigDecimal settleAmount) {
		this.settleAmount = settleAmount;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
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

	public BigDecimal getPostalAmount() {
		return postalAmount;
	}

	public void setPostalAmount(BigDecimal postalAmount) {
		this.postalAmount = postalAmount;
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

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDescreption() {
		return descreption;
	}

	public void setDescreption(Integer descreption) {
		this.descreption = descreption;
	}

	public String getHandlePeople() {
		return handlePeople;
	}

	public void setHandlePeople(String handlePeople) {
		this.handlePeople = handlePeople == null ? null : handlePeople.trim();
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId == null ? null : merchantId.trim();
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getMerchantBindBank() {
		return merchantBindBank;
	}

	public void setMerchantBindBank(String merchantBindBank) {
		this.merchantBindBank = merchantBindBank;
	}

	public Integer getMerchantBindBankId() {
		return merchantBindBankId;
	}

	public void setMerchantBindBankId(Integer merchantBindBankId) {
		this.merchantBindBankId = merchantBindBankId;
	}
}