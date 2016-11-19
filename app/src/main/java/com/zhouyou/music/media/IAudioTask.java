package com.zhouyou.music.media;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 */
public interface IAudioTask {

    /**
     * 继续播放
     */
    void resume();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 播放下一个
     */
    void playNext();

    /**
     * 播放上一个
     */
    void playBack();



}
