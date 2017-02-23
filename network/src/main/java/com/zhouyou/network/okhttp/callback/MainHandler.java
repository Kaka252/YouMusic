package com.zhouyou.network.okhttp.callback;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhouyou on 17/2/24.
 */

public class MainHandler {

    private static volatile MainHandler instance;
    private MainHandlerExecutor executor;

    private static class MainHandlerExecutor implements Executor {

        private static final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            handler.post(runnable);
        }
    }

    private MainHandler() {
        if (executor == null) {
            executor = new MainHandlerExecutor();
        }
    }

    public static MainHandler getInstance() {
        if (instance == null) {
            synchronized (MainHandler.class) {
                if (instance == null) {
                    instance = new MainHandler();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
