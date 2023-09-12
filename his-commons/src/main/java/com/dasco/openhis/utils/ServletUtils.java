package com.dasco.openhis.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }

    public static ServletRequestAttributes getRequestAttributes(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }
}
