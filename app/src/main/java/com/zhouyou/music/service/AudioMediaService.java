package com.zhouyou.music.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zhouyou.music.media.IAudioTask;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
public class AudioMediaService extends Service {

    private boolean isServiceRunning;

    private static IAudioTask binder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AudioTask();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bindService(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isServiceRunning) {
            bindService(getApplicationContext());
        }
        isServiceRunning = true;
        return START_STICKY;
    }

    public static void startService(Context cxt) {
        if (cxt == null) return;
        Context context = cxt.getApplicationContext();
        Intent intent = new Intent(context, AudioMediaService.class);
        try {
            context.startService(intent);
        } catch (Throwable t) {
            // 未知异常
        }
    }

    private static void bindService(Context cxt) {
        if (cxt == null) return;
        Context context = cxt.getApplicationContext();
        AudioServiceConnection connection = new AudioServiceConnection();
        Intent intent = new Intent(context, AudioMediaService.class);
        try {
            context.bindService(intent, connection, BIND_AUTO_CREATE);
        } catch (Throwable t) {
            // 未知异常
        }
    }

    private static class AudioServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null && service instanceof IAudioTask) {
                binder = (IAudioTask) service;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        stopForeground(true);
        startService(getApplicationContext());
        super.onDestroy();
    }
}
