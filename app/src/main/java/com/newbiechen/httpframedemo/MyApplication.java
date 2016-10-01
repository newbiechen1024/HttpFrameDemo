package com.newbiechen.httpframedemo;

import android.app.Application;

import com.newbiechen.httpframedemo.net.utils.UrlManager;

/**
 * Created by PC on 2016/10/1.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化URLManager
        UrlManager.init(this);
    }
}
