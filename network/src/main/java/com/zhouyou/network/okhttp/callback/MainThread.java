package com.zhouyou.network.okhttp.callback;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by zhouyou on 17/2/24.
 */

public class MainThread {

    private static volatile MainThread instance;
    private MainHandlerExecutor executor;

    private static class MainHandlerExecutor implements Executor {

        private static final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            handler.post(runnable);
        }
    }

    private MainThread() {
        if (executor == null) {
            executor = new MainHandlerExecutor();
        }
    }

    public static MainThread getInstance() {
        if (instance == null) {
            synchronized (MainThread.class) {
                if (instance == null) {
                    instance = new MainThread();
                }
            }
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
