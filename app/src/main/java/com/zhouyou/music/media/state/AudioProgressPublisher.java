package com.zhouyou.music.media.state;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/25.
 */
public class AudioProgressPublisher implements IAudioProgressPublisher {

    private final Object lock = new Object();

    // 存放观察者
    private List<IAudioProgressSubscriber> subscribers;

    public AudioProgressPublisher() {
        subscribers = new ArrayList<>();
    }

    @Override
    public void register(IAudioProgressSubscriber subscriber) {
        synchronized (lock) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unregister(IAudioProgressSubscriber subscriber) {
        synchronized (lock) {
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void notifySubscribers(int currentPosition, int duration) {
        synchronized (lock) {
            for (IAudioProgressSubscriber subscriber : subscribers) {
                subscriber.onProgressChange(currentPosition, duration);
            }
        }
    }
}
