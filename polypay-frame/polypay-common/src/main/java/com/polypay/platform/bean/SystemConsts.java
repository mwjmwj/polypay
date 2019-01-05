package com.polypay.platform.bean;

import java.util.Date;

public class SystemConsts {
    private Integer id;

    private String constsKey;

    private String constsValue;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConstsKey() {
        return constsKey;
    }

    public void setConstsKey(String constsKey) {
        this.constsKey = constsKey == null ? null : constsKey.trim();
    }

    public String getConstsValue() {
        return constsValue;
    }

    public void setConstsValue(String constsValue) {
        this.constsValue = constsValue == null ? null : constsValue.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}