package com.polypay.platform.bean;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantSettleOrder {
    private Integer id;

    private String orderNumber;

    private BigDecimal postalAmount;

    private BigDecimal serviceAmount;

    private BigDecimal arrivalAmount;

    private String merchantBindBank;

    private Date createTime;

    private Date payTime;

    private Integer status;

    private Integer type;

    private Integer descreption;

    private String handlePeople;

    private String merchantId;

    private String bankName;

    private String accountName;

    private String accountCity;

    private String accountProvice;

    private String bankCode;

    private String tradeType;
    
    private String branchBankName;

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

    public String getMerchantBindBank() {
        return merchantBindBank;
    }

    public void setMerchantBindBank(String merchantBindBank) {
        this.merchantBindBank = merchantBindBank == null ? null : merchantBindBank.trim();
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getAccountCity() {
        return accountCity;
    }

    public void setAccountCity(String accountCity) {
        this.accountCity = accountCity == null ? null : accountCity.trim();
    }

    public String getAccountProvice() {
        return accountProvice;
    }

    public void setAccountProvice(String accountProvice) {
        this.accountProvice = accountProvice == null ? null : accountProvice.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

	public String getBranchBankName() {
		return branchBankName;
	}

	public void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}
}