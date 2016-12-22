package com.zhouyou.remote.client;

import android.content.Intent;
import android.os.Bundle;

import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;
import com.zhouyou.remote.constants.MusicConstants;

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
     * 手动更新播放进度
     */
//    void seekTo(int seekPosition) {
//        Intent intent = MusicStateMessageFactory.createMusicStateMessage(State.PREPARED, seekPosition);
//        proxy.doMediaPlayerAction(intent);
//    }
}
