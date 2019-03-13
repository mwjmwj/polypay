package com.polypay.platform.bean;

import java.math.BigDecimal;
import java.util.Date;

public class MerchantAccountInfo {
    private Integer id;

    private String uuid;

    private String proxyId;

    private String accountName;

    private String mobileNumber;

    private String passWord;

    private Date createTime;

    private Integer status;

    private String loginIp;

    private Integer helppayStatus;

    private Integer payLevel;
    
    private String token;
    
    private Integer roleId;
    
    private Integer channelId;
    
    private String helppayoff;
    
    private BigDecimal handAmount;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getProxyId() {
        return proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId == null ? null : proxyId.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber == null ? null : mobileNumber.trim();
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord == null ? null : passWord.trim();
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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public Integer getHelppayStatus() {
        return helppayStatus;
    }

    public void setHelppayStatus(Integer helppayStatus) {
        this.helppayStatus = helppayStatus;
    }

    public Integer getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(Integer payLevel) {
        this.payLevel = payLevel;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getHelppayoff() {
		return helppayoff;
	}

	public void setHelppayoff(String helppayoff) {
		this.helppayoff = helppayoff;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public BigDecimal getHandAmount() {
		return handAmount;
	}

	public void setHandAmount(BigDecimal handAmount) {
		this.handAmount = handAmount;
	}
}