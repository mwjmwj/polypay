package com.polypay.platform.bean;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantFrezzon {
    private Integer id;

    private String merchantId;

    private String orderNumber;

    private BigDecimal amount;

    private Date arrivalTime;

    private Date frezzTime;

    private Date reallyArrivalTime;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getFrezzTime() {
        return frezzTime;
    }

    public void setFrezzTime(Date frezzTime) {
        this.frezzTime = frezzTime;
    }

    public Date getReallyArrivalTime() {
        return reallyArrivalTime;
    }

    public void setReallyArrivalTime(Date reallyArrivalTime) {
        this.reallyArrivalTime = reallyArrivalTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}