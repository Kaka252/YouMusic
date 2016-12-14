package com.zhouyou.remote.client.observer;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 */
public interface IMusicStateSubscriber {

    /**
     * 通知状态改变
     * @param state 状态
     */
    void onUpdateChange(int state);
}
