package com.zhouyou.music.media;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.library.utils.ListUtils;
import com.zhouyou.library.utils.T;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.entity.AudioLocalDataManager;
import com.zhouyou.music.notification.NotificationReceiver;
import com.zhouyou.remote.Mode;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicServiceSDK;
import com.zhouyou.remote.constants.MusicConstants;

import java.util.ArrayList;
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
    public List<Audio> getPlayList() {
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

    /**
     * 获取当前正在播放的音乐路径
     *
     * @return
     */
    private String getCurrentPlayingMusicPath() {
        return MusicServiceSDK.get().getMusicPath();
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
     * 获取音乐播放模式
     *
     * @return
     */
    public int getPlayMode() {
        return MusicServiceSDK.get().getMode();
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
     * 播放列表是否已经被初始化
     *
     * @return
     */
    public boolean hasPlayListInitiated() {
        return MusicServiceSDK.get().hasInitializedPlayList();
    }

    /**
     * 生成播放列表，并开始播放指定音乐
     *
     * @param data          播放列表
     * @param selectedMusic 播放歌曲
     * @param playAction    0 - 播放当前 | 1 - 播放下一首 | 2 - 播放上一首
     * @param seekPosition  被指定的进度
     */
    public void playMusic(List<Audio> data, String selectedMusic, int playAction, int seekPosition) {
        if (ListUtils.isEmpty(data)) {
            T.ss("没有可用的音乐播放列表");
            return;
        }
        ArrayList<String> playList = new ArrayList<>();
        for (Audio audio : data) {
            if (audio == null || TextUtils.isEmpty(audio.path)) continue;
            playList.add(audio.path);
        }
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putStringArrayList(MusicConstants.MUSIC_PLAY_LIST, playList);
        b.putString(MusicConstants.MUSIC_SELECTED, selectedMusic);
        b.putInt(MusicConstants.MUSIC_PLAY_ACTION, playAction);
        b.putInt(MusicConstants.MUSIC_PLAYING_POSITION, seekPosition);
        intent.putExtras(b);
        MusicServiceSDK.get().playMusicList(intent);
    }

    /**
     * 生成播放列表，并开始播放指定音乐
     *
     * @param data          播放列表
     * @param selectedMusic 播放歌曲
     * @param playAction    0 - 播放当前 | 1 - 播放下一首 | 2 - 播放上一首
     */
    public void playMusic(List<Audio> data, String selectedMusic, int playAction) {
        playMusic(data, selectedMusic, playAction, -1);
    }

    /**
     * 生成播放列表，并开始播放指定音乐
     *
     * @param data          播放列表
     * @param selectedMusic 播放歌曲
     */
    public void playMusic(List<Audio> data, String selectedMusic) {
        playMusic(data, selectedMusic, 0, -1);
    }

    public void setPlayMode(int mode) {
        MusicServiceSDK.get().setMode(mode);
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
     * 完成播放
     *
     * @param isPlayBack 是否播放上一首
     */
    public void complete(boolean isPlayBack) {
        MusicServiceSDK.get().complete(isPlayBack);
    }

    /**
     * 发送音乐通知
     */
    public void sendMusicNotification() {
        if (getCurrentPlayingMusicState() == State.PREPARED || getCurrentPlayingMusicState() == State.PAUSED) {
            NotificationReceiver.get().sendNotification();
        }
    }

    /**
     * 播放下一首
     */
//    public Audio getNextOne() {
//        int index = 0;
//        List<Audio> playList = getPlayList();
//        if (ListUtils.isEmpty(playList)) return null;
//        int size = playList.size();
//        int mode = getPlayMode();
//
//        String path = getCurrentPlayingMusicPath();
//        // 0. 循环播放
//        if (mode == Mode.MODE_CYCLE_ALL_PLAY) {
//            for (int i = 0; i < size; i++) {
//
//            }
//            index = playList.indexOf(path);
//            if (index >= size - 1) {
//                index = 0;
//            } else {
//                index += 1;
//            }
//        }
//        // 1. 单曲循环
//        else if (mode == Mode.MODE_SINGLE_PLAY) {
//            index = playList.indexOf(path);
//            if (getCurrentPlayingMusicPosition() != getCurrentPlayingMusicDuration()) {
//                index = playList.indexOf(path);
//                if (index >= size - 1) {
//                    index = 0;
//                } else {
//                    index += 1;
//                }
//            }
//        }
//        // 2. 随机播放
//        else if (mode == Mode.MODE_RANDOM_PLAY) {
//            Random random = new Random();
//            index = random.nextInt(size) - 1;
//        }
//        path = playList.get(index);

//    }

    /**
     * 播放上一首
     */
//    public Audio getLastOne() {
//        int index = 0;
//        int size = playList.size();
//        int mode = getPlayMode();
//        String path = getCurrentPlayingMusicPath();
//        // 0. 循环播放
//        if (mode == Mode.MODE_CYCLE_ALL_PLAY) {
//            index = playList.indexOf(path);
//            if (index <= 0) {
//                index = playList.size() - 1;
//            } else {
//                index -= 1;
//            }
//        }
//        // 2. 随机播放
//        else if (mode == Mode.MODE_RANDOM_PLAY) {
//            Random random = new Random();
//            index = random.nextInt(size) - 1;
//        }
//
//        path = playList.get(index);
//    }
}
