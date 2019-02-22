package com.polypay.platform.vo;

import java.util.Date;

import com.polypay.platform.consts.VerifyTypeEnum;

public class MerchantAccountInfoVO extends VO{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8889010967210081001L;

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
    
    private VerifyTypeEnum verifyType;
    
    private String verifyCode;
    
    private String payPassword;
    
    private String newPayPassword;
    
    private String newPassword;
    
    private String confirmPassword;
    
    private String confirmPayPassword;
    
    private Integer roleId;
    
    private Integer rate;
    
    
    public String getNewPayPassword() {
		return newPayPassword;
	}

	public void setNewPayPassword(String newPayPassword) {
		this.newPayPassword = newPayPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public VerifyTypeEnum getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(VerifyTypeEnum verifyType) {
		this.verifyType = verifyType;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

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

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getConfirmPayPassword() {
		return confirmPayPassword;
	}

	public void setConfirmPayPassword(String confirmPayPassword) {
		this.confirmPayPassword = confirmPayPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

}