package com.zhouyou.music.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.base.App;
import com.zhouyou.music.config.Constants;
import com.zhouyou.music.data.AudioLocalDataManager;
import com.zhouyou.music.entity.Audio;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/21.
 */
public class MusicPlaySDK implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {


    private Context context;
    private int currState;
    private Audio currAudio;
    private MediaPlayer mediaPlayer;


    private static class SDKHolder {
        private static final MusicPlaySDK SDK = new MusicPlaySDK();
    }

    public static MusicPlaySDK get() {
        return SDKHolder.SDK;
    }

    /**
     * 获取当前音乐的播放状态
     *
     * @return
     */
    public int getCurrState() {
        return currState;
    }

    /**
     * 获取当前音乐的实体
     *
     * @return
     */
    public Audio getCurrAudio() {
        return currAudio;
    }

    private MusicPlaySDK() {
        context = App.get().getApplicationContext();
    }

    /**
     * 初始化
     */
    public void init() {
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
    private void changeState(int state) {
        currState = state;
        sendPlayStateBroadcast();
    }

    /**
     * 发送一个更改状态的广播
     */
    private void sendPlayStateBroadcast() {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA_INT, currState);
        intent.putExtra(Constants.DATA_ENTITY, currAudio);
        intent.setAction(Constants.RECEIVER_AUDIO_STATE_CHANGE);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
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

    /**
     * 开始播放音频
     *
     * @param audio
     */
    public void prepare(Audio audio) {
        init();
        try {
            if (currState == AudioPlayState.IDLE) {
                Uri uri = Uri.parse(audio.path);
                mediaPlayer.setDataSource(context, uri);
            }
            changeState(AudioPlayState.INITIALIZED);
            if (currState != AudioPlayState.ERROR) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            if (currState == AudioPlayState.INITIALIZED || currState == AudioPlayState.STOPPED) {
                mediaPlayer.prepareAsync();
                this.currAudio = audio;
                changeState(AudioPlayState.PREPARING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    public void play() {
        mediaPlayer.start();
        changeState(AudioPlayState.STARTED);
    }

    /**
     * 暂停
     */
    public void pause() {
        mediaPlayer.pause();
        changeState(AudioPlayState.PAUSED);
    }

    /**
     * 停止
     */
    public void stop() {
        mediaPlayer.stop();
        changeState(AudioPlayState.STOPPED);
    }

    /**
     * 下一首
     */
    public void next() {
        List<Audio> list = getAudioList();
        if (ListUtils.isEmpty(list)) return;
        if (currAudio == null) {
            currAudio = ListUtils.getElement(list, 0);
        } else {
            int index = 0;
            for (Audio audio : list) {
                if (audio == null) continue;
                if (audio.id == currAudio.id) {
                    index = list.indexOf(audio) + 1;
                }
            }
            currAudio = ListUtils.getElement(list, index);
        }
        prepare(currAudio);
    }


    /**
     * 获取上一次选中的音频文件
     *
     * @return
     */
    public void initLastSelectedAudio() {
        currAudio = ListUtils.getElement(getAudioList(), 0);
    }

    public List<Audio> getAudioList() {
        List<Audio> audioList = AudioLocalDataManager.get().getAudioCacheList();
        if (ListUtils.isEmpty(audioList)) {
            audioList = AudioLocalDataManager.get().getAudioList();
        }
        return audioList;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        play();
    }
}
