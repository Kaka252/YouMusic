package com.zhouyou.music.media;

import com.zhouyou.music.entity.Audio;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 * 音频状态的接受者（被观察者）
 */
public interface IAudioStateSubscriber {

    /**
     * 通知状态改变
     *
     * @param audio 音频
     * @param state 状态
     */
    void notifyStateChanged(Audio audio, int state);
}
