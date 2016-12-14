package com.zhouyou.remote;

import com.zhouyou.remote.Music;

interface IMusicControlInterface {

    // 初始化
    void init();
    // 播放
    void play(in Music music);
    // 切换MediaPlayer的播放状态
    void switchMediaState(int state);
}
