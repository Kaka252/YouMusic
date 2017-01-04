package com.zhouyou.remote.client.observer;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 */
public interface IMusicStateSubscriber {

    /**
     * 通知改变
     */
    void onUpdateChange(int state);
}
