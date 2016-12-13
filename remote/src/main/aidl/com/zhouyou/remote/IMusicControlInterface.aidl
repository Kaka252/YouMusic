// IMusicControlInterface.aidl
package com.zhouyou.remote;

// Declare any non-default types here with import statements

interface IMusicControlInterface {

    // 播放
    void play();
    // 播放下一首
    void playNext();
    // 播放上一首
    void playBack();
    // 暂停
    void pause();
    // 继续播放
    void resume();
}
