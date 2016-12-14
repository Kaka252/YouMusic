package com.zhouyou.remote.client;

import android.content.Context;

import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;

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
    private Sender sender;
    private Receiver receiver;

    private MusicServiceSDK() {
        receiver = new Receiver();
        proxy = new RemoteServiceProxy(mContext);
        proxy.setReceiver(receiver);
        sender = new Sender(proxy);
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
        sender.play(audioPath, currentPosition);
    }

    /**
     * 暂停
     */
    public void pause() {
        sender.pause();
    }

    /**
     * 继续播放
     */
    public void resume() {
        sender.resume();
    }

    /**
     * 完成播放
     */
    public void complete() {
        sender.complete();
    }
}
