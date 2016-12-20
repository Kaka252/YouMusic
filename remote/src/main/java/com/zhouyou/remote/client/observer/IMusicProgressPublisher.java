package com.zhouyou.remote.client.observer;

/**
 * Created by zhouyou on 16/12/21.
 */

public interface IMusicProgressPublisher {

    void register(IMusicProgressSubscriber subscriber);

    void unregister(IMusicProgressSubscriber subscriber);

    /**
     * 通知观察者
     *
     * @param currentPosition 已播放
     * @param duration        时长
     */
    void notifySubscribers(int currentPosition, int duration);
}
