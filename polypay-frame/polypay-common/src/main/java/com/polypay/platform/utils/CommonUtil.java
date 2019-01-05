package com.polypay.platform.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 公共工具类
 * @author:  
 * @since: 2017年12月22日上午10:03:51 
 * @version: 1.0
 */
public class CommonUtil {

    /**
     * 空集合
     * @author:  
     * @param t
     * @return
     */
    public static final <T> List<T> emptyList(Class<T> clazz){
        return new ArrayList<T>(0);
    }

    /**
     * 字符串转list
     * @author:  
     * @param str
     * @param sep 分隔符
     * @return
     */
    public static final List<String> str2List(String str, String sep){
        return str2List(str, sep, true, false);
    }

    /**
     * 字符串转list
     * @author:  
     * @param str
     * @param sep 分隔符
     * @param filterBlank 过滤纯空白
     * @return
     */
    public static final List<String> str2List(String str, String sep, boolean filterBlank){
        return str2List(str, sep, filterBlank, false);
    }

    /**
     * 字符串转list
     * @author:  
     * @param str
     * @param sep 分隔符
     * @param filterBlank 过滤纯空白
     * @param trim 去掉首尾空白
     * @return
     */
    public static final List<String> str2List(String str, String sep, boolean filterBlank, boolean trim){
        List<String> list = new ArrayList<String>();
        if(StringUtils.isEmpty(str)){ return list; }

        // 过滤空白字符串
        if(filterBlank && StringUtils.isBlank(str)){ return list; }
        String[] split = str.split(sep);
        for(String string : split){
            if(filterBlank && StringUtils.isBlank(string)){
                continue;
            }
            if(trim){
                string = string.trim();
            }
            list.add(string);
        }

        return list;
    }

    /**
     * 字符串转set
     * @author:  
     * @param str
     * @param sep 分隔符
     * @return
     */
    public static final Set<String> str2Set(String str, String sep){
        return new HashSet<String>(str2List(str, sep, true, false));
    }

    /**
     * 字符串转set
     * @author:  
     * @param str
     * @param sep 分隔符
     * @param filterBlank 过滤纯空白
     * @return
     */
    public static final Set<String> str2Set(String str, String sep, boolean filterBlank){
        return new HashSet<String>(str2List(str, sep, filterBlank, false));
    }

    /**
     * 字符串转set
     * @author:  
     * @param str
     * @param sep 分隔符
     * @param filterBlank 过滤纯空白
     * @param trim 去掉首尾空白
     * @return
     */
    public static final Set<String> str2Set(String str, String sep, boolean filterBlank, boolean trim){
        Set<String> set = new HashSet<String>();
        if(StringUtils.isEmpty(str)){ return set; }

        // 过滤空白字符串
        if(filterBlank && StringUtils.isBlank(str)){ return set; }
        String[] split = str.split(sep);
        for(String string : split){
            if(filterBlank && StringUtils.isBlank(string)){
                continue;
            }
            if(trim){
                string = string.trim();
            }
            set.add(string);
        }

        return set;
    }

    /**
     * list转字符串
     * @author:  
     * @param list
     * @param sep 分隔符
     * @return
     */
    public static final String list2Str(List<String> list, String sep){
        return list2Str(list, sep, false);
    }

    /**
     * list转字符串
     * @author:  
     * @param list
     * @param sep 分隔符
     * @param filterBlank 过滤空白
     * @return
     */
    public static final String list2Str(List<String> list, String sep, boolean filterBlank){
        return list2Str(list, sep, filterBlank, false);
    }

    /**
     * list转字符串
     * @author:  
     * @param list
     * @param sep 分隔符
     * @param filterBlank 过滤空白
     * @param trim 去掉前后空格
     * @return
     */
    public static final String list2Str(List<String> list, String sep, boolean filterBlank, boolean trim){
        if(CollectionUtils.isEmpty(list)){ return ""; }
        StringBuilder sBuilder = new StringBuilder();
        for(String str : list){
            // 过滤空白
            if(filterBlank){
                if(StringUtils.isBlank(str)){
                    continue;
                }
            }

            // 去掉前后空格
            if(trim){
                str = str.trim();
            }

            if(StringUtils.isNotEmpty(str)){
                sBuilder.append(str).append(sep);
            }
        }

        if(sBuilder.length() > 0){ return sBuilder.substring(0, sBuilder.lastIndexOf(sep)); }
        return "";
    }

    /**
     * 获取url的域名
     * @author:  
     * @param url
     * @return
     */
    public static final String urlToDomain(String url){
        String temp = url;
        int protocol = temp.indexOf("//");
        if(-1 < protocol){
            temp = temp.substring(protocol);
        }

        int port = temp.indexOf(":");
        if(-1 < port){
            temp = temp.substring(0, port);
        }
        return temp;
    }

    /**
     * 去除最后斜杠
     * @author:  
     * @param url
     * @return
     */
    public static final String removeLastPath(String url){
        if(null != url){
            while(url.endsWith("/")){
                url = url.substring(0, url.length() - 1);
            }
        }
        return url;
    }

    /**
     * 去除最前的斜杠
     * @author:  
     * @param url
     * @return
     */
    public static final String removeFirstPath(String url){
        if(null != url){
            while(url.startsWith("/")){
                url = url.substring(1);
            }
        }
        return url;
    }

    /**
     * 拼接url路径
     * @author:  
     * @param url
     * @return
     */
    public static final String urlAppend(String url, String... path){
        if(null == url){ return null; }
        url = removeLastPath(url);
        StringBuilder sb = new StringBuilder(url);
        for(String s : path){
            if(null != s){
                s = removeFirstPath(s);
                s = removeLastPath(s);
                sb.append("/").append(s);
            }
        }

        return sb.toString();
    }

    /**
     * 拆分list成n个小list,每个小的最大长度限制
     * @author:  
     * @param list
     * @param pageSize
     * @return
     */
    public static final <T> List<List<T>> splitList(List<T> list, int pageSize){
        List<List<T>> listArr = new ArrayList<List<T>>();
        if(list.size() <= pageSize){
            // 不需要拆分
            listArr.add(list);
            return listArr;
        }
        int total = list.size();
        // 获取被拆分的数组个数
        int pageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        for(int i = 0; i < pageCount; i++){
            int start = i * pageSize;
            // 最后一条可能超出总数
            int end = start + pageSize > total ? total : start + pageSize;
            List<T> subList = list.subList(start, end);
            listArr.add(subList);
        }

        return listArr;
    }

    /**
     * 拆分list成n个小list,每个小的最大长度限制
     * @author:  
     * @param list
     * @param pageSize
     * @return
     */
    public static final <T> List<List<T>> splitNewList(List<T> list, int pageSize){
        List<List<T>> listArr = new ArrayList<List<T>>();
        if(list.size() <= pageSize){
            // 不需要拆分
            listArr.add(new ArrayList<T>(list));
            return listArr;
        }
        int total = list.size();
        // 获取被拆分的数组个数
        int pageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        for(int i = 0; i < pageCount; i++){
            int start = i * pageSize;
            // 最后一条可能超出总数
            int end = start + pageSize > total ? total : start + pageSize;
            List<T> subList = list.subList(start, end);
            listArr.add(new ArrayList<T>(subList));
        }

        return listArr;
    }

}
