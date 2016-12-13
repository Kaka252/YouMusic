// IMusicControlInterface.aidl
package com.zhouyou.remote;

import com.zhouyou.remote.Music;

interface IMusicControlInterface {

    // 初始化
    void init();
    // 播放
    boolean play(in Music music);
    // 播放下一首
    void playNext();
    // 播放上一首
    void playBack();
    // 暂停
    void pause();
    // 继续播放
    void resume();
}
