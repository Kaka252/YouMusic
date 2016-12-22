package com.zhouyou.music.media;

/**
 * Created by zhouyou on 16/12/18.
 */

public interface OnMusicPlayingActionListener {

    void onMusicPlay(int playAction, int seekPosition);

    void onMusicPause();

    void onMusicResume(int seekPosition);

}
