package com.zhouyou.remote;

/**
 * Created by zhouyou on 16/12/24.
 */

public interface Mode {

    /**
     * 顺序播放（默认）
     */
    int MODE_SEQUENCE_PLAY = 0;
    /**
     * 全部循环播放
     */
    int MODE_CYCLE_ALL_PLAY = 1;
    /**
     * 单曲循环
     */
    int MODE_SINGLE_PLAY = 2;
    /**
     * 随机播放
     */
    int MODE_RANDOM_PLAY = 3;
}
