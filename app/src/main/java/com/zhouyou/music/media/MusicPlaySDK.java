package com.zhouyou.music.media;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.music.base.App;
import com.zhouyou.music.config.Constants;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.music.entity.Audio;

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
        currAudio = ListUtils.getElement(getPlayList(), 0);
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
        Intent intent = new Intent(Constants.RECEIVER_AUDIO_STATE_CHANGE);
        intent.putExtra(Constants.DATA_INT, currState);
        intent.putExtra(Constants.DATA_ENTITY, currAudio);
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
        if (audio == null) {
            changeState(AudioPlayState.ERROR);
            return;
        }
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
                currAudio = audio;
                changeState(AudioPlayState.PREPARING);
            }
        } catch (Exception e) {
            e.printStackTrace();
            changeState(AudioPlayState.ERROR);
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
     * 播放下一首
     */
    public void playNext() {
        // TODO 单曲播放

        // TODO 随机播放

        // TODO 顺序播放

        // TODO 循环播放
        List<Audio> list = getPlayList();
        if (ListUtils.isEmpty(list)) return;
        if (currAudio == null) {
            currAudio = ListUtils.getElement(list, 0);
        } else {
            int index = 0;
            for (Audio audio : list) {
                if (audio == null) continue;
                if (audio.id == currAudio.id) {
                    index = list.indexOf(audio) + 1;
                    if (index >= list.size()) {
                        index = 0;
                    }
                }
            }
            currAudio = ListUtils.getElement(list, index);
        }
        prepare(currAudio);
    }

    /**
     * 播放上一首
     */
    public void playBack() {
        // TODO 单曲播放

        // TODO 随机播放

        // TODO 循环播放
        List<Audio> list = getPlayList();
        if (ListUtils.isEmpty(list)) return;
        if (currAudio == null) {
            currAudio = ListUtils.getElement(list, 0);
        } else {
            int index = 0;
            for (Audio audio : list) {
                if (audio == null) continue;
                if (audio.id == currAudio.id) {
                    index = list.indexOf(audio) - 1;
                    if (index < 0) {
                        index = list.size() - 1;
                    }
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
        currAudio = ListUtils.getElement(getPlayList(), 0);
    }

    /**
     * 获取播放列表
     *
     * @return
     */
    public List<Audio> getPlayList() {
        List<Audio> audioList = AudioLocalDataManager.get().getAudioCacheList();
        if (ListUtils.isEmpty(audioList)) {
            audioList = AudioLocalDataManager.get().getAudioList();
        }
        return audioList;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        changeState(AudioPlayState.ERROR);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        play();
    }
}
