package com.zhouyou.remote.client;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.server.MusicService;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 * 远程服务代理
 */
public class RemoteServiceProxy {

    private Context context;

    private IMusicControlInterface mIMusicControlInterface;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mIMusicControlInterface = IMusicControlInterface.Stub.asInterface(service);
            try {
                mIMusicControlInterface.init();
            } catch (RemoteException e) {
                e.printStackTrace();
                mIMusicControlInterface = null;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIMusicControlInterface = null;
        }
    };

    public RemoteServiceProxy(Context context) {
        this.context = context;
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

    private boolean isConnected() {
        return mIMusicControlInterface != null;
    }

    /**
     * 播放音乐
     * @param music
     * @return
     */
    public synchronized void play(Music music) {
        if (isConnected()) {
            try {
                mIMusicControlInterface.play(music);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 切换播放的状态
     * @param state
     */
    public synchronized void switchMediaState(int state) {
        if (isConnected()) {
            try {
                mIMusicControlInterface.switchMediaState(state);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
