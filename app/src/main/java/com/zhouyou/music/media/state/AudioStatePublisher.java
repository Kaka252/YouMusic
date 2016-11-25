package com.zhouyou.music.media.state;

import com.zhouyou.music.entity.Audio;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 */
public class AudioStatePublisher implements IAudioStatePublisher {

    private final Object lock = new Object();

    // 存放观察者
    private List<IAudioStateSubscriber> subscribers;

    public AudioStatePublisher() {
        subscribers = new ArrayList<>();
    }

    @Override
    public void register(IAudioStateSubscriber subscriber) {
        synchronized (lock) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unregister(IAudioStateSubscriber subscriber) {
        synchronized (lock) {
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void notifySubscribers(Audio audio, int state) {
        synchronized (lock) {
            for (IAudioStateSubscriber subscriber : subscribers) {
                subscriber.onUpdateChange(audio, state);
            }
        }
    }
}
