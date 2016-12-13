// IMusicControlInterface.aidl
package com.zhouyou.remote;

import com.zhouyou.remote.Music;

interface IMusicControlInterface {

    // 初始化
    void init();
    // 播放
    boolean play(in Music music);
    // 改变音乐播放的状态
    void changeMusicState(int state);
}
