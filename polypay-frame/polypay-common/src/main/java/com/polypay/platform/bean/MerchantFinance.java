package com.polypay.platform.bean;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantFinance {
    private Integer id;

    private BigDecimal blanceAmount;

    private BigDecimal fronzeAmount;

    private String merchantId;

    private String payPassword;

    private Date createTime;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBlanceAmount() {
        return blanceAmount;
    }

    public void setBlanceAmount(BigDecimal blanceAmount) {
        this.blanceAmount = blanceAmount;
    }

    public BigDecimal getFronzeAmount() {
        return fronzeAmount;
    }

    public void setFronzeAmount(BigDecimal fronzeAmount) {
        this.fronzeAmount = fronzeAmount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}