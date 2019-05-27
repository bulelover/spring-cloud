/**
 * Copyright (c) 2011-2015 zcframework.org (jetyou@foxmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloud.auth.utils;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Base64双向加密工具
 *
 * @author xianannan 20180404
 */
public class Base64Util {

    private static String GB2312_CHARSET = "GB2312";
    private static String GBK_CHARSET = "GBK";
    private static String UTF8_CHARSET = "UTF-8";

    /**
     * UTF8_CHARSET 方式加密
     * @param str 入参
     * @return String
     */
    public static String encode(String str)  {
        return encode(str,UTF8_CHARSET,UTF8_CHARSET);
    }
    public static void encode(Object str)  {
        encode(str, UTF8_CHARSET, UTF8_CHARSET);
    }
    /**
     * 循环加密
     * @param  arg0 用【arg0】字符集编码 去加密
     * @param arg1 最终 转换成【arg1】的字符集编码 字符串返回
     * @return String
     */
    public static void encode(Object o,String arg0, String arg1)  {
        if(o instanceof Map){
            Map objectMap = ((Map) o);
            //如果是list进入递归
            for (Object key: objectMap.keySet())
            {
                Object value = objectMap.get(key);
                //当前属性如果是list则继续进入递归
                if (value instanceof List || value instanceof Map ) {
                    encode(value,arg0,arg1);
                }else {
                    if (value instanceof String) {
                        objectMap.put(key, encode((String) value, arg0, arg1));
                    }
                }
            }
        }else if(o instanceof List){
            // 如果是list进入递归
            for (Object item :(List) o) {
                encode(item,arg0,arg1);
            }
        }else {
            //不是list 循环修改每一个属性
            //判断是否有父类
            List<Field> fields = new ArrayList<Field>();
            for(Class<?> clazz = o.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {
                List<Field> tmp = Arrays.asList(clazz.getDeclaredFields());
                fields.addAll(tmp);
            }
            for(Field field:fields) {
                try {
                    //设置可以访问
                    field.setAccessible(true);
                    Object item = field.get(o);
                    //当前属性如果不是字符串进入递归
                    if(item instanceof List || item instanceof Map) {
                        encode(item, arg0, arg1);
                    }else {
                        field.set(o, encode(String.valueOf(item), arg0, arg1));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * UTF8_CHARSET 方式解密
     * @param str 入参
     * @return String
     */
    public static String decode(String str)  {
        return decode(str, UTF8_CHARSET, UTF8_CHARSET);
    }

    public static void decode(Object str)  {
        decode(str, UTF8_CHARSET, UTF8_CHARSET);
    }
    /**
     * 循环解密
     *  @param arg0 用【arg0】字符集编码 去解密
     *  @param arg1 最终 转换成【arg1】的字符集编码 的 字符串 返回
     * @return String
     */
    public static void decode(Object o,String arg0, String arg1)  {
        if(o instanceof Map){
            Map objectMap = ((Map) o);
            //如果是list进入递归
            for (Object key: objectMap.keySet())
            {
                Object value = objectMap.get(key);
                //当前属性如果是list则继续进入递归
                if (value instanceof List || value instanceof Map ) {
                    decode(value, arg0, arg1);
                }else {
                    if (value instanceof String) {
                        objectMap.put(key, decode((String) value, arg0, arg1));
                    }
                }
            }
        }else if(o instanceof List){
            // 如果是list进入递归
            for (Object item :(List) o) {
                decode(item, arg0, arg1);
            }
        }else {
            //不是list 循环修改每一个属性
            //判断是否有父类
            List<Field> fields = new ArrayList<Field>();
            for(Class<?> clazz = o.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {
                List<Field> tmp = Arrays.asList(clazz.getDeclaredFields());
                fields.addAll(tmp);
            }
            for(Field field:fields) {
                try {
                    //设置可以访问
                    field.setAccessible(true);
                    Object item = field.get(o);
                    //当前属性如果不是字符串进入递归
                    if(item instanceof List || item instanceof Map) {
                        decode(item, arg0, arg1);
                    }else {
                        field.set(o, decode(String.valueOf(item), arg0, arg1));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     *  加密
     *  @param arg0 用【arg0】字符集编码 去加密
     *  @param arg1 最终 转换成【arg1】的字符集编码 字符串返回
     */
    public static String encode(String str, String arg0, String arg1)  {
        if (StringUtils.isEmpty(str)){
            return str;
        }
        String deStr ="";
        try {
            byte[] bs = org.apache.commons.codec.binary.Base64.encodeBase64(str.getBytes(arg0));
            deStr = new String(bs, arg1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return deStr;
    }
    /**
     *  解密
     *  @param arg0 用【arg0】字符集编码 去解密
     *  @param arg1 最终 转换成【arg1】的字符集编码 的 字符串 返回
     */
    public static String decode(String str, String arg0, String arg1) {
        if (StringUtils.isEmpty(str))
            return str;
        String deStr ="";
        try {
            byte[] bs = org.apache.commons.codec.binary.Base64.decodeBase64(str.getBytes(arg0));
            deStr = new String(bs, arg1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return deStr;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        String str = "氯化钠注射液";
        String str1 = encode(str);
        String str2 = decode(str1);
        String s1 = "氯化钠注射液";
        String s2="注射液(塑瓶)";
        String s3="剂";
        System.out.println("加密前："+str+"======================");
        System.out.println("加密后："+str1+"======================");
        System.out.println("解密后："+str2+"======================");
        System.out.println("s1:"+encode(s1));
        System.out.println("s2:"+encode(s2));
        System.out.println("s3:"+encode(s3));
//        baseFile("F:/1_t_D",1);
    }

    /**
     *
     * @param filePath
     * @param opt 0:code 1:decode
     */
    public static void baseFile(String filePath, int opt){
        File f = new File(filePath);
        String outName = opt==0?filePath+"_D" : filePath+"_C";
        File outF = new File(outName);
        if(f.exists()){
            FileInputStream fis = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            StringBuffer sb = new StringBuffer();
            FileWriter fw= null;
            try {
                String str = "";
                fis = new FileInputStream(f);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
                fw = new FileWriter(outF);

                while ((str = br.readLine()) != null) {
                    String[] words = str.split(",");
                    for(int i = 0; i< words.length; i++) {
                        if(opt == 0) {
                            sb.append(encode(words[i]));
                        }else{
                            sb.append(decode(words[i]));
                        }
                        if(i < words.length-1){
                            sb.append(",");
                        }
                    }
                    sb.append('\n');
                }
                fw.write(sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                 try {
                     if(null!=br) br.close();
                     if(null!=isr) isr.close();
                     if(null!=fis) fis.close();
                     if(null!=fw) fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(sb.toString());
            System.out.println(decode("1No="));
        }else {
            System.out.println("文件不存在！");
        }
    }
}
