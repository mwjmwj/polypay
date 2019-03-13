package com.polypay.platform.pay.common;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
	private String payReqUrl;
	private String payH5ReqUrl;
	private String payCashierUrl;
	private String payH5CashierUrl;
	private String orderQueryUrl;
	private String refundUrl;
	private String channelNo;
	private String h5ChannelNo;
	private String channelNo02;
	private String version4Pay;
	private String version4Cashier;
	private String version4Unionpay;
	private String merchantNo;
	private String notifyUrl;
	private String returnUrl;
	
	private static Config config = new Config();
	
	private Config(){
		try {
			PropertiesConfiguration config = new PropertiesConfiguration("serviceconfig.properties");
			this.payReqUrl = config.getString("pay_req_url");
			this.payH5ReqUrl = config.getString("pay_h5_req_url");
			this.orderQueryUrl = config.getString("order_query_url");
			this.refundUrl = config.getString("refund_url");
			this.channelNo = config.getString("channelNo");
			this.h5ChannelNo = config.getString("h5ChannelNo");
			this.channelNo02 = config.getString("channelNo02");
			this.version4Pay = config.getString("version.pay");
			this.version4Cashier = config.getString("version.cashier");
			this.version4Unionpay = config.getString("version.unionpay");
			this.merchantNo = config.getString("merchantNo");
			this.notifyUrl = config.getString("notify_url");
			this.returnUrl = config.getString("return_url");
			this.payCashierUrl = config.getString("pay_cashier_req_url");
			this.payH5CashierUrl = config.getString("pay_h5_cashier_req_url");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Config getInstance(){
		return config;
	}


	public String getPayReqUrl() {
		return payReqUrl;
	}


	public String getOrderQueryUrl() {
		return orderQueryUrl;
	}


	public String getRefundUrl() {
		return refundUrl;
	}


	public String getChannelNo() {
		return channelNo;
	}

	
	
	public String getH5ChannelNo() {
		return h5ChannelNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public String getPayCashierUrl() {
		return payCashierUrl;
	}

	public void setPayCashierUrl(String payCashierUrl) {
		this.payCashierUrl = payCashierUrl;
	}

	public String getPayH5CashierUrl() {
		return payH5CashierUrl;
	}

	public void setPayH5CashierUrl(String payH5CashierUrl) {
		this.payH5CashierUrl = payH5CashierUrl;
	}

	public String getVersion4Pay() {
		return version4Pay;
	}

	public String getVersion4Cashier() {
		return version4Cashier;
	}
	
	public String getVersion4Unionpay() {
		return version4Unionpay;
	}

	public String getPayH5ReqUrl() {
		return payH5ReqUrl;
	}

	public void setPayH5ReqUrl(String payH5ReqUrl) {
		this.payH5ReqUrl = payH5ReqUrl;
	}

	public String getChannelNo02() {
		return channelNo02;
	}

	public void setChannelNo02(String channelNo02) {
		this.channelNo02 = channelNo02;
	}
	
	
}
