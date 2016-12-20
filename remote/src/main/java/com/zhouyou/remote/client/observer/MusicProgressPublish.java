package com.zhouyou.remote.client.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyou on 16/12/21.
 */

public class MusicProgressPublish implements IMusicProgressPublisher {

    private final Object lock = new Object();

    // 存放观察者
    private List<IMusicProgressSubscriber> subscribers;

    public MusicProgressPublish() {
        subscribers = new ArrayList<>();
    }

    @Override
    public void register(IMusicProgressSubscriber subscriber) {
        synchronized (lock) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unregister(IMusicProgressSubscriber subscriber) {
        synchronized (lock) {
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void notifySubscribers(int currentPosition, int duration) {
        synchronized (lock) {
            for (IMusicProgressSubscriber subscriber : subscribers) {
                subscriber.onProgressChange(currentPosition, duration);
            }
        }
    }
}
