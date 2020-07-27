package com.polypay.platform.paychannel;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantChannelAmount;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.utils.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class YUFUPayChannel implements IPayChannel{
    private final static Logger log = LoggerFactory.getLogger(YUFUPayChannel.class);

    public static final String URL = "http://pay.yufupays.com/Pay/GateWay?";
    public static final String METCHANT_NO = "1679";
    public static final String KEY = "cc6c6ce50e7440709883e21f5185ad37";

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


        String device = "PC";


        String callbackurl = "http://www.bigchengs.cn/getway/recharge/back/tong";

        String siginParam = String.format("parter=%s&type=%s&value=%s&orderid=%s&callbackurl=%s",
                parter,type,value,orderid,callbackurl
                );
        String sign = MD5.md5(siginParam+KEY).toLowerCase();
        try {
            log.info(URL+"?"+"&device="+device+siginParam);
            response.sendRedirect(URL+siginParam+"&device="+device+"&sign="+sign);
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

        String dfUrl = "http://pay.yufupays.com/Pay/Distribution";
        TreeMap param = new TreeMap();


        String appid = METCHANT_NO;
        param.put("appid",appid);

        String orderid = selectByPrimaryKey.getOrderNumber();
        param.put("orderid",orderid);

        String amount = selectByPrimaryKey.getPostalAmount().subtract(selectByPrimaryKey.getServiceAmount()).setScale(2).toString();
        param.put("amount",amount);

        String bankcode = selectByPrimaryKey.getBankNo();
        param.put("bankcode",bankcode);

        String payeename = selectByPrimaryKey.getAccountName();
        param.put("payeename",payeename);

        String accountno = selectByPrimaryKey.getMerchantBindBank();
        param.put("accountno",accountno);

        String banktype =  "1";
        param.put("banktype",banktype);

        String province = selectByPrimaryKey.getAccountProvice();
        param.put("province",province);

        String city = selectByPrimaryKey.getAccountCity();
        param.put("city",city);

        String branchbank = selectByPrimaryKey.getBranchBankName();
        param.put("branchbank",branchbank);

        String notifyurl = "http://www.bigchengs.cn/settle/callback/yufu";
        param.put("notifyurl",notifyurl);

        String appkey = "17FB05749C7F4E279362989DF3D052B4";
        param.put("appkey",appkey);

        String sign  = "";

        for (Object o : param.keySet()) {
            sign+= o.toString()+"="+param.get(o)+"&";
        }
        sign = sign.substring(0,sign.length()-1);
        sign = getSha1(sign).toLowerCase();
        sign = MD5.md5(sign).toLowerCase();
        param.put("sign",sign);

        HttpRequestDetailVo rtn = HttpClientUtil.httpPost(dfUrl,null,param);
        Map<String, Object> resultEnd = Maps.newHashMap();
        log.info(rtn.getResultAsString());
        try {
            Map map = JSON.parseObject(rtn.getResultAsString(), Map.class);

            Object obj = map.get("result");

            if(null!= obj && "success".equals(obj.toString())){
                resultEnd.put("status", "1");
            } else {
                resultEnd.put("status", "0");
                resultEnd.put("msg", JSON.toJSONString(map));
            }

        } catch (Exception e) {

        }
        return resultEnd;

    }

    public static String getSha1(String str) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
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
