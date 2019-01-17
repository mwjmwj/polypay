package com.polypay.platform.bean;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantBill {
    private Integer id;

    private String merchantId;

    private String billName;

    private BigDecimal rechargeAmount;

    private Integer rechargeNumber;

    private BigDecimal rechargeServiceAmount;

    private BigDecimal settleAmount;

    private Integer settleNumber;

    private BigDecimal settleServiceAmount;

    private BigDecimal placeAmount;

    private Integer placeNumber;

    private BigDecimal placeServiceAmount;

    private Date createTime;

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

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName == null ? null : billName.trim();
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(Integer rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public BigDecimal getRechargeServiceAmount() {
        return rechargeServiceAmount;
    }

    public void setRechargeServiceAmount(BigDecimal rechargeServiceAmount) {
        this.rechargeServiceAmount = rechargeServiceAmount;
    }

    public BigDecimal getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(BigDecimal settleAmount) {
        this.settleAmount = settleAmount;
    }

    public Integer getSettleNumber() {
        return settleNumber;
    }

    public void setSettleNumber(Integer settleNumber) {
        this.settleNumber = settleNumber;
    }

    public BigDecimal getSettleServiceAmount() {
        return settleServiceAmount;
    }

    public void setSettleServiceAmount(BigDecimal settleServiceAmount) {
        this.settleServiceAmount = settleServiceAmount;
    }

    public BigDecimal getPlaceAmount() {
        return placeAmount;
    }

    public void setPlaceAmount(BigDecimal placeAmount) {
        this.placeAmount = placeAmount;
    }

    public Integer getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(Integer placeNumber) {
        this.placeNumber = placeNumber;
    }

    public BigDecimal getPlaceServiceAmount() {
        return placeServiceAmount;
    }

    public void setPlaceServiceAmount(BigDecimal placeServiceAmount) {
        this.placeServiceAmount = placeServiceAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}