package com.zhouyou.remote.client;

import android.content.Intent;
import android.os.Bundle;

import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 * 信号发送者
 */
public class Sender {

    private RemoteServiceProxy proxy;

    public Sender(RemoteServiceProxy proxy) {
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
        proxy.switchMediaState(State.PAUSED);
    }

    /**
     * 继续播放
     */
    void resume() {
        proxy.switchMediaState(State.PREPARED);
    }

    /**
     * 完成播放
     */
    void complete(boolean isPlayBack) {
        proxy.switchMediaState(State.COMPLETED);
    }
}
