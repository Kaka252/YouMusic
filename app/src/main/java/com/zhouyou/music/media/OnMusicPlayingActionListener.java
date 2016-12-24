package com.zhouyou.music.media;

/**
 * Created by zhouyou on 16/12/18.
 */

public interface OnMusicPlayingActionListener {

    /**
     * 手动播放音乐
     *
     * @param playAction   0 - 当前 | 1 - 下一首 | 2 - 上一首
     * @param seekPosition 进度控制
     */
    void onMusicPlay(int playAction, int seekPosition);

    /**
     * 音乐暂停
     */
    void onMusicPause();

    /**
     * 音乐继续
     *
     * @param seekPosition 进度控制
     */
    void onMusicResume(int seekPosition);

}
