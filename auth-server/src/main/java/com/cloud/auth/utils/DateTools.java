package com.cloud.auth.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTools {
    /**
     * 获取当前系统时间
     */
    public static String getSysTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }
    /**
     * 获取当前日期
     */
    public static String getSysDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }
    /**
     * 获取当前日期
     */
    public static String getSysDateBy(String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * 截取时间的日期字符串
     */
    public static String toDateString(String dateStr) {
        if(dateStr == null || dateStr.length()<10) return dateStr;
        return dateStr.substring(0, 10);
    }
}
