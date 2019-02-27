package com.polypay.platform.controller;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.polypay.platform.Page;
import com.polypay.platform.utils.Tools;


@Component
public class BaseController<T> {

    protected Logger logger = Logger.getLogger(this.getClass());

//    //返回值success
//    public static String RESPONSE_SUCCESS = "success";

    /**
     * 得到request对象
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 从国际化资源配置文件中根据key获取value  方法一
     *
     * @param request
     * @param key
     * @return
     */
    public static String getMessage(HttpServletRequest request, String key) {

        try {
            Locale currentLocale = RequestContextUtils.getLocale(request);
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.message", currentLocale);
            return new String(bundle.getString(key).getBytes("iso-8859-1"), "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return key;
        }
    }

    /**
     * 得到ModelAndView
     */
    public ModelAndView getModelAndView(){
        return new ModelAndView();
    }

    /**
     * 获取PageBounds
     *
     * @return
     */
    public PageBounds getPageBounds() {
        //页面上显示的行数
        int defaultRows = 15;

        //页数
        int defalutPage = 1;

        String rows = getRequest().getParameter("limit");
        String page = getRequest().getParameter("page");
        if (!Tools.isEmpty(rows))
            defaultRows = new Integer(rows);
        if (!Tools.isEmpty(page))
            defalutPage = new Integer(page);
        return new PageBounds(defalutPage, defaultRows, true);
    }

    public Page<T> getPageData(PageList<T> pageInfoList) {
        Paginator paginator = pageInfoList.getPaginator();
        Page<T> pageBean = new Page<T>();
        pageBean.setTotal(paginator.getTotalCount());
        pageBean.setPageSize(paginator.getTotalPages());
        pageBean.setPageIndex(paginator.getPage());
        pageBean.setRows(pageInfoList);
        return pageBean;
    }

    public Map<String, Object> returnStatus(Integer code, String msg) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }
 
}
