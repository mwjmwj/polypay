package com.polypay.platform.paychannel;

import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantChannelAmount;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.utils.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class TONGPayChannel implements IPayChannel{
    private final static Logger log = LoggerFactory.getLogger(TONGPayChannel.class);

    public static final String URL = "http://gateway.tongfpay.com/Recharge.aspx?";
    public static final String METCHANT_NO = "8847";
    public static final String KEY = "658e437b3a50467c91eadab65c0b0d99";

    @Override
    public void sendRedirect(Map<String, Object> param, HttpServletResponse response, HttpServletRequest request) {

        String parter = METCHANT_NO;
        String type = param.get("pay_channel").toString();

        String amount = new BigDecimal(param.get("pay_amount").toString()).setScale(2).toString();
        String order_amount = amount;
        String value = order_amount;

        String orderId = param.get("we_order_number").toString();//订单id[商户网站]
        String order_no = orderId;
        String orderid = order_no;

        String callbackurl = "http://www.bigchengs.cn/getway/recharge/back/tong";

        String siginParam = String.format("parter=%s&type=%s&value=%s&orderid=%s&callbackurl=%s",
                parter,type,value,orderid,callbackurl
                );
        String sign = MD5.md5(siginParam+KEY).toLowerCase();
        try {
            log.info(URL+siginParam);
            response.sendRedirect(URL+siginParam+"&sign="+sign);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Map<String, Object> checkOrder(HttpServletRequest request) {

        String status = request.getParameter("opstate");


        Map<String, Object> result = Maps.newHashMap();
//        if (nowMac.equals(mac)) { //若mac校验匹配
        if ("0".equals(status)) {
            result.put("status", "1");
            result.put("total_fee", request.getParameter("ovalue"));
            result.put("orderno", request.getParameter("orderid"));
            result.put("channel", "WY");
        } else {
            result.put("status", "-1");
        }

        return result;
    }

    @Override
    public Map<String, Object> checkOrder(MerchantChannelAmount m, HttpServletRequest request) {
        return checkOrder(request);
    }

    @Override
    public Map<String, Object> getOrder(String orderNumber) {
        return null;
    }

    @Override
    public Map<String, Object> settleOrder(MerchantSettleOrder selectByPrimaryKey) throws Exception {
        return null;
    }

    @Override
    public Map<String, Object> placeOrder(MerchantPlaceOrder selectByPrimaryKey) {
        return null;
    }

    @Override
    public Map<String, Object> taskPayOrderNumber(String orderNumber, Date date) {
        return null;
    }
}
