package com.newbiechen.httpframedemo.net.utils;

import com.newbiechen.httpframedemo.net.base.Request;
import com.newbiechen.httpframedemo.net.base.RequestParameter;
import com.newbiechen.httpframedemo.net.base.UrlData;
import com.newbiechen.httpframedemo.net.work.RequestQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2016/10/1.
 * 作用：处理Request请求。
 */
public class RemoteService {

    private static RemoteService sRemoteService;

    private RemoteService(){
    }

    public static RemoteService getInstance(){
        synchronized (RemoteService.class){
            if (sRemoteService == null){
                sRemoteService = new RemoteService();
            }
        }
        return sRemoteService;
    }


    /**
     * 启动网络连接（该连接只能实现非表单的GET、POST请求）
     * @param name   xml中Node的名字
     * @param parameters  提交的参数键值对
     * @param callback  回调
     */
    public void remoteConn(String name, List<RequestParameter> parameters, Request.RequestCallback callback){
        this.remoteConn(name,new ArrayList<RequestParameter>(),parameters,callback);
    }

    public void remoteConn(String name, List<RequestParameter> headers, List<RequestParameter> parameters, Request.RequestCallback callback){
        UrlData urlData = UrlManager.parseUrlData(name);
        Request request = new Request(urlData.getUrlPath(),urlData.getRequestType());
        //赋值数据
        for (RequestParameter headerParam : headers){
            request.addHeaderParam(headerParam.getKey(),headerParam.getValue());
        }
        for (RequestParameter bodyParam : parameters){
            request.addBodyParam(bodyParam.getKey(),bodyParam.getValue());
        }

        RequestQueue.getInstance().addRequest(request);
    }
}
