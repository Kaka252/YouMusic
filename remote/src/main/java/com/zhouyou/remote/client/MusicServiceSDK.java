package com.zhouyou.remote.client;

import android.content.Context;
import android.text.TextUtils;

import com.zhouyou.remote.Music;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class MusicServiceSDK {

    private static Context mContext;

    public static MusicServiceSDK get() {
        return MusicServiceProxy.SDK;
    }

    private static final class MusicServiceProxy {
        private static final MusicServiceSDK SDK = new MusicServiceSDK();
    }

    private RemoteServiceProxy proxy;

    private MusicServiceSDK() {
        proxy = new RemoteServiceProxy(mContext);
    }

    /**
     * 初始化音乐服务
     *
     * @param context
     * @return
     */
    public static MusicServiceSDK init(Context context) {
        if (context != null && mContext == null) {
            mContext = context.getApplicationContext();
        }
        return MusicServiceProxy.SDK;
    }

    /**
     * 开启音乐服务
     */
    public void startMusicService() {
        proxy.startMusicBackgroundService();
    }

    /**
     * 播放
     *
     * @param audioPath       播放的地址
     * @param currentPosition 播放的位置
     */
    public void play(String audioPath, int currentPosition) {
        Music intent = MusicMsgFactory.setupMusic(audioPath, currentPosition);
        proxy.play(intent);
    }

    public void switchMediaState(int state) {
        proxy.switchMediaState(state);
    }
}
