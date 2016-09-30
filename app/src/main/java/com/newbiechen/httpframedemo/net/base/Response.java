package com.newbiechen.httpframedemo.net.base;

/**
 * Created by PC on 2016/9/29.
 */
public class Response {
    private int statusCode;
    private String content;

    public Response(int statusCode,String content){
        this.statusCode = statusCode;
        this.content = content;
    }

    /**
     * 获取网络返回的状态码
     * */
    public int getStatusCode(){
        return statusCode;
    }

    /**
     * 获取网络返回的数据
     * @return
     */
    public String getContent(){
        return content;
    }
}


