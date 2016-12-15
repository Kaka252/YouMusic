package com.zhouyou.remote.client.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 */
public class MusicStatePublisher implements IMusicStatePublisher {

    private final Object lock = new Object();

    // 存放观察者
    private List<IMusicStateSubscriber> subscribers;

    public MusicStatePublisher() {
        subscribers = new ArrayList<>();
    }

    @Override
    public void register(IMusicStateSubscriber subscriber) {
        synchronized (lock) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unregister(IMusicStateSubscriber subscriber) {
        synchronized (lock) {
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void notifySubscribers() {
        synchronized (lock) {
            for (IMusicStateSubscriber subscriber : subscribers) {
                subscriber.onUpdateChange();
            }
        }
    }
}
