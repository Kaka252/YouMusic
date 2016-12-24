package com.zhouyou.library.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by monch on 16/7/9.
 */
public class PoolUtils {

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2 + 1;
    private static final AtomicLong THREAD_INDEX = new AtomicLong(0);
    /**
     * 统一线程池处理器
     */
    public static final ExecutorService POOL =
            Executors.newFixedThreadPool(CORE_POOL_SIZE, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "AppThread-Thread-" + THREAD_INDEX.getAndIncrement());
                }
            });

}
