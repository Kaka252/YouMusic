package com.zhouyou.music.service;

import android.media.MediaPlayer;
import android.os.Binder;

import com.zhouyou.music.media.AudioPlayState;

/**
 * 作者：ZhouYou
 * 日期：2016/11/19.
 */
public class AudioTask extends Binder implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private int currState;

    private MediaPlayer mediaPlayer;

    public AudioTask() {
        init();
    }

    private void init() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            changeState(AudioPlayState.IDLE);
        } else {
            if (isReset()) {
                mediaPlayer.reset();
                changeState(AudioPlayState.IDLE);
            }
        }
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    /**
     * 改变音乐的播放状态
     *
     * @param state
     */
    public void changeState(int state) {
        currState = state;
    }

    /**
     * 重置状态
     *
     * @return
     */
    private boolean isReset() {
        return currState == AudioPlayState.IDLE || currState == AudioPlayState.INITIALIZED || currState == AudioPlayState.PREPARED ||
                currState == AudioPlayState.STARTED || currState == AudioPlayState.PAUSED || currState == AudioPlayState.STOPPED ||
                currState == AudioPlayState.COMPLETED || currState == AudioPlayState.ERROR;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
