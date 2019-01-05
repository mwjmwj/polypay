package com.polypay.platform.utils;

import net.sf.json.JSONObject;

/**
 * @author: 大数据应用开发- ggt
 * @date: 2018-09-21 16:07
 * @version: 1.0
 */
public class JsonUtil {

    public static Object getJsonValue(String jsonStr,String jsonKey){

        if(jsonStr!=null||"".equals(jsonStr)&&jsonKey!=null||"".equals(jsonKey)){

            boolean validateInfo = new JsonValidator().validate(jsonStr);

            if(validateInfo){

                JSONObject jsonObject = JSONObject.fromObject(jsonStr);

                return  jsonObject.get(jsonKey);

            }
         return "非Json格式";
        }
        return "传入参数有误";
    }
}

