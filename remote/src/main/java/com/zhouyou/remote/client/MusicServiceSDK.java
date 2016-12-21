package com.zhouyou.remote.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhouyou.remote.Music;

import java.util.ArrayList;
import java.util.List;

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
     * 初始化播放列表
     *
     * @param data 数据
     */
    public void playMusicList(Intent data) {
        sender.playMusicList(data);
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
    public void complete(boolean isPlayBack) {
        sender.complete(isPlayBack);
    }

    /**
     * 手动更新播放进度
     */
    public void seekTo(int seekPosition) {
        sender.seekTo(seekPosition);
    }

    /**
     * 获取音频播放器的播放状态
     *
     * @return
     */
    public int getState() {
        return receiver.getCurrState();
    }

    /**
     * 获取音频播放器当前播放的音乐的路径
     *
     * @return
     */
    public String getMusicPath() {
        return receiver.getCurrMusicPath();
    }

    /**
     * 获取音频播放器当前播放的音乐的播放进度
     *
     * @return
     */
    public int getMusicCurrentPosition() {
        return receiver.getCurrPlayingPosition();
    }

    /**
     * 获取音频播放器当前播放的音乐的播放时长
     *
     * @return
     */
    public int getMusicDuration() {
        return receiver.getCurrPlayingDuration();
    }

    /**
     * 是否初始化播放列表
     *
     * @return
     */
    public boolean hasInitializedPlayList() {
        return receiver.hasInitializedPlayList();
    }
}
