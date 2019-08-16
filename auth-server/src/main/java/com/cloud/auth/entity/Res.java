package com.cloud.auth.entity;

import com.alibaba.fastjson.JSON;
import com.cloud.auth.utils.Base64Util;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Res implements Serializable {
    private int flag = 1;
    private String errorMsg;
    //会base64 加密传输 防止特殊字符
    private String result;
    private String token;
    /**
     * 返回一个指定类型的集合
     */
    public List<?> list;
    /**
     * 返回一个Map对象（键值对模型）
     */
    public Map<String,Object> model;

    private static Res custom(int flag,String message,List<?> list,Map<String,Object> model){
        Res res = new Res();
        res.setFlag(flag);
        res.setErrorMsg(message);
        res.setList(list);
        res.setModel(model);
        return res;
    }

    public static Res custom(boolean flag, String errorMsg){
        int f = flag?1:0;
        if(flag){
            errorMsg = null;
        }
        return Res.custom(f,errorMsg);
    }

    public static Res custom(int flag,Object object){
        if(object == null){
            return Res.custom(flag,null, null,  null);
        }
        if(object instanceof String){
            return Res.custom(flag,(String) object,null,null);
        }
        if(object instanceof List){
            return Res.custom(flag,null, (List<?>) object,null);
        }

        if(object instanceof Map){
            return Res.custom(flag,null, null,(Map<String,Object>) object);
        }
        Map<String,Object> map = JSON.parseObject(JSON.toJSONString(object));
        return Res.custom(flag,null, null,  map);
    }

    public static Res success(Object object){
        return Res.custom(1,object);
    }

    public static Res success(){
        return Res.custom(1,null);
    }

    public static Res fail(Object object){
        return Res.custom(0,object);
    }

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
