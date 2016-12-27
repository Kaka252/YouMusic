package com.zhouyou.remote.client;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.MusicManager;
import com.zhouyou.remote.constants.MusicConstants;

import java.util.ArrayList;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 * 信号接受者
 */
public class Receiver extends IMusicReceiver.Stub {

    /*播放列表是否被初始化*/
    private boolean hasPlayListInitialized;
    /*当前播放状态*/
    private int currState;
    /*当前播放音乐的路径*/
    private String currMusicPath;
    /*当前音乐的进度*/
    private int currPlayingPosition;
    /*当前音乐的时长*/
    private int currPlayingDuration;

    public int getCurrState() {
        return currState;
    }

    public String getCurrMusicPath() {
        return currMusicPath;
    }

    public int getCurrPlayingPosition() {
        return currPlayingPosition;
    }

    public int getCurrPlayingDuration() {
        return currPlayingDuration;
    }

    @Override
    public void onReceive(Intent data) throws RemoteException {
        currMusicPath = data.getStringExtra(MusicConstants.MUSIC_SELECTED);
        currState = data.getIntExtra(MusicConstants.MUSIC_STATE, 0);
        hasPlayListInitialized = data.getBooleanExtra(MusicConstants.MUSIC_PLAY_LIST, false);
        currPlayingPosition = data.getIntExtra(MusicConstants.MUSIC_PLAYING_POSITION, 0);
        currPlayingDuration = data.getIntExtra(MusicConstants.MUSIC_PLAYING_DURATION, 0);
        dispatch();
    }

    /**
     * 播放列表是否被初始化
     *
     * @return true - 初始化了
     */
    boolean hasInitializedPlayList() {
        return hasPlayListInitialized;
    }

    private boolean isPlaying() {
        return currState == State.IN_PROGRESS;
    }

    /**
     * 消息分发
     */
    private void dispatch() {
        if (isPlaying()) {
            mMainHandler.sendEmptyMessage(UPDATE_PROGRESS);
        }
        mMainHandler.sendEmptyMessage(UPDATE_STATE);
    }

    private static final int UPDATE_STATE = 0;
    private static final int UPDATE_PROGRESS = 1;

    private Handler mMainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_STATE:
                    MusicManager.get().createAudioStatePublisher().notifySubscribers();
                    break;
                case UPDATE_PROGRESS:
                    MusicManager.get().createProgressPublisher().notifySubscribers(currPlayingPosition, currPlayingDuration);
                    break;
                default:
                    break;
            }
            return true;
        }
    });
}
