package com.newbiechen.httpframedemo.net.base;

/**
 * Created by PC on 2016/9/28.
 */
public class RequestParameter {
    //防止别人篡改数据
    private final String key;
    private final String value;

    public RequestParameter(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }
}
