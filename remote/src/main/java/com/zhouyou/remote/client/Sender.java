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
     * 播放
     *
     * @param audioId         播放的id
     * @param audioPath       播放的地址
     * @param currentPosition 播放的位置
     */
    void play(int audioId, String audioPath, int currentPosition) {
        Music intent = new Music();
        intent.setMusicId(audioId);
        intent.setMusicPath(audioPath);
        intent.setCurrentPosition(currentPosition);
        proxy.play(intent);
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
    void complete() {
        proxy.switchMediaState(State.COMPLETED);
    }
}
