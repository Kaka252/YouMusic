package com.zhouyou.music.media;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 */
public class MediaConfig {

    /**
     * 当前播放状态
     */
    public int currState;
    /**
     * 当前播放的音频毫秒数
     */
    public int currentPosition;
    /**
     * 音频的时长
     */
    public int duration;

    /**
     * 进度是否由用户控制
     */
    public boolean isProgressControlledByUser = false;

    /**
     * 是否播放上一首
     */
    public boolean isPlayBack;

    public void setConfig() {

    }

}
