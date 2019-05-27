package com.cloud.auth.entity;

import com.cloud.auth.utils.Base64Util;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Res implements Serializable {
    private int flag = 1;
    private String errorMsg;
    //会base64 加密传输 防止特殊字符
    private String result;
    private String token;

    public static Res failure(String errorMsg){
        Res res = new Res();
        res.setFlag(0);
        res.setErrorMsg(errorMsg);
        return res;
    }

    public static Res succeed(String result){
        Res res = new Res();
        res.setResult(Base64Util.encode(result));
        return res;
    }

    public static void decode(Res res){
        res.setResult(Base64Util.decode(res.getResult()));
    }
}
