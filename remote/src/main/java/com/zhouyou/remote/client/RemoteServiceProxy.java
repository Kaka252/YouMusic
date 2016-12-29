package com.zhouyou.remote.client;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.server.MusicService;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 * 远程服务代理
 */
public class RemoteServiceProxy {

    private static final String TAG = RemoteServiceProxy.class.getSimpleName();
    private Context context;

    private IMusicControlInterface mIMusicControlInterface;
    private IMusicReceiver receiver;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mIMusicControlInterface = IMusicControlInterface.Stub.asInterface(service);
            try {
                mIMusicControlInterface.registerReceiver(receiver);
                mIMusicControlInterface.init();
            } catch (RemoteException e) {
                e.printStackTrace();
                mIMusicControlInterface = null;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected");
            mIMusicControlInterface = null;
        }
    };

    RemoteServiceProxy(Context context) {
        this.context = context;
    }

    void setReceiver(IMusicReceiver receiver) {
        this.receiver = receiver;
    }

    /**
     * 开启音乐后台服务
     */
    void startMusicBackgroundService() {
        startMusicService();
        bindMusicService();
    }

    private void startMusicService() {
        Intent intent = new Intent(context, MusicService.class);
        context.startService(intent);
    }

    private void bindMusicService() {
        Intent intent = new Intent(context, MusicService.class);
        context.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
    }

    /**
     * 检查服务是否连接
     *
     * @return
     */
    private boolean isConnected() {
        return mIMusicControlInterface != null;
    }


    /**
     * 设置播放列表，并播放音乐
     */
    synchronized void playMusicList(Intent data) {
        if (isConnected()) {
            try {
                mIMusicControlInterface.playMusicList(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 切换播放的状态
     *
     * @param action
     */
    synchronized void doMediaPlayerAction(Intent action) {
        if (isConnected()) {
            try {
                mIMusicControlInterface.doMediaPlayerAction(action);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置播放模式
     *
     * @param mode
     */
    void setMode(int mode) {
        if (isConnected()) {
            try {
                mIMusicControlInterface.setMode(mode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
