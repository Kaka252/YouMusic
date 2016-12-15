package com.zhouyou.music.media.state;

import com.zhouyou.music.entity.Audio;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 * 音频状态的管理者（被观察者）
 */
@Deprecated
public interface IAudioStatePublisher {

    void register(IAudioStateSubscriber subscriber);

    void unregister(IAudioStateSubscriber subscriber);

    void notifySubscribers(Audio audio, int state);
}
