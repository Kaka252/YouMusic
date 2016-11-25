package com.zhouyou.music.media.state;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 */
public interface AudioPlayState {

    int IDLE = 0;
    int INITIALIZED = 1;
    int PREPARED = 2;
    int PREPARING = 3;
    int IN_PROGRESS = 4;
    int PAUSED = 5;
    int STOPPED = 6;
    int COMPLETED = 7;
    int END = -1;
    int ERROR = -2;
}
