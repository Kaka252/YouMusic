package com.zhouyou.music.media;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public interface AudioPlayMode {

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
