package com.zhouyou.music.media.state;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 */
public interface IAudioProgressSubscriber {

    /**
     * 通知进度改变
     *
     * @param currentPosition 播放进度
     * @param duration        音频时长
     */
    void onProgressChange(int currentPosition, int duration);
}
