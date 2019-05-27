package com.cloud.client.common.utils;

import org.apache.commons.lang.StringUtils;

public class SysUtil {
    public static String getErrMsg(String error){
        if(StringUtils.isBlank(error)){
            error="服务异常，拥挤或超时";
        }
        return error;
    }
}
