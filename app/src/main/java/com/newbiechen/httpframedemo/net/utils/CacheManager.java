package com.newbiechen.httpframedemo.net.utils;

import android.util.LruCache;

/**
 * Created by PC on 2016/9/30.
 * 缓存的存储
 */
public class CacheManager {
    private static final int MAX_SIZE = ((int) Runtime.getRuntime().maxMemory()/1024);
    private final LruCache<String,String> mLruCache;

    public CacheManager(){
        int cacheSize = MAX_SIZE/8;
        mLruCache = new LruCache<String,String>(cacheSize){
            @Override
            protected int sizeOf(String key, String value) {
                return value.length();
            }
        };
    }
}
