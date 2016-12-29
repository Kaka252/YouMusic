package com.zhouyou.remote.client;

import android.content.Intent;

import com.zhouyou.remote.State;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 * 信号发送者
 */
public class Sender {

    private RemoteServiceProxy proxy;

    Sender(RemoteServiceProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * 设置播放列表，并播放音乐
     */
    void playMusicList(Intent data) {
        proxy.playMusicList(data);
    }

    /**
     * 暂停
     */
    void pause() {
        Intent intent = MusicStateMessageFactory.createMusicStateMessage(State.PAUSED);
        proxy.doMediaPlayerAction(intent);
    }

    /**
     * 继续播放
     */
    void resume(int seekPosition) {
        Intent intent = MusicStateMessageFactory.createMusicStateMessage(State.PREPARED, seekPosition);
        proxy.doMediaPlayerAction(intent);
    }

    /**
     * 完成播放
     */
    void complete(boolean isPlayBack) {
        Intent intent = MusicStateMessageFactory.createMusicControlMessage(State.COMPLETED, isPlayBack);
        proxy.doMediaPlayerAction(intent);
    }

    /**
     * 设置播放模式
     */
    void setMode(int mode) {
        proxy.setMode(mode);
    }
}
