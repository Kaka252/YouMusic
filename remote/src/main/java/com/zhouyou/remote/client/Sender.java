package com.zhouyou.remote.client;

import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;

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
     * 播放
     *
     * @param audioId         播放的id
     * @param audioPath       播放的地址
     * @param currentPosition 播放的位置
     */
    public void play(int audioId, String audioPath, int currentPosition) {
        Music intent = new Music();
        intent.setMusicId(audioId);
        intent.setMusicPath(audioPath);
        intent.setCurrentPosition(currentPosition);
        proxy.play(intent);
    }

    /**
     * 暂停
     */
    public void pause() {
        proxy.switchMediaState(State.PAUSED);
    }

    /**
     * 继续播放
     */
    public void resume() {
        proxy.switchMediaState(State.PREPARED);
    }

    /**
     * 完成播放
     */
    public void complete() {
        proxy.switchMediaState(State.COMPLETED);
    }
}
