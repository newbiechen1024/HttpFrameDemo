package com.newbiechen.httpframedemo.net.base;

/**
 * Created by PC on 2016/9/30.
 */
public class UrlData {


    //地址
    private String urlPath;
    //请求类型
    private String requestType;
    //缓存时间
    private long expired;

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    @Override
    public String toString() {
        return "UrlData{" +
                "expired=" + expired +
                ", urlPath='" + urlPath + '\'' +
                ", requestType='" + requestType + '\'' +
                '}';
    }
}

