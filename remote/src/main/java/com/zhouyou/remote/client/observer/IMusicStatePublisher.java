package com.zhouyou.remote.client.observer;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 */
public interface IMusicStatePublisher {

    void register(IMusicStateSubscriber subscriber);

    void unregister(IMusicStateSubscriber subscriber);

    void notifySubscribers(int state);
}
