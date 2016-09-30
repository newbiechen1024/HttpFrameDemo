package com.newbiechen.httpframedemo.net.work;

/**
 * Created by PC on 2016/9/29.
 */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义网络线程池
 * */
public class NetThreadPool extends ThreadPoolExecutor {

    //核心线程数
    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors()+1;
    //最大线程数
    private static final int MAX_SIZE = 2*CORE_SIZE;
    //非核心线程数的存在时常
    private static final long KEEP_TIME = 3000;
    //
    private static final int MAX_QUEUE_SIZE = 100;
    //创建线程安全的容器，并设置队列的最大数
    private static final BlockingQueue<Runnable> sQueue = new PriorityBlockingQueue<>(MAX_QUEUE_SIZE);
    //创建线程工厂
    private static final ThreadFactory sFactory = new ThreadFactory() {
        private final AtomicInteger atomicInteger = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            String name = atomicInteger.getAndIncrement()+"";
            return new Thread(r,name);
        }
    };

    public NetThreadPool() {
        super(CORE_SIZE,MAX_SIZE,KEEP_TIME,TimeUnit.MILLISECONDS, sQueue, sFactory);
    }

    public void removeRunnable(){
        //移除存储在队列中的Runnable
        sQueue.poll();
    }
}

