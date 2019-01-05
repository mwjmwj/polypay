package com.polypay.platform.bean;

import java.util.Date;

import com.polypay.platform.consts.VerifyTypeEnum;

public class MerchantVerify {
    private Integer id;

    private String mobileNumber;

    private String email;

    private String code;

    private VerifyTypeEnum type;

    private Date avaliableTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber == null ? null : mobileNumber.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public VerifyTypeEnum getType() {
        return type;
    }

    public void setType(VerifyTypeEnum type) {
        this.type = type;
    }

    public Date getAvaliableTime() {
        return avaliableTime;
    }

    public void setAvaliableTime(Date avaliableTime) {
        this.avaliableTime = avaliableTime;
    }
}