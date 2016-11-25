package com.zhouyou.music.media.state;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 * 音频播放进度的发布者
 */
public interface IAudioProgressPublisher {

    void register(IAudioProgressSubscriber subscriber);

    void unregister(IAudioProgressSubscriber subscriber);

    /**
     * 通知观察者
     *
     * @param currentPosition 已播放
     * @param duration        时长
     */
    void notifySubscribers(int currentPosition, int duration);
}
