package com.polypay.platform.bean;

public class Channel {
    private Integer id;

    private String name;

    private String bean;

    private String callBack;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean == null ? null : bean.trim();
    }

    public String getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack = callBack == null ? null : callBack.trim();
    }
}