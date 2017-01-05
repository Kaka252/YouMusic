package com.zhouyou.music.media;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.library.utils.PrefUtils;
import com.zhouyou.music.config.Constants;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.music.notification.NotificationReceiver;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;
import com.zhouyou.remote.constants.MusicConstants;

import java.util.List;
import java.util.Random;

/**
 * 作者：ZhouYou
 * 日期：2016/12/15.
 * 客户端的数据管理类
 */
public class ClientCoreSDK {

    private static final String TAG = "ClientCoreSDK";

    private static class ClientHolder {
        private static final ClientCoreSDK SDK = new ClientCoreSDK();
    }

    public static ClientCoreSDK get() {
        return ClientHolder.SDK;
    }

    private ClientCoreSDK() {

    }

    /**
     * 获取当前正在播放的音乐
     *
     * @return
     */
    public Audio getPlayingMusic() {
        String musicPath = getCurrentPlayingMusicPath();
        return AudioLocalDataManager.get().queryByPath(musicPath);
    }

    public void savePlayList(List<Audio> data) {
        AudioLocalDataManager.get().save(data);
    }

    /**
     * 获取播放列表
     *
     * @return
     */
    private List<Audio> getPlayList() {
        return AudioLocalDataManager.get().queryAll();
    }

    /**
     * 是否是当前播放的音乐
     *
     * @param path
     * @return
     */
    public boolean isPlayingCurrentMusic(String path) {
        return !TextUtils.isEmpty(path) && TextUtils.equals(path, getCurrentPlayingMusicPath());
    }

    public void saveCurrentPlayMusicPath(String path) {
        PrefUtils.put(Constants.SP_MUSIC_PATH, path);
    }

    /**
     * 获取当前正在播放的音乐路径
     *
     * @return
     */
    private String getCurrentPlayingMusicPath() {
        String path = MusicServiceSDK.get().getMusicPath();
        if (TextUtils.isEmpty(path)) {
            path = PrefUtils.getString(Constants.SP_MUSIC_PATH, "");
        }
        return path;
    }

    /**
     * 获取当前正在播放的音乐状态
     *
     * @return
     */
    public int getCurrentPlayingMusicState() {
        return MusicServiceSDK.get().getState();
    }

    /**
     * 获取当前正在播放的音乐时长
     *
     * @return
     */
    public int getCurrentPlayingMusicDuration() {
        int duration = 0;
        if (isMusicPlaying()) {
            duration = MusicServiceSDK.get().getMusicDuration();
        } else {
            Audio audio = getPlayingMusic();
            if (audio != null) {
                duration = audio.duration;
            }
        }
        return duration;
    }

    /**
     * 保存播放模式
     * @param playMode
     */
    public void savePlayMode(int playMode) {
        PrefUtils.put(Constants.SP_PLAY_MODE, playMode);
    }
    /**
     * 获取音乐播放模式
     *
     * @return
     */
    public int getPlayMode() {
        return PrefUtils.getInt(Constants.SP_PLAY_MODE, Mode.MODE_CYCLE_ALL_PLAY);
    }

    /**
     * 获取当前正在播放的音乐进度
     *
     * @return
     */
    public int getCurrentPlayingMusicPosition() {
        return MusicServiceSDK.get().getMusicCurrentPosition();
    }

    /**
     * 音乐是否正处于播放中
     *
     * @return
     */
    public boolean isMusicPlaying() {
        return getCurrentPlayingMusicState() == State.IN_PROGRESS;
    }

    /**
     * 生成播放列表，并开始播放指定音乐
     *
     * @param selectedMusic 播放歌曲
     * @param seekPosition  被指定的进度
     */
    public void playMusic(String selectedMusic, int seekPosition) {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putString(MusicConstants.MUSIC_SELECTED, selectedMusic);
        b.putInt(MusicConstants.MUSIC_PLAYING_POSITION, seekPosition);
        intent.putExtras(b);
        MusicServiceSDK.get().playMusicList(intent);
    }

    /**
     * 生成播放列表，并开始播放指定音乐
     *
     * @param selectedMusic 播放歌曲
     */
    public void playMusic(String selectedMusic) {
        playMusic(selectedMusic, -1);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        MusicServiceSDK.get().pause();
    }

    /**
     * 继续播放
     */
    public void resume(int seekPosition) {
        MusicServiceSDK.get().resume(seekPosition);
    }

    /**
     * 播放下一首
     */
    public Audio getNextOne() {
        List<Audio> playList = getPlayList();
        Audio currAudio = getPlayingMusic();
        if (ListUtils.isEmpty(playList) || currAudio == null) return null;
        int index = 0;
        int size = playList.size();

        for (int i = 0; i < size; i++) {
            Audio item = playList.get(i);
            if (item == null) continue;
            if (item.id == currAudio.id) {
                index = i;
                break;
            }
        }
        // 0. 循环播放
        int mode = getPlayMode();
        if (mode == Mode.MODE_CYCLE_ALL_PLAY) {
            if (index >= size - 1) {
                index = 0;
            } else {
                index += 1;
            }
        }
        // 1. 单曲循环
        else if (mode == Mode.MODE_SINGLE_PLAY) {
            if (getCurrentPlayingMusicPosition() != getCurrentPlayingMusicDuration()) {
                if (index >= size - 1) {
                    index = 0;
                } else {
                    index += 1;
                }
            }
        }
        // 2. 随机播放
        else if (mode == Mode.MODE_RANDOM_PLAY) {
            Random random = new Random();
            index = random.nextInt(size) - 1;
            if (index < 0) index = 0;
        }
        return playList.get(index);
    }

    /**
     * 播放上一首
     */
    public Audio getLastOne() {
        List<Audio> playList = getPlayList();
        Audio currAudio = getPlayingMusic();
        if (ListUtils.isEmpty(playList) || currAudio == null) return null;
        int index = 0;
        int size = playList.size();
        for (int i = 0; i < size; i++) {
            Audio item = playList.get(i);
            if (item == null) continue;
            if (item.id == currAudio.id) {
                index = i;
                break;
            }
        }
        // 0. 循环播放
        int mode = getPlayMode();
        if (mode == Mode.MODE_CYCLE_ALL_PLAY) {
            if (index <= 0) {
                index = playList.size() - 1;
            } else {
                index -= 1;
            }
        }
        // 2. 随机播放
        else if (mode == Mode.MODE_RANDOM_PLAY) {
            Random random = new Random();
            index = random.nextInt(size) - 1;
        }
        return playList.get(index);
    }
}
