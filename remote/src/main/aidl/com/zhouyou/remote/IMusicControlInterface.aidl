package com.zhouyou.remote;

import com.zhouyou.remote.Music;
import com.zhouyou.remote.IMusicReceiver;

interface IMusicControlInterface {

    // 初始化
    void init();
    // 播放
    void playMusicList(in Intent data);
    // 切换MediaPlayer的播放状态
    void switchMediaState(int state);
    // 注册播放状态的返回
    void registerReceiver(IMusicReceiver receiver);
}
