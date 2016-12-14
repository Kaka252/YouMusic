package com.zhouyou.remote;

import com.zhouyou.remote.Music;

interface IMusicControlInterface {

    // 初始化
    void init();
    // 播放
    void play(in Music music);
}
