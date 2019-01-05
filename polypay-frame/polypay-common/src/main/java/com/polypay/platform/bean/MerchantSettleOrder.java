package com.polypay.platform.bean;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantSettleOrder {
    private Integer id;

    private String orderNumber;

    private BigDecimal postalAmount;

    private BigDecimal serviceAmount;

    private BigDecimal arrivalAmount;

    private Integer merchantBindBankId;

    private Date createTime;

    private Date payTime;

    private Integer status;

    private Integer type;

    private Integer descreption;

    private String handlePeople;

    private String merchantId;

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

    public Integer getMerchantBindBankId() {
        return merchantBindBankId;
    }

    public void setMerchantBindBankId(Integer merchantBindBankId) {
        this.merchantBindBankId = merchantBindBankId;
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
}