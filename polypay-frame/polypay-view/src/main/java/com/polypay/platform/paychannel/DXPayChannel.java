package com.polypay.platform.paychannel;

import com.google.common.collect.Maps;
import com.polypay.platform.bean.MerchantChannelAmount;
import com.polypay.platform.bean.MerchantPlaceOrder;
import com.polypay.platform.bean.MerchantSettleOrder;
import com.polypay.platform.pay.util.DateUtil;
import com.polypay.platform.paychannel.rx.ConfigUtils;
import com.polypay.platform.paychannel.rx.MD5Utils;
import com.polypay.platform.paychannel.rx.SSLClient;
import com.polypay.platform.paychannel.rx.SignUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  瑞鑫支付
 */
public class DXPayChannel implements IPayChannel{

    private final static Logger log = LoggerFactory.getLogger(TONGPayChannel.class);
    public static final String METCHANT_NO = "MD202004300000002784";

    @Override
    public void sendRedirect(Map<String, Object> param, HttpServletResponse response, HttpServletRequest request) {

        Map<String, String> para = Maps.newHashMap();

        String TradeType = "bis.pay.submit";
        para.put("TradeType",TradeType);

        String PayType = param.get("pay_channel").toString();
        para.put("PayType",PayType);

        String MerchantID = METCHANT_NO;
        para.put("MerchantID",MerchantID);

        String OrderID = param.get("we_order_number").toString();
        para.put("OrderID",OrderID);

        String Subject = "手机";
        para.put("Subject",Subject);

        String MachineIP = "127.0.0.1";
        para.put("MachineIP",MachineIP);

        String Amt = new BigDecimal(param.get("pay_amount").toString()).multiply(new BigDecimal(100)).setScale(0).toString();
        para.put("Amt",Amt);

        String NotifyUrl = "http://www.bigchengs.cn/getway/recharge/back/rx";
        para.put("NotifyUrl",NotifyUrl);

        String SubmitTime = DateUtil.getTime();
        para.put("SubmitTime",SubmitTime);

        Object BankID = param.get("bank_code");

        String Mode = "1";
        if(null!=BankID)
        {
            para.put("Mode",Mode);
            para.put("BankID",BankID.toString());
        }

//        try {
//            DefaultHttpClient httpClient = new SSLClient();
//            HttpPost postMethod = new HttpPost("https://pay.xxiamenchangmateng.xyz/paygateway/gateway/api/pay");

            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            nvps.add(new BasicNameValuePair("TradeType",TradeType));
            nvps.add(new BasicNameValuePair("PayType",PayType));
            nvps.add(new BasicNameValuePair("SubmitTime",SubmitTime));
            nvps.add(new BasicNameValuePair("MerchantID", MerchantID));
            nvps.add(new BasicNameValuePair("OrderID", OrderID));
            nvps.add(new BasicNameValuePair("Subject", Subject));
            nvps.add(new BasicNameValuePair("MachineIP",MachineIP));
            nvps.add(new BasicNameValuePair("NotifyUrl", NotifyUrl));
            nvps.add(new BasicNameValuePair("Amt", Amt));
        if(null!=BankID) {
            nvps.add(new BasicNameValuePair("Mode", Mode));
            nvps.add(new BasicNameValuePair("BankID", BankID.toString()));
        }
//            nvps.add(new BasicNameValuePair("Sign", SignUtils.signData(nvps)));

//            if(PayType.equals("0202") || PayType.equals("0218")){
//                String m_PayUrl = EntityUtils.toString(new UrlEncodedFormEntity(nvps, "UTF-8"));
//                m_PayUrl = "http://127.0.0.1:8080/java_demo/rockform.jsp?"+m_PayUrl;
//                response.sendRedirect(m_PayUrl);
//            }else {
//                postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
//                HttpResponse resp = httpClient.execute(postMethod);
//                String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
//                System.out.println("响应信息："+str);
//                int statusCode = resp.getStatusLine().getStatusCode();
//                if (200 == statusCode) {
//                    boolean signFlag = SignUtils.verferSignData(str);
//                    if (!signFlag) {
//                        System.out.println("验签失败");
//                        return;
//                    }
//                    System.out.println("验签成功");
//                    JSONObject resObject = ConfigUtils.getJSON(str);
//                    retStatus = resObject.getString("RetStatus");
//                    String urlstr = resObject.has("CodeImgUrl")?resObject.getString("CodeImgUrl"):"";
//                    if (urlstr.trim().length()>0) {
//                        response.sendRedirect(urlstr);
//                    }
//
//                }
//                System.out.println("返回错误码:" + statusCode);
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            para.put("Sign",SignUtils.signData1(nvps));

            if(null!=BankID){

                HttpRequestDetailVo retn  = HttpClientUtil.httpPost("https://ren.xxiamenyuhuidi.top/paygateway/gateway/api/pay",
                        Maps.newHashMap(),para);

                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(retn.getResultAsString());
                request.setAttribute("action", jsonObject.getString("PayUrl"));
            }else{
                String PAY_URL = "https://ren.xxiamenyuhuidi.top/paygateway/gateway/api/pay";
                request.setAttribute("dataMap", para);
                request.setAttribute("action", PAY_URL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    @Override
    public Map<String, Object> checkOrder(HttpServletRequest request) {

        String status = request.getParameter("RetStatus");

        Map<String, Object> result = Maps.newHashMap();
//        if (nowMac.equals(mac)) { //若mac校验匹配
        if ("0".equals(status)) {
            result.put("status", "1");
            result.put("total_fee",new BigDecimal(request.getParameter("Amt")).divide(new BigDecimal(100)).toString());
            result.put("orderno", request.getParameter("OrderID"));
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
        String uri  = "https://ren.xxiamenyuhuidi.top/paygateway/gateway/api/pay";

        Map<String,String> param = Maps.newHashMap();
        String TradeType = "bis.pay.payment";
        param.put("TradeType",TradeType);

        String PayType = "0101";
        param.put("PayType",PayType);

        String PayPWD  = "123456";
        param.put("PayPWD",PayPWD);

        String MerchantID = METCHANT_NO;
        param.put("MerchantID",MerchantID);

        String BankID = selectByPrimaryKey.getBankNo();
        param.put("BankID",BankID);

        String OrderID = selectByPrimaryKey.getOrderNumber();
        param.put("OrderID",OrderID);

        //String BankAccountType = request.getParameter("BankAccountType");
        String BankAccountNo = selectByPrimaryKey.getMerchantBindBank();
        param.put("BankAccountNo",BankAccountNo);

        String BankAccountName = selectByPrimaryKey.getAccountName();
        param.put("BankAccountName",BankAccountName);

        String Province = selectByPrimaryKey.getAccountProvice();
        param.put("Province",Province);

        String City = selectByPrimaryKey.getAccountCity();
        param.put("City",City);

        String BankName = selectByPrimaryKey.getBankName();
        param.put("BankName",BankName);

        String MachineIP = "127.0.0.1";
        param.put("MachineIP",MachineIP);

        String Amt = selectByPrimaryKey.getPostalAmount().subtract(selectByPrimaryKey.getServiceAmount()).multiply(new BigDecimal(100)).setScale(0).toString();
        param.put("Amt",Amt);

        String SubmitTime= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        param.put("SubmitTime",SubmitTime);

        String NotifyUrl= "http://www.bigchengs.cn/settle/callback/rx";
        param.put("NotifyUrl",NotifyUrl);

        //String Summary = request.getParameter("Summary");
        String retStatus = "";
        try {
            DefaultHttpClient httpClient = new SSLClient();
            HttpPost postMethod = new HttpPost("https://ren.xxiamenyuhuidi.top/paygateway/gateway/api/pay");

            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            nvps.add(new BasicNameValuePair("TradeType",TradeType));
            nvps.add(new BasicNameValuePair("PayType",PayType));

            String pwdString = PayPWD;
            nvps.add(new BasicNameValuePair("PayPWD", MD5Utils.makeMD5String(pwdString).toUpperCase()));
            nvps.add(new BasicNameValuePair("MerchantID",MerchantID));
            nvps.add(new BasicNameValuePair("BankID", BankID));
            nvps.add(new BasicNameValuePair("OrderID", OrderID));
            //nvps.add(new BasicNameValuePair("BankAccountType",BankAccountType));
            nvps.add(new BasicNameValuePair("BankAccountNo",BankAccountNo));
            nvps.add(new BasicNameValuePair("BankAccountName",BankAccountName));
            nvps.add(new BasicNameValuePair("Province",Province));
            nvps.add(new BasicNameValuePair("City",City));
            nvps.add(new BasicNameValuePair("BankName",BankName));
            nvps.add(new BasicNameValuePair("MachineIP",MachineIP));
            nvps.add(new BasicNameValuePair("NotifyUrl",NotifyUrl));
            nvps.add(new BasicNameValuePair("Amt",Amt));
            nvps.add(new BasicNameValuePair("SubmitTime",SubmitTime));
            //nvps.add(new BasicNameValueP air("Summary",Summary));
            nvps.add(new BasicNameValuePair("Sign", SignUtils.signData1(nvps)));

            System.out.println(nvps);

            postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse resp = httpClient.execute(postMethod);
            String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
            System.out.println("响应信息："+str);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                boolean signFlag = SignUtils.verferSignData1(str);
                if (!signFlag) {
                    System.out.println("验签失败");
                }
                System.out.println("验签成功");
                JSONObject resObject = ConfigUtils.getJSON(str);
                retStatus = resObject.getString("RetStatus");
            }
            System.out.println("返回错误码:" + statusCode);
            Map<String, Object> resultEnd = Maps.newHashMap();

            if("0".equals(retStatus)){
                //订单提交成功
//            response.sendRedirect(request.getContextPath()+ File.separator+"indextwo.jsp");
                resultEnd.put("status", "1");
            }else{
                resultEnd.put("status", "0");
                resultEnd.put("msg", str);
            }
            return resultEnd;
        } catch (Exception e) {
            e.printStackTrace();
        }

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
