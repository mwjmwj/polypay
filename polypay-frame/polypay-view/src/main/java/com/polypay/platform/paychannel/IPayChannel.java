package com.polypay.platform.paychannel;

import com.polypay.platform.bean.MerchantChannelAmount;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

public interface IPayChannel {

    void sendRedirect(Map<String, Object> param, HttpServletResponse response, HttpServletRequest request);

    Map<String, Object> checkOrder(HttpServletRequest request);

    Map<String, Object> checkOrder(MerchantChannelAmount m , HttpServletRequest request);

    Map<String, Object> getOrder(String orderNumber);

    Map<String, Object> settleOrder(MerchantSettleOrder selectByPrimaryKey) throws Exception;

    Map<String, Object> placeOrder(MerchantPlaceOrder selectByPrimaryKey);

    Map<String, Object> taskPayOrderNumber(String orderNumber, Date date);
}
