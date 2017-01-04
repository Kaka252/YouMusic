package com.zhouyou.remote;

import com.zhouyou.remote.IMusicReceiver;

interface IMusicControlInterface {

    // 初始化
    void init();
    // 播放
    void playMusic(in Intent data);
    // 切换MediaPlayer的播放状态
    void doMediaPlayerAction(in Intent action);
    // 注册播放状态的返回
    void registerReceiver(IMusicReceiver receiver);
}
