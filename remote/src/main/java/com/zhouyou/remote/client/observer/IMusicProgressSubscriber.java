package com.zhouyou.remote.client.observer;

/**
 * Created by zhouyou on 16/12/21.
 */

public interface IMusicProgressSubscriber {


    /**
     * 通知进度改变
     *
     * @param currentPosition 播放进度
     * @param duration        音频时长
     */
    void onProgressChange(int currentPosition, int duration);
}
