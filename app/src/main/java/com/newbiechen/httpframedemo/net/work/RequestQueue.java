package com.newbiechen.httpframedemo.net.work;

import com.newbiechen.httpframedemo.net.base.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by PC on 2016/9/29.
 * 利用单例模式创建请求队列。
 */
public class RequestQueue{
    //创建线程池
    private static final NetThreadPool THREAD_POOL = new NetThreadPool();
    private static RequestQueue sRequestQueue;

    private final List<Future> mFutureList = new ArrayList<>();
    private RequestQueue(){
    }
    /**
     * 单例模式
     * */
    public static RequestQueue getInstance(){
        synchronized (RequestQueue.class){
            if (sRequestQueue == null){
                sRequestQueue = new RequestQueue();
            }
        }
        return sRequestQueue;
    }


    /**
     * 添加请求到线程池
     *
     * */
    public void addRequest(Request request){
        //加入线程池
        Future future = THREAD_POOL.submit(new NetWork(request));
        mFutureList.add(future);
    }

    /***
     * 移除所有队列中，或者正在运行的Request请求
     * */

    public void removeAll(){
        //移除所有队列中的请求
        THREAD_POOL.removeRunnable();
        //取向当前正在执行的请求
        for (Future future : mFutureList){
            future.cancel(false);
        }

    }

}
