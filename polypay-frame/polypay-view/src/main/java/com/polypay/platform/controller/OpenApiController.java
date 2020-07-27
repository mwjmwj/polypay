package com.polypay.platform.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.*;
import com.polypay.platform.consts.*;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.pay.util.http.Httpz;
import com.polypay.platform.paychannel.IPayChannel;
import com.polypay.platform.paychannel.RXPayChannel;
import com.polypay.platform.service.*;
import com.polypay.platform.utils.*;
import com.polypay.platform.vo.MerchantAccountInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class OpenApiController {
    private static final String RECHARGE = "R";
    private Logger log = LoggerFactory.getLogger(OpenApiController.class);

    @Autowired
    private IMerchantAccountInfoService merchantAccountInfoService;

    @Autowired
    private IMerchantRechargeOrderService merchantRechargeOrderService;

    @Autowired
    private IMerchantApiService merchantApiService;

    @Autowired
    private IMerchantFinanceService merchantFinanceService;

    @Autowired
    private IMerchantFrezzService merchantFrezzService;

    @Autowired
    private IPayTypeService payTypeService;

    @Autowired
    private IMerchantPlaceOrderService merchantPlaceOrderService;

    @Autowired
    private IMerchantSettleOrderService merchantSettleOrderService;

    @Autowired
    private IMerchantChannelAmountService merchantChannelAmountService;

    @Autowired
    private IChannelService channelService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    /* 自动下发*/
    private static int OPEN_AUTO = 1;

    /**
     * 适用通道 请求转发通道
     *
     * @param paramMap
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/open/api/recharge")
    @ResponseBody
    public ServiceResponse recharge(Map<String, Object> paramMap, HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException {

        ServiceResponse result = new ServiceResponse();

        try {
            List<String> keys = Lists.newArrayList();

            // 签名校验 map
            Map<String, Object> signMap = Maps.newHashMap();
            String merchantUUID = getParameter(request, "merchant_id");
            signMap.put("merchant_id", merchantUUID);
            if (StringUtils.isEmpty(merchantUUID)) {
                ResponseUtils.exception(result, "商户ID不能为空！", RequestStatus.FAILED.getStatus());
                return result;
            }
            keys.add("merchant_id");

            // 必传商户订单号
            String merchantOrderNumber = getParameter(request, "order_number");
            signMap.put("order_number", merchantOrderNumber);
            if (StringUtils.isEmpty(merchantOrderNumber)) {
                ResponseUtils.exception(result, "订单ID不能为空！", RequestStatus.FAILED.getStatus());
                return result;
            }
            keys.add("order_number");

            // 订单金额必传
            String orderAmount = getParameter(request, "pay_amount");
            signMap.put("pay_amount", orderAmount);
            if (StringUtils.isEmpty(orderAmount)) {
                ResponseUtils.exception(result, "支付金额不能为空！", RequestStatus.FAILED.getStatus());
                return result;
            }
            keys.add("pay_amount");
            try {
                double parseDouble = Double.parseDouble(orderAmount);

                if (parseDouble < 1.0) {
                    ResponseUtils.exception(result, "支付金额必须大于1元！", RequestStatus.FAILED.getStatus());
                    return result;
                }

                if (parseDouble > 100000.0) {
                    ResponseUtils.exception(result, "支付金额上限 10万元！", RequestStatus.FAILED.getStatus());
                    return result;
                }

            } catch (Exception e) {
                ResponseUtils.exception(result, "支付金额填写错误！", RequestStatus.FAILED.getStatus());
                return result;
            }

            String time = getParameter(request, "time");
            signMap.put("time", time);
            if (StringUtils.isEmpty(time)) {
                ResponseUtils.exception(result, "时间撮不能为空！", RequestStatus.FAILED.getStatus());
                return result;
            }
            keys.add("time");

            String payChannel = getParameter(request, "pay_channel");
            signMap.put("pay_channel", payChannel);
            if (StringUtils.isEmpty(payChannel)) {
                ResponseUtils.exception(result, "支付通道不能为空！", RequestStatus.FAILED.getStatus());
                return result;
            }
            keys.add("pay_channel");

            String notify_url = getParameter(request, "notify_url");
            signMap.put("notify_url", notify_url);
            if (StringUtils.isEmpty(notify_url)) {
                ResponseUtils.exception(result, "异步通知地址不能为空！", RequestStatus.FAILED.getStatus());
                return result;
            }
            keys.add("notify_url");

            // 如果是sichuan 银行卡号
            String bank_no = getParameter(request, "bank_no");
            if (!StringUtils.isEmpty(bank_no)) {
                signMap.put("bank_no", bank_no);
                keys.add("bank_no");
            }

            String bank_code = getParameter(request, "bank_code");
            if (!StringUtils.isEmpty(bank_code)) {
                signMap.put("bank_code", bank_code);
                keys.add("bank_code");
            }

            String sign = getParameter(request, "sign");
            if (StringUtils.isEmpty(sign)) {
                ResponseUtils.exception(result, "签名不能为空！", RequestStatus.FAILED.getStatus());
                return result;
            }

            // 校验商户资格
            checkMerchantId(merchantUUID, result);
            if (!RequestStatus.SUCCESS.getStatus().equals(result.getStatus())) {
                return result;
            }

            // 校验商户秘钥
            MerchantApi merchantApi = getMerchantApi(merchantUUID);

            keys.add("api_key");
            signMap.put("api_key", merchantApi.getSecretKey());

            // 校验签名是否正确
            if (!checkSign(signMap, sign, keys)) {
                ResponseUtils.exception(result, "簽名不正确！", RequestStatus.FAILED.getStatus());
                return result;
            }

            // 校验账户财务信息
            checkMerchantFinance(result, merchantUUID);
            if (!RequestStatus.SUCCESS.getStatus().equals(result.getStatus())) {
                return result;
            }

            // 校验订单是否重复
            checkMerchantOrderNumber(result, merchantOrderNumber, merchantUUID);
            if (!RequestStatus.SUCCESS.getStatus().equals(result.getStatus())) {
                return result;
            }

            // 生成订单
            MerchantRechargeOrder generatorOrder = generatorOrder(merchantUUID, signMap);

            // 请求转发到第三方
            sendRediect(response, signMap, generatorOrder, request);

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return result;
        }

        return result;
    }


    /**
     * 适用通道:  支付页面支付
     *
     * @param paramMap
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/open/hfbapi/recharge")
    public String recharge1(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String result = "result";
        ServiceResponse results = new ServiceResponse();

        try {
            List<String> keys = Lists.newArrayList();

            // 签名校验 map
            Map<String, Object> signMap = Maps.newHashMap();
            String merchantUUID = getParameter(request, "merchant_id");
            signMap.put("merchant_id", merchantUUID);
            if (StringUtils.isEmpty(merchantUUID)) {
                request.setAttribute("errorMsg", "缺失商户ID");
                return result;
            }
            keys.add("merchant_id");

            // 必传商户订单号
            String merchantOrderNumber = getParameter(request, "order_number");
            signMap.put("order_number", merchantOrderNumber);
            if (StringUtils.isEmpty(merchantOrderNumber)) {
                request.setAttribute("errorMsg", "缺失订单号");
                return result;
            }
            keys.add("order_number");

            // 订单金额必传
            String orderAmount = getParameter(request, "pay_amount");
            signMap.put("pay_amount", orderAmount);
            if (StringUtils.isEmpty(orderAmount)) {
                request.setAttribute("errorMsg", "缺失金额");
                return result;
            }
            keys.add("pay_amount");
            try {
                double parseDouble = Double.parseDouble(orderAmount);

                if (parseDouble < 1.0) {
                    return result;
                }

                if (parseDouble > 100000.0) {
                    return result;
                }

            } catch (Exception e) {
                return result;
            }

            String time = getParameter(request, "time");
            signMap.put("time", time);
            if (StringUtils.isEmpty(time)) {
                request.setAttribute("errorMsg", "缺失时间");
                return result;
            }
            keys.add("time");

            String payChannel = getParameter(request, "pay_channel");
            signMap.put("pay_channel", payChannel);
            if (StringUtils.isEmpty(payChannel)) {
                request.setAttribute("errorMsg", "缺失通道");
                return result;
            }
            keys.add("pay_channel");

            String notify_url = getParameter(request, "notify_url");
            signMap.put("notify_url", notify_url);
            if (StringUtils.isEmpty(notify_url)) {
                request.setAttribute("errorMsg", "缺失回调");
                return result;
            }
            keys.add("notify_url");

            // 如果是sichuan 银行卡号
            String bank_no = getParameter(request, "bank_no");
            if (!StringUtils.isEmpty(bank_no)) {
                signMap.put("bank_no", bank_no);
                keys.add("bank_no");
            }

            // 如果是sichuan 银行卡号
            String bank_code = getParameter(request, "bank_code");
            if (!StringUtils.isEmpty(bank_code)) {
                signMap.put("bank_code", bank_code);
                keys.add("bank_code");
            }

            String sign = getParameter(request, "sign");
            if (StringUtils.isEmpty(sign)) {
                request.setAttribute("errorMsg", "缺失签名");
                return result;
            }

            // 校验商户资格
            checkMerchantId(merchantUUID, results);
            if (!RequestStatus.SUCCESS.getStatus().equals(results.getStatus())) {
                return result;
            }

            // 校验商户秘钥
            MerchantApi merchantApi = getMerchantApi(merchantUUID);

            keys.add("api_key");
            signMap.put("api_key", merchantApi.getSecretKey());

            // 校验签名是否正确
            if (!checkSign(signMap, sign, keys)) {
                request.setAttribute("errorMsg", "签名不正确");
                return result;
            }

            // 校验账户财务信息
            checkMerchantFinance(results, merchantUUID);
            if (!RequestStatus.SUCCESS.getStatus().equals(results.getStatus())) {
                request.setAttribute("errorMsg", "系统异常01");
                return result;
            }

            // 校验订单是否重复
            checkMerchantOrderNumber(results, merchantOrderNumber, merchantUUID);
            if (!RequestStatus.SUCCESS.getStatus().equals(results.getStatus())) {
                request.setAttribute("errorMsg", "系统异常02");
                return result;
            }

            // 生成订单
            MerchantRechargeOrder generatorOrder = generatorOrder(merchantUUID, signMap);
            // 请求转发到第三方

            sendRediect(response, signMap, generatorOrder, request);
        } catch (Exception e) {
            request.setAttribute("errorMsg", "系统异常03");
            return result;
        }
        if (StringUtils.isEmpty(getParameter(request, "bank_code"))) {
            return "send";
        }
        return "send1";
    }

    @RequestMapping("/merchant/pay/ysb")
    public String pagePay(HttpServletRequest request, HttpServletResponse response) {

        MerchantAccountInfo merchant = MerchantUtils.getMerchant();
        String payAmount = request.getParameter("payAmount");
        if (null == merchant) {
            return "fail";
        }
        try {
            Double.parseDouble(payAmount);
        } catch (Exception e) {
            return "fail";
        }
        Map<String, Object> signMap = Maps.newHashMap();

        String merchantUUID = merchant.getUuid();

        try {

            signMap.put("pay_amount", request.getParameter("payAmount"));

            MerchantRechargeOrder generatorOrder = generatorOrder(merchantUUID, signMap);

            sendRediect(response, signMap, generatorOrder, request);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "send";
    }


    /**
     * HY 充值回调
     *
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("/getway/recharge/back/{route}")
    @ResponseBody
    public String rechargeCallBack(HttpServletRequest request, HttpServletResponse response, @PathVariable(name = "route") String route)
            throws Exception, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Class<?> payBean = Class.forName("com.polypay.platform.paychannel." + route.toUpperCase() + "PayChannel");
        return pay(request, payBean);
    }

    private String pay(HttpServletRequest request, Class<?> payBean)
            throws Exception {
        IPayChannel paychannel = (IPayChannel) payBean.newInstance();
        String orderName = "orderid";

        if (payBean.equals(RXPayChannel.class)) {
            orderName = "OrderID";
        }
        MerchantRechargeOrder orderByMerchantOrderNumber = merchantRechargeOrderService
                .getOrderByOrderNumber(request.getParameter(orderName));

        if (null == orderByMerchantOrderNumber) {
            return "fail";
        }

        List<MerchantChannelAmount> merchantChannelAmounts = merchantChannelAmountService.listAll();
        MerchantChannelAmount m = null;
        if (!CollectionUtils.isEmpty(merchantChannelAmounts)) {
            for (MerchantChannelAmount merchantChannelAmount : merchantChannelAmounts) {
                if (merchantChannelAmount.getId().toString().equals(orderByMerchantOrderNumber.getAccountMobileNumber())
                        && orderByMerchantOrderNumber.getMerchantId().equals(merchantChannelAmount.getChannelId().toString())) {
                    m = merchantChannelAmount;
                    break;
                }
            }
        }

        Map<String, Object> result = paychannel.checkOrder(m, request);

        Object payStatus = result.get("status");

        if (null == payStatus || "-10".equals(payStatus)) {
            return "fail";
        }

        String merchantOrderNumber = result.get("orderno").toString();


        // 除去0000 为 订单成功处理失败
        if (!("1").equals(payStatus.toString())) {
            orderByMerchantOrderNumber.setStatus(OrderStatusConsts.FAIL);
            merchantRechargeOrderService.updateByPrimaryKeySelective(orderByMerchantOrderNumber);

            // callbackurl
            String resultAsString = sendNotifyMerchant(orderByMerchantOrderNumber);
            // success
            return resultAsString;
        }

        String merchantId = orderByMerchantOrderNumber.getMerchantId();

        // 原有资金
        BigDecimal resourceAmount;
        // 原冻结资金
        BigDecimal resourceFrezzAmount;

        // 订单金额
        BigDecimal payAmount;
        // 费率
        Double rate;
        // 手续费
        BigDecimal poundage;
        // 到账金额
        BigDecimal arrivalAmount;

        // 冻结金额
        BigDecimal frezzAmount;

        synchronized (merchantId.intern()) {

            MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchantId);

            // 冻结费率
            // 冻结费率
            MerchantAccountInfoVO merchantAccountInfoVO = new MerchantAccountInfoVO();
            merchantAccountInfoVO.setUuid(merchantId);
            MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService
                    .getMerchantInfoByUUID(merchantAccountInfoVO);

            Integer frezz = merchantInfoByUUID.getPayLevel();
            if (frezz < 0 || frezz > 100) {
                frezz = 20;
            }
            Double frezzrate = frezz / 100.0;

            if (null == merchantFinance) {
                return "fail";
            }

            resourceAmount = merchantFinance.getBlanceAmount();
            if (null == resourceAmount) {
                resourceAmount = new BigDecimal(0);
            }
            resourceFrezzAmount = merchantFinance.getFronzeAmount();
            if (null == resourceFrezzAmount) {
                resourceFrezzAmount = new BigDecimal(0);
            }

            rate = getRate(orderByMerchantOrderNumber);

            payAmount = new BigDecimal(result.get("total_fee").toString());
            orderByMerchantOrderNumber.setPayAmount(payAmount);

            poundage = payAmount.multiply(new BigDecimal(rate)).setScale(4, BigDecimal.ROUND_HALF_UP);

            arrivalAmount = payAmount.subtract(poundage);

            frezzAmount = arrivalAmount.multiply(new BigDecimal(frezzrate)).setScale(4, BigDecimal.ROUND_HALF_UP);


            MerchantRechargeOrder order = merchantRechargeOrderService
                    .getOrderByOrderNumber(merchantOrderNumber);
            Integer status = order.getStatus();
            if (OrderStatusConsts.SUBMIT == status) {
                // 统一入库
                endInsertOrder(orderByMerchantOrderNumber, resourceAmount, resourceFrezzAmount, poundage, arrivalAmount,
                        frezzAmount, merchantFinance);
            }
            sendNotifyMerchant(orderByMerchantOrderNumber);

        }
        return "success";
    }


    @RequestMapping("/settle/callback/ysb")
    @ResponseBody
    public String payCallBack(HttpServletRequest request) throws ServiceException {

        String accountId = request.getParameter("accountId");
        String orderId = request.getParameter("orderId");
        String amount = request.getParameter("amount");
        String result_code = request.getParameter("result_code");
        String result_msg = request.getParameter("result_msg");

        StringBuffer s = new StringBuffer();
        s.append("accountId").append(accountId)
                .append("&orderId").append(orderId)
                .append("&amount").append(amount)
                .append("&result_code").append(result_code)
                .append("&result_msg").append(result_msg)
                .append("&key").append("howe528741");
        String mac = MD5.md5(s.toString()).toUpperCase();
        log.info(mac);
        log.info(s.toString());

        String rmac = request.getParameter("mac");

//        if (!mac.equals(rmac)) {
//            return "fail";
//
//        }

        MerchantSettleOrder settleOrderByOrderNo;
        try {

            settleOrderByOrderNo = merchantSettleOrderService.getSettleOrderByOrderNo(orderId);

            if (null == settleOrderByOrderNo) {
                return "fail";
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            return "fail";
        }

        if ("0000".equals(result_code)) {
            settleOrderByOrderNo.setStatus(OrderStatusConsts.SUCCESS);
            merchantSettleOrderService.updateByPrimaryKeySelective(settleOrderByOrderNo);
        } else {
            settleOrderByOrderNo.setStatus(OrderStatusConsts.FAIL);
            settleOrderByOrderNo.setDescreption(result_msg);
            merchantSettleOrderService.updateByPrimaryKeySelective(settleOrderByOrderNo);
        }

        return "success";
    }

    @RequestMapping("/settle/callback/rx")
    @ResponseBody
    public String payCallMaxBack(HttpServletRequest request) throws ServiceException {

        String orderId = request.getParameter("OrderID");
        String amount = request.getParameter("Amt");
        String result_code = request.getParameter("RetStatus");
        String result_msg = request.getParameter("RetMsg");

        log.info("settle:" + JSON.toJSONString(request.getParameterMap()));

//        if (!mac.equals(rmac)) {
//            return "fail";
//
//        }

        MerchantSettleOrder settleOrderByOrderNo;
        try {

            settleOrderByOrderNo = merchantSettleOrderService.getSettleOrderByOrderNo(orderId);

            if (null == settleOrderByOrderNo) {
                return "fail";
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            return "fail";
        }

        if ("0".equals(result_code)) {
            settleOrderByOrderNo.setStatus(OrderStatusConsts.SUCCESS);
            merchantSettleOrderService.updateByPrimaryKeySelective(settleOrderByOrderNo);
        } else {
            settleOrderByOrderNo.setStatus(OrderStatusConsts.FAIL);
            settleOrderByOrderNo.setDescreption(result_msg);
            merchantSettleOrderService.updateByPrimaryKeySelective(settleOrderByOrderNo);
        }

        filaPlacePayNotify(settleOrderByOrderNo);

        return "success";
    }

    @RequestMapping("/settle/callback/yufu")
    @ResponseBody
    public String payCallHLBBack(HttpServletRequest request) throws ServiceException {

        String orderId = request.getParameter("orderid");
        String amount = request.getParameter("order_amount");
        String result_code = request.getParameter("rt2_retCode");
        String result_msg = request.getParameter("msg");
        String status = request.getParameter("status");

        log.info("settle:" + JSON.toJSONString(request.getParameterMap()));

//        if (!mac.equals(rmac)) {
//            return "fail";
//
//        }

        MerchantSettleOrder settleOrderByOrderNo;
        try {

            settleOrderByOrderNo = merchantSettleOrderService.getSettleOrderByOrderNo(orderId);

            if (null == settleOrderByOrderNo) {
                return "fail";
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            return "fail";
        }

        if ("0".equals(status)) {
            settleOrderByOrderNo.setStatus(OrderStatusConsts.SUCCESS);
            merchantSettleOrderService.updateByPrimaryKeySelective(settleOrderByOrderNo);
        } else {
            settleOrderByOrderNo.setStatus(OrderStatusConsts.FAIL);
            settleOrderByOrderNo.setDescreption(result_msg);
            merchantSettleOrderService.updateByPrimaryKeySelective(settleOrderByOrderNo);
        }

        filaPlacePayNotify(settleOrderByOrderNo);

        return "success";
    }

    /**
     * 商户充值完回调
     *
     * @param orderByMerchantOrderNumber
     * @return
     * @throws ServiceException
     */
    private String sendNotifyMerchant(MerchantRechargeOrder orderByMerchantOrderNumber) throws Exception {
        String callbackurl = orderByMerchantOrderNumber.getDescreption();
        Long time = System.currentTimeMillis();
        String param = "status=" + orderByMerchantOrderNumber.getStatus() + "&merchantno="
                + orderByMerchantOrderNumber.getMerchantOrderNumber() + "&payno="
                + orderByMerchantOrderNumber.getOrderNumber() + "&merchantid="
                + orderByMerchantOrderNumber.getMerchantId() +
                "&time=" + time;
        String param1 = "status=" + orderByMerchantOrderNumber.getStatus() + "&merchantno="
                + orderByMerchantOrderNumber.getMerchantOrderNumber() + "&payno="
                + orderByMerchantOrderNumber.getOrderNumber() + "&merchantid="
                + orderByMerchantOrderNumber.getMerchantId() +
                "&time=" + time;
        MerchantApi mapi = merchantApiService.getMerchantApiByUUID(orderByMerchantOrderNumber.getMerchantId());

        param += "&api_key=" + mapi.getSecretKey();
        String sign = MD5.md5(param);

        log.info(" ===> " + param);
        log.info(" ===> " + callbackurl + "?" + param1 + "&sign=" + sign);
//		HttpRequestDetailVo httpGet = HttpClientUtil.httpGet(callbackurl + "?" + param1 + "&sign=" + sign);
        String resultAsString = null;
        try {
            resultAsString = new Httpz().get(callbackurl + "?" + param1 + "&sign=" + sign);
            log.info(" ===>1 " + resultAsString);
        } catch (Exception e) {
            /* 停顿一秒 再发起*/
            Thread.sleep(1000);
            resultAsString = new Httpz().get(callbackurl + "?" + param1 + "&sign=" + sign);
            log.info(" ===>2 " + e.getMessage());
        }

        if (!"success".equals(resultAsString)) {
            /* 停顿一秒 再发起*/
            Thread.sleep(1000);
            resultAsString = new Httpz().get(callbackurl + "?" + param1 + "&sign=" + sign);
            log.info(" ===>3 " + resultAsString);
        }

        // success - 成功 -- fail - 失败
        log.info(orderByMerchantOrderNumber.getMerchantOrderNumber() + " ===> " + resultAsString);
        return resultAsString;
    }

    /**
     * 代理商佣金
     *
     * @param setScale
     * @param merchantInfoByUUID
     * @throws ServiceException
     */
    private void endInsertProxyFinance(BigDecimal setScale, MerchantAccountInfo merchantInfoByUUID) throws ServiceException {

        if (setScale.compareTo(new BigDecimal(0)) < 0) {
            return;
        }

        MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
        merchantInfo.setUuid(merchantInfoByUUID.getProxyId());
        MerchantAccountInfo proxy = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

        if (!proxy.getRoleId().equals(RoleConsts.PROXY)) {
            return;
        }

        MerchantFinance merchantFinanceByUUID = merchantFinanceService.getMerchantFinanceByUUID(proxy.getUuid());
        merchantFinanceByUUID.setBlanceAmount(merchantFinanceByUUID.getBlanceAmount().add(setScale));
        merchantFinanceService.updateByPrimaryKeySelective(merchantFinanceByUUID);

    }

    private String getUrl(Map<String, String> signMap, List<String> keys) {
        StringBuffer newSign = new StringBuffer();
        keys.forEach(key -> {
            newSign.append(key).append("=").append(signMap.get(key)).append("&");
        });

        if (StringUtils.isEmpty(newSign.toString())) {
            return null;
        }
        newSign.delete(newSign.length() - 1, newSign.length());
        return newSign.toString();
    }

    /**
     * 商户 结算订单提交
     *
     * @param request
     * @param response
     * @return
     * @throws ServiceException
     */
    @RequestMapping("gateway/placepay/pre")
    @ResponseBody
    public Map<String, Object> placePay(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException {

        Map<String, Object> result = Maps.newHashMap();
        Map<String, String> sParam = Maps.newHashMap();
        List<String> keys = Lists.newArrayList();
        MerchantSettleOrder sOrder = null;
        // 商户ID
        String merchant_id = getParameter(request, "merchant_id");
        sParam.put("merchant_id", merchant_id);
        keys.add("merchant_id");

        if (StringUtils.isEmpty(merchant_id)) {
            result.put("status", RequestStatus.FAILED.getStatus());
            result.put("msg", "merchant_id isnull");
            return result;
        }

        // 行级锁
        synchronized (merchant_id.intern()) {

            // 支付类型 结算
            String paytype = getParameter(request, "pay_type"); // 100001 结算 100002 代付
            sParam.put("pay_type", paytype);
            keys.add("pay_type");

            if (StringUtils.isEmpty(paytype)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "pay_type is null");
                return result;
            }

            // 支付金额
            String total_amount = getParameter(request, "total_amount");
            sParam.put("total_amount", total_amount);
            keys.add("total_amount");

            if (StringUtils.isEmpty(total_amount)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "total_amount is null");
                return result;
            }

            // 订单编号
            String out_trade_no = getParameter(request, "order_no");
            sParam.put("order_no", out_trade_no);
            keys.add("order_no");

            if (StringUtils.isEmpty(out_trade_no)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "out_trade_no is null");
                return result;
            }

            // 银行卡号
            String bank_number = getParameter(request, "bank_account_number");
            sParam.put("bank_account_number", bank_number);
            keys.add("bank_account_number");

            if (StringUtils.isEmpty(bank_number)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "bank_number is null");
                return result;
            }

            // 银行编码
            String bankCode = getParameter(request, "bank_code");
            sParam.put("bank_code", bankCode);
            keys.add("bank_code");

            if (StringUtils.isEmpty(bankCode)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "bank_code is null");
                return result;
            }

            // 开户名
            String bankusername = getParameter(request, "bank_user_name");
            sParam.put("bank_user_name", bankusername);
            keys.add("bank_user_name");

            if (StringUtils.isEmpty(bankusername)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "bank_user_name is null");
                return result;
            }

            // 银行联行号
            String bankNo = getParameter(request, "bank_no");
            sParam.put("bank_no", bankNo);
            keys.add("bank_no");

            if (StringUtils.isEmpty(bankNo)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "bank_no is null");
                return result;
            }

            // 银行名称
            String bankName = getParameter(request, "bank_name");
            sParam.put("bank_name", bankName);
            keys.add("bank_name");

            if (StringUtils.isEmpty(bankName)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "bank_name is null");
                return result;
            }

            // 省
            String province = getParameter(request, "province");
            sParam.put("province", province);
            keys.add("province");

            if (StringUtils.isEmpty(province)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "province is null");
                return result;
            }

            // 城市
            String city = getParameter(request, "city");
            sParam.put("city", city);
            keys.add("city");

            if (StringUtils.isEmpty(city)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "city is null");
                return result;
            }

            // 银行全称
            String bankaddress = getParameter(request, "bank_address");
            sParam.put("bank_address", bankaddress);
            keys.add("bank_address");

            if (StringUtils.isEmpty(bankaddress)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "bank_address is null");
                return result;
            }


            // 代付回调
            String notify_url = getParameter(request, "notify_url");
            sParam.put("notify_url", notify_url);
            keys.add("notify_url");
            if (StringUtils.isEmpty(notify_url)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "notify_url is null");
                return result;
            }


            MerchantApi merchantApiByUUID = merchantApiService.getMerchantApiByUUID(merchant_id);

            if (null == merchantApiByUUID) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "finance status is lock");
                return result;
            }

            sParam.put("api_key", merchantApiByUUID.getSecretKey());
            keys.add("api_key");

            String sign = getParameter(request, "sign");
            String sign2 = getSign(sParam, keys);

            if (!sign2.equals(sign)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "sign check fail");
                return result;
            }

            MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchant_id);

            if (null == merchantFinance) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "finance is not found");
                return result;
            }

            if (!merchantFinance.getStatus().equals(MerchantFinanceStatusConsts.USABLE)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "finance is not found1");
                return result;
            }

            MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
            merchantInfo.setUuid(merchant_id);
            MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

            if (!merchantInfoByUUID.getStatus().equals(MerchantAccountInfoStatusConsts.SUCCESS)) {
                result.put("status", RequestStatus.FAILED.getStatus());
                result.put("msg", "merchant is not found");
                return result;
            }

            BigDecimal blanceAmount = merchantFinance.getBlanceAmount();
            BigDecimal serviceAmount = merchantInfoByUUID.getHandAmount();
            BigDecimal settleAmount = new BigDecimal(total_amount);

            Integer channelId = merchantInfoByUUID.getChannelId();

            // 新网支付
            if (channelId == 2) {
                if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
                    result.put("status", RequestStatus.FAILED.getStatus());
                    return result;
                }
                settleAmount = settleAmount.add(serviceAmount);
            } else if (channelId == 1) // 熊猫代付
            {
                if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
                    result.put("status", RequestStatus.FAILED.getStatus());
                    return result;
                }
                settleAmount = settleAmount.add(serviceAmount);
            } else if (channelId == 3)// 合付宝支付
            {

                if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
                    result.put("status", RequestStatus.FAILED.getStatus());
                    result.put("msg", "amount < sAmount");
                    return result;
                }
                settleAmount = settleAmount.add(serviceAmount);
            } else {
                if (blanceAmount.compareTo(settleAmount.add(serviceAmount)) < 0) {
                    result.put("status", RequestStatus.FAILED.getStatus());
                    result.put("msg", "amount < sAmount");
                    return result;
                }
                settleAmount = settleAmount.add(serviceAmount);
            }

            if ("100001".equals(paytype)) // 结算
            {
                sOrder = new MerchantSettleOrder();

                String currentOrder = DateUtil.getCurrentDate();
                String orderNumber = "S" + currentOrder + RandomUtils.random(2);

                sOrder.setOrderNumber(orderNumber);

                sOrder.setMerchantOrderNumber(out_trade_no);
                sOrder.setMerchantBindBank(bank_number);
                sOrder.setBankCode(bankCode);
                sOrder.setBankName(bankName);
                sOrder.setBranchBankName(bankaddress);

                sOrder.setAccountName(bankusername);
                sOrder.setAccountProvice(province);
                sOrder.setAccountCity(city);
                sOrder.setBankNo(bankNo);

                sOrder.setMerchantId(merchant_id);
                sOrder.setStatus(OrderStatusConsts.SUBMIT);
                sOrder.setCreateTime(new Date());
                sOrder.setPostalAmount(settleAmount);
                sOrder.setServiceAmount(serviceAmount);
                sOrder.setType(MerchantOrderTypeConsts.SETTLE_ORDER);

                sOrder.setCallUrl(notify_url);
                merchantSettleOrderService.insertSelective(sOrder);

            } else if ("100002".equals(paytype)) { // 代付

                MerchantPlaceOrder pOrder = new MerchantPlaceOrder();

                String currentOrder = DateUtil.getCurrentDate();
                String orderNumber = "P" + currentOrder + RandomUtils.random(6);

                pOrder.setOrderNumber(orderNumber);
                pOrder.setMerchantOrderNumber(out_trade_no);
                pOrder.setBankNumber(bank_number);
                pOrder.setBankCode(bankCode);
                pOrder.setBankName(bankName);
                pOrder.setBranchBankName(bankaddress);

                pOrder.setAccountName(bankusername);
                pOrder.setAccountProvice(province);
                pOrder.setAccountCity(city);
                pOrder.setBankNo(bankNo);

                pOrder.setMerchantId(merchant_id);
                pOrder.setStatus(OrderStatusConsts.SUBMIT);
                pOrder.setCreateTime(new Date());
                pOrder.setPayAmount(settleAmount);
                pOrder.setServiceAmount(serviceAmount);
                pOrder.setType(MerchantOrderTypeConsts.SETTLE_ORDER);

                pOrder.setCallUrl(notify_url);
                merchantPlaceOrderService.insertSelective(pOrder);
            }

            // 扣除余额
            merchantFinance.setBlanceAmount(blanceAmount.subtract(settleAmount));
            merchantFinanceService.updateByPrimaryKeySelective(merchantFinance, settleAmount, new BigDecimal(0), "del");

            try {
                // 自动下发
                if (OPEN_AUTO == 0) {

                    MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService.getSettleOrderByOrderNo(sOrder.getOrderNumber());

                    if (null != selectByPrimaryKey) {

                        // 异步执行该订单修改该订单状态
                        executorService.execute(() -> submitSettleOrder(selectByPrimaryKey));
                    }
                }
            } catch (Exception e) {
                log.info("settle " + e.getMessage());
            }
        }


        //  订单生成成功

        result.put("status", "0");
        return result;
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getway/change")
    @ResponseBody
    public String changeStatus(HttpServletRequest request, HttpServletResponse response) {
        String parameter = request.getParameter("status");
        String parameter2 = request.getParameter("key");
        if (!"abcabc".equals(parameter2)) {
            return null;
        }

        String msg = null;
        if ("1".equals(parameter)) {
            OPEN_AUTO = 1;
            msg = "set 1 success " + OPEN_AUTO;
        }

        if ("0".equals(parameter)) {
            OPEN_AUTO = 0;
            msg = "set 0 success " + OPEN_AUTO;
        }


        return msg;
    }

    private String getSign(Map<String, String> signMap, List<String> keys) {

        StringBuffer newSign = new StringBuffer();
        keys.forEach(key -> {
            newSign.append(key).append("=").append(signMap.get(key)).append("&");
        });

        if (StringUtils.isEmpty(newSign.toString())) {
            return null;
        }
        newSign.delete(newSign.length() - 1, newSign.length());
        return MD5.md5(newSign.toString());
    }


    /**
     * 充值订单查询
     *
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/getway/recharge/order/query")
    @ResponseBody
    public Map<String, Object> getRechargeOrder(HttpServletRequest request) throws ServiceException {

        Map<String, Object> result = Maps.newHashMap();

        String merchantid = getParameter(request, "merchant_id");

        String orderNumber = getParameter(request, "order_no");

        String time = getParameter(request, "time");

        String sign = getParameter(request, "sign");

        StringBuffer signParam = new StringBuffer();

        MerchantApi merchantApiByUUID2 = merchantApiService.getMerchantApiByUUID(merchantid);

        signParam.append("merchant_id=" + merchantid).append("&order_no=" + orderNumber).append("&time=" + time)
                .append("&api_key=" + merchantApiByUUID2.getSecretKey());

        if (!sign.equals(MD5.md5(signParam.toString()))) {
            result.put("status", -1);
            result.put("msg", "签名错误");
            return result;
        }

        MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
        merchantInfo.setUuid(merchantid);
        MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

        if (null == merchantInfoByUUID) {
            result.put("status", -1);
            result.put("msg", "商户不存在");
            return result;
        }

        Map<String, Object> param = Maps.newHashMap();
        param.put("merchantid", merchantid);
        param.put("merchantOrderNumber", orderNumber);

        MerchantRechargeOrder orderByMerchantOrderNumber = merchantRechargeOrderService
                .getOrderByMerchantOrderNumber(param);
        if (null == orderByMerchantOrderNumber) {
            result.put("status", -1);
            result.put("msg", "订单不存在");
            return result;
        }

        if (!merchantid.equals(orderByMerchantOrderNumber.getMerchantId())) {
            result.put("status", -1);
            result.put("msg", "订单不存在");
            return result;
        }

        if (orderByMerchantOrderNumber.getStatus().equals(OrderStatusConsts.FAIL)) {
            result.put("status", -2);
            result.put("msg", "失败订单");
            return result;
        }

        if (orderByMerchantOrderNumber.getStatus().equals(OrderStatusConsts.SUBMIT)) {
            result.put("status", 1);
            result.put("msg", "待支付订单");
            return result;
        }

        result.put("status", 0);
        result.put("msg", "成功订单");
        result.put("merchantno", orderByMerchantOrderNumber.getMerchantOrderNumber());
        result.put("payno", orderByMerchantOrderNumber.getOrderNumber());
        result.put("amount", orderByMerchantOrderNumber.getPayAmount());

        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    void endInsertOrder(MerchantRechargeOrder orderByMerchantOrderNumber, BigDecimal resourceAmount,
                        BigDecimal resourceFrezzAmount, BigDecimal poundage, BigDecimal arrivalAmount, BigDecimal frezzAmount,
                        MerchantFinance merchantFinance) throws ServiceException {

        try {
            log.info("6");
            // 修改订单入库
            saveRechargeOrder(orderByMerchantOrderNumber, poundage, arrivalAmount);

            if (frezzAmount.compareTo(new BigDecimal(0)) > 0) {
                MerchantFrezzon merchantFrezzon = new MerchantFrezzon();
                merchantFrezzon.setAmount(frezzAmount);
                merchantFrezzon.setFrezzTime(new Date());
                merchantFrezzon.setArrivalTime(DateUtils.getAfterDayDate("1"));
                merchantFrezzon.setStatus(MerchantFinanceStatusConsts.FREEZE);
                merchantFrezzon.setMerchantId(orderByMerchantOrderNumber.getMerchantId());
                merchantFrezzon.setOrderNumber(orderByMerchantOrderNumber.getOrderNumber());
                merchantFrezzService.insertSelective(merchantFrezzon);
            }
            log.info("addfinance = >" + resourceAmount.toString() + " = " + arrivalAmount);
            merchantFinance.setBlanceAmount(resourceAmount.add(arrivalAmount.subtract(frezzAmount)));
            merchantFinance.setFronzeAmount(resourceFrezzAmount.add(frezzAmount));

            merchantFinanceService.updateByPrimaryKeySelective(merchantFinance, arrivalAmount, frezzAmount, "add");
        } catch (Exception e) {
            log.error("save order back error");
        }
    }

    private void saveRechargeOrder(MerchantRechargeOrder orderByMerchantOrderNumber, BigDecimal poundage,
                                   BigDecimal arrivalAmount) throws ServiceException {
        orderByMerchantOrderNumber.setStatus(OrderStatusConsts.SUCCESS);
        orderByMerchantOrderNumber.setServiceAmount(poundage);
        orderByMerchantOrderNumber.setSuccessTime(new Date());
        orderByMerchantOrderNumber.setArrivalAmount(arrivalAmount);
        merchantRechargeOrderService.updateByPrimaryKeySelective(orderByMerchantOrderNumber);
    }

    private Double getRate(MerchantRechargeOrder orderByMerchantOrderNumber) throws ServiceException {

        PayType payType = payTypeService.getPayTypeChannel(orderByMerchantOrderNumber.getMerchantId(), "WY");
        if (null == payType) {
            return 0.02;
        }
        return payType.getRate().doubleValue() / 1000.0;
    }

    private void sendRediect(HttpServletResponse response, Map<String, Object> signMap,
                             MerchantRechargeOrder generatorOrder, HttpServletRequest request) throws ServiceException, IOException {

        MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
        merchantInfo.setUuid(generatorOrder.getMerchantId());
        MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

        Channel selectByPrimaryKey = channelService.selectByPrimaryKey(merchantInfoByUUID.getChannelId());
        signMap.put("call_back", selectByPrimaryKey.getCallBack());

        // 根据配置的四方平台
        sendRedirectUrl(response, selectByPrimaryKey.getBean(), signMap, generatorOrder, request);

    }

    private void sendRedirectUrl(HttpServletResponse response, String bean, Map<String, Object> signMap,
                                 MerchantRechargeOrder generatorOrder, HttpServletRequest request) {

        try {

            Class<?> forName = Class.forName(bean);

            IPayChannel payChannel = (IPayChannel) forName.newInstance();

            // 通道关闭
            if (null == payChannel) {
                generatorOrder.setStatus(OrderStatusConsts.FAIL);
                merchantRechargeOrderService.updateByPrimaryKeySelective(generatorOrder);
                return;
            }
            List<MerchantChannelAmount> merchantChannelAmounts = merchantChannelAmountService.listAll();

            if (!CollectionUtils.isEmpty(merchantChannelAmounts)) {
                for (MerchantChannelAmount merchantChannelAmount : merchantChannelAmounts) {
                    if ("1".equals(merchantChannelAmount.getReFlag().toString())
                            && generatorOrder.getMerchantId().equals(merchantChannelAmount.getChannelId().toString())) {
                        signMap.put("send", merchantChannelAmount);
                        generatorOrder.setAccountMobileNumber(merchantChannelAmount.getId().toString());
                        merchantRechargeOrderService.updateByPrimaryKeySelective(generatorOrder);
                        break;
                    }
                }
            }

            payChannel.sendRedirect(signMap, response, request);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    private void checkMerchantOrderNumber(ServiceResponse result, String merchantOrderNumber, String merchantUUID)
            throws ServiceException {

        Map<String, Object> param = Maps.newHashMap();

        param.put("merchantOrderNumber", merchantOrderNumber);

        param.put("merchantId", merchantUUID);

        MerchantRechargeOrder merchantRechargeOrder = merchantRechargeOrderService.getOrderByMerchantOrderNumber(param);

        if (null != merchantRechargeOrder) {
            ResponseUtils.exception(result, "订单已提交！", RequestStatus.FAILED.getStatus());
        }

    }

    private MerchantRechargeOrder generatorOrder(String merchantUUID, Map<String, Object> signMap)
            throws ServiceException {

        MerchantRechargeOrder order = new MerchantRechargeOrder();
        String currentOrder = DateUtil.getCurrentDate();
        String orderNumber = RECHARGE + currentOrder + RandomUtils.random(2);

        order.setOrderNumber(orderNumber);
        order.setPayBank(getValue(signMap, "bank_code"));
        order.setPayAmount(new BigDecimal(getValue(signMap, "pay_amount")));
        order.setCreateTime(new Date());
        order.setAccountBankNumber(getValue(signMap, "bank_no"));

        Object payment_code = signMap.get("payment_code");
        if (null != payment_code) {
            order.setBankOrderNumber(payment_code.toString());
        }

        Channel channel = channelService.selectChannelByMerchantId(merchantUUID);
        order.setPayChannel(channel.getName());

        order.setStatus(OrderStatusConsts.SUBMIT);
        order.setType(MerchantOrderTypeConsts.RECHARGE_ORDER);

        order.setDescreption(getValue(signMap, "notify_url"));

        order.setMerchantId(merchantUUID);
        order.setMerchantOrderNumber(getValue(signMap, "order_number"));
        signMap.put("we_order_number", orderNumber);

        MerchantAccountInfoVO merchantAccountInfoVO = new MerchantAccountInfoVO();
        merchantAccountInfoVO.setUuid(merchantUUID);
        MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService
                .getMerchantInfoByUUID(merchantAccountInfoVO);
        Integer payLevel = merchantInfoByUUID.getPayLevel();
        order.setTradeType(payLevel == 0 ? "D0" : "D1");
        merchantRechargeOrderService.insertSelective(order);
        return order;
    }

    private String getValue(Map<String, Object> signMap, String key) {
        return signMap.get(key) == null ? "" : signMap.get(key).toString();
    }

    private void checkMerchantFinance(ServiceResponse result, String merchantUUID) throws ServiceException {
        MerchantFinance merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(merchantUUID);
        if (null == merchantFinance) {
            ResponseUtils.exception(result, "财务信息缺失！", RequestStatus.FAILED.getStatus());
            return;
        }
        if (merchantFinance.getStatus().equals(MerchantFinanceStatusConsts.FREEZE)) {
            ResponseUtils.exception(result, "通道维护中！", RequestStatus.FAILED.getStatus());
        }

    }

    private MerchantApi getMerchantApi(String merchantUUID) throws ServiceException {

        MerchantApi merchantApi = merchantApiService.getMerchantApiByUUID(merchantUUID);

        return merchantApi;
    }

    private static boolean checkSign(Map<String, Object> signMap, String sign, List<String> keys) throws Exception {

        StringBuffer newSign = new StringBuffer();
        keys.forEach(key -> {
            newSign.append(key).append("=").append(signMap.get(key)).append("&");
        });

        if (StringUtils.isEmpty(newSign.toString())) {
            return false;
        }
        newSign.delete(newSign.length() - 1, newSign.length());

        return MD5.md5(newSign.toString()).equals(sign) ? true : false;
    }

    private void checkMerchantId(String merchantUUID, ServiceResponse result) throws ServiceException {

        MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
        merchantInfo.setUuid(merchantUUID);
        MerchantAccountInfo merchantAccountInfo = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

        if (null == merchantAccountInfo) {
            ResponseUtils.exception(result, "该商户未注册！", RequestStatus.FAILED.getStatus());
            return;
        }
    }

    public String getParameter(HttpServletRequest request, String key) {
        String parameter = request.getParameter(key);
        return StringUtils.isEmpty(parameter) ? "" : parameter;
    }

    // 同步回滚订单
    private void rollBackSettlerOrder(MerchantSettleOrder merchantSettleOrder) throws ServiceException {
        synchronized (merchantSettleOrder.getMerchantId().intern()) {

            MerchantSettleOrder selectByPrimaryKey = merchantSettleOrderService
                    .selectByPrimaryKey(merchantSettleOrder.getId());
            if (selectByPrimaryKey.getStatus().equals(OrderStatusConsts.SUBMIT)
                    || selectByPrimaryKey.getStatus().equals(OrderStatusConsts.HANDLE)) {
                // 回滚金额
                MerchantFinance merchantFinance = merchantFinanceService
                        .getMerchantFinanceByUUID(merchantSettleOrder.getMerchantId());
                merchantFinance
                        .setBlanceAmount(merchantFinance.getBlanceAmount().add(merchantSettleOrder.getPostalAmount()));
                merchantSettleOrder.setStatus(OrderStatusConsts.FAIL);
                merchantSettleOrderService.updateByPrimaryKeySelective(merchantSettleOrder);
                merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
            }
        }
    }

    private void submitSettleOrder(MerchantSettleOrder merchantSettleOrder) {

        Object status = null;
        try {

            MerchantSettleOrder selectByPrimaryKey;
            synchronized (merchantSettleOrder.getMerchantId().intern()) {

                selectByPrimaryKey = merchantSettleOrderService.selectByPrimaryKey(merchantSettleOrder.getId());

                if (null == selectByPrimaryKey) {
                    return;
                }

                if (OrderStatusConsts.SUBMIT != selectByPrimaryKey.getStatus()) {
                    return;
                }

                MerchantAccountInfoVO merchantInfo = new MerchantAccountInfoVO();
                merchantInfo.setUuid(merchantSettleOrder.getMerchantId());
                MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(merchantInfo);

                Channel channel = channelService.selectByPrimaryKey(merchantInfoByUUID.getChannelId());

                Class<?> payBean = Class.forName(channel.getBean());
                IPayChannel paychannel = (IPayChannel) payBean.newInstance();

                // {"status":1,"msg":"代付申请成功，系统处理中","serial":"代付订单号"}
                // {"status":0,"msg":"代付失败"}

                List<MerchantChannelAmount> merchantChannelAmounts = merchantChannelAmountService.listAll();

                if (!CollectionUtils.isEmpty(merchantChannelAmounts)) {
                    for (MerchantChannelAmount merchantChannelAmount : merchantChannelAmounts) {
                        if ("1".equals(merchantChannelAmount.getDfFlag().toString())
                                && merchantSettleOrder.getMerchantId().equals(merchantChannelAmount.getChannelId().toString())) {
                            selectByPrimaryKey.setMm(merchantChannelAmount);
                            break;
                        }
                    }
                }

                Map<String, Object> result = paychannel.settleOrder(selectByPrimaryKey);

                status = result.get("status");

                // 返回结果失败 回滚订单
                if (status.toString().equals("0")) {

                    merchantSettleOrder.setDescreption(null == result.get("msg") ? "" : result.get("msg").toString());
                    // 回滚
                    rollBackSettlerOrder(merchantSettleOrder);


                    // 通知
                    filaPlacePayNotify(merchantSettleOrder);
                    return;
                }

                // 未支付状态返回
                if (status.toString().equals("2")) {
                    return;
                }

                // 成功修改订单状态
                merchantSettleOrder.setStatus(OrderStatusConsts.HANDLE);

                merchantSettleOrder.setHandlePeople("admin");
                merchantSettleOrder.setPayTime(new Date());
                merchantSettleOrderService.updateByPrimaryKeySelective(merchantSettleOrder);
            }

        } catch (Exception e) {
            // 回滚
            try {
                if (null != status && "1".equals(status.toString())) {
                    return;
                }
                rollBackSettlerOrder(merchantSettleOrder);
                filaPlacePayNotify(merchantSettleOrder);
            } catch (ServiceException e1) {
                log.error(" settle order fail: " + merchantSettleOrder.getMerchantId() + " - "
                        + merchantSettleOrder.getId());

            }

        }
    }

    private void filaPlacePayNotify(MerchantSettleOrder merchantSettleOrder) throws ServiceException {


        Map<String, String> param = Maps.newHashMap();
        List<String> keys = Lists.newArrayList();

        param.put("merchant_id", merchantSettleOrder.getMerchantId());
        keys.add("merchant_id");

        param.put("order_no", merchantSettleOrder.getOrderNumber());
        keys.add("order_no");

        param.put("m_order_no", merchantSettleOrder.getMerchantOrderNumber());
        keys.add("m_order_no");

        param.put("amount", merchantSettleOrder.getPostalAmount().toString());
        keys.add("amount");

        String status = "";
        if (merchantSettleOrder.getStatus().equals(OrderStatusConsts.SUCCESS)) {
            status = "success";
        } else if (merchantSettleOrder.getStatus().equals(OrderStatusConsts.FAIL)) {
            status = "fail";
        }

        param.put("status", status);
        keys.add("status");  // 1 支付中   0 成功  -1  失败

        param.put("time", System.currentTimeMillis() + "");
        keys.add("time");

        MerchantApi merchantApiByUUID = merchantApiService.getMerchantApiByUUID(merchantSettleOrder.getMerchantId());

        String signParam = getUrl(param, keys);
        signParam += "&api_key=" + merchantApiByUUID.getSecretKey();
        String sign = MD5.md5(signParam);

        param.put("sign", sign);

        HttpClientUtil.httpPost(merchantSettleOrder.getCallUrl(), null, param);

    }


}
