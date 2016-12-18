package com.zhouyou.music.media;

/**
 * Created by zhouyou on 16/12/18.
 */

public interface OnMusicPlayingActionListener {

    void onMusicPlay();

    void onMusicPause();

    void onMusicResume();

    void onMusicComplete(boolean isPlayBack);

}
