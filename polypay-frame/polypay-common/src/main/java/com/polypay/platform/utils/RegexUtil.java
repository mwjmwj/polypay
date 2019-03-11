package com.polypay.platform.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 正则表达式工具类
 * @since: 2017年7月10日上午10:14:20 
 * @version: 1.0
 */
public class RegexUtil {

    /**
     * IP地址
     */
    private static final String IP_ADDRESS = "^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    /**
     * 邮箱
     */
    private static final String EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    /**
     * 身份证
     */
    private static final String IDENTIFY = "^\\d{15}(\\d{2}[0-9xX])?$";

    /**
     * 护照
     */
    private static final String HZ = "^[a-zA-Z](\\d{8}|\\d{7})$";

    /**
     * 手机号数组
     */
    private static final String[] MOBILE_ARR = new String[]{
        // 移动
        "134", "135", "136", "137", "138", "139",
        "147", "148", 
        "150", "151", "152", "157", "158", "159",
        "177", "178", "179",
        "182", "183", "184", "187", "188",  
        "198", 
        
        // 联通
        "130", "131", "132", 
        "145", "146",
        "155", "156", 
        "166", "167", "168", "169",
        "175", "176",
        "185", "186", 
        "197", "198", "199","171",
        
        // 电信
        "133", 
        "149", 
        "153", 
        "173", "174", "177", 
        "180", "181", "189", 
        "199"
    };

    /**
     * 虚拟运营商号码段
     */
    private static final String[] VIRSUAL_MOBILE_OPERATOR = new String[]{
        "170", "171", "172"
    };

    /**
     * 手机号表达式
     */
    public static final String MOBILE_PATTERN = "^(" + StringUtils.join(MOBILE_ARR, "|") + ")\\d{8}$";
    
    /**
     * 虚拟运营商手机号表达式
     */
    public static final String VIRSUAL_MOBILE_PATTERN = "^(" + StringUtils.join(VIRSUAL_MOBILE_OPERATOR, "|") + ")\\d{8}$";
    
    /**
     * 验证特殊符号
     */
    public static final String SPECIAL_SYMBOL = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    
    /**
     * 判断是否为ip地址
     * @param ip
     * @return
     */
    public static boolean isIPAddress(String ip){
        return Pattern.compile(IP_ADDRESS).matcher(ip).matches();
    }

    /**
     * 判断是否为手机号
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone){
        return Pattern.compile(MOBILE_PATTERN).matcher(phone).matches();
    }

    /**
     * 判断是否为虚拟运营商手机号
     * @param phone
     * @return
     */
    public static boolean isVirsualMobile(String phone){
        return Pattern.compile(VIRSUAL_MOBILE_PATTERN).matcher(phone).matches();
    }

    /**
     * 邮箱地址
     * @author: 研发部-黄泽建
     * @param line
     * @return
     */
    public static boolean isEmail(String line){
        Matcher m = Pattern.compile(EMAIL).matcher(line);
        return m.find();
    }

    /**
     * 判断是否符合身份证的 字符串格式
     * 身份证 15或18位 仅数字+字母
     * @param str
     * @return
     */
    public static boolean isSFZ(String str){
        String s = str.trim();
        Matcher match = Pattern.compile(IDENTIFY).matcher(s);
        return match.matches();
    }

    /**
     * 判断是否符合护照的 字符串格式
     * @param str
     * @return
     */
    public static boolean isHZ(String str){
        String s = str.trim();
        Matcher match = Pattern.compile(HZ).matcher(s);
        return match.matches();
    }
    
    /**
     * 字符串是否数字
     * @author: 研发部-黄泽建
     * @since: 2018年10月22日下午3:58:25 
     * @version: 1.0.5
     * @param str
     * @return
     */
    public static final boolean isNumeric(String str){
        Matcher isNum = Pattern.compile("^[0-9]*$").matcher(str);
        if(isNum.matches()){
            return true; 
        } 
        return false;
    }
    /**
     * 判断是否存在特殊符号
     * @author: 研发部-谢岳城
     * @param str
     * @return true=存在，false=不存在
     */
    public static final boolean isSpecialSymbol(String str){
        Pattern p = Pattern.compile(SPECIAL_SYMBOL); 
        Matcher m = p.matcher(str);
        return m.find();
    }
}
