package com.dasco.openhis.utils;

import cn.hutool.core.date.DateUtil;

public class HisDateUtils {

    /**
     * 1--上午
     * 2--下午
     * 3-晚上
     * @return
     */
    public static String getCurrentTimeType(){
        int hour = DateUtil.hour(DateUtil.date(), true);
        if(hour>=6 && hour<=12){
            return "1";
        }else if(hour>12 && hour<= 18){
            return "2";
        }else{
            return "3";
        }
    }
}
