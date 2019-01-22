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
    
    private Date beginTime;
    
    private Date endTime;
    
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

	public Date getsEndTime() {
		return sEndTime;
	}

	public void setsEndTime(Date sEndTime) {
		this.sEndTime = sEndTime;
	}
}