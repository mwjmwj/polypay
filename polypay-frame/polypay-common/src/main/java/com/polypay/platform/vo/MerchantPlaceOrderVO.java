package com.polypay.platform.vo;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantPlaceOrderVO extends VO{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8076315251938609030L;

	private Integer id;

    private String orderNumber;

    private String merchantId;

    private BigDecimal payAmount;

    private BigDecimal serviceAmount;

    private BigDecimal arriveAmount;

    private String bankName;

    private String branchName;

    private String bankNumber;

    private String province;

    private String city;

    private Date createTime;

    private Date handlerTime;

    private String handlerName;

    private Integer status;

    private Integer type;

    private String descreption;
    
    private Date beginTime;
    
    private Date endTime;
    
    private Integer merchantPlaceBindBankId;
    
    private String payPassword;
    
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public BigDecimal getArriveAmount() {
        return arriveAmount;
    }

    public void setArriveAmount(BigDecimal arriveAmount) {
        this.arriveAmount = arriveAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName == null ? null : branchName.trim();
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber == null ? null : bankNumber.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getHandlerTime() {
        return handlerTime;
    }

    public void setHandlerTime(Date handlerTime) {
        this.handlerTime = handlerTime;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName == null ? null : handlerName.trim();
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

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption == null ? null : descreption.trim();
    }

	public Integer getMerchantPlaceBindBankId() {
		return merchantPlaceBindBankId;
	}

	public void setMerchantPlaceBindBankId(Integer merchantPlaceBindBankId) {
		this.merchantPlaceBindBankId = merchantPlaceBindBankId;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
  
    
}