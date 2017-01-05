package com.zhouyou.remote.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.MusicConfig;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.observer.MusicManager;
import com.zhouyou.remote.constants.MusicConstants;

/**
 * 作者：ZhouYou
 * 日期：2016/12/14.
 * 信号接受者
 */
public class Receiver extends IMusicReceiver.Stub {

    private static final String TAG = "Receiver";

    /*当前播放状态*/
    private int currState;
    /*当前播放音乐的路径*/
    private String currMusicPath;
    /*当前音乐的进度*/
    private int currPlayingPosition;
    /*当前音乐的时长*/
    private int currPlayingDuration;

    int getCurrState() {
        return currState;
    }

    String getCurrMusicPath() {
        return currMusicPath;
    }

    int getCurrPlayingPosition() {
        return currPlayingPosition;
    }

    int getCurrPlayingDuration() {
        return currPlayingDuration;
    }

    @Override
    public void onReceive(MusicConfig config) throws RemoteException {
        if (config == null) return;
        Bundle data = config.getExtra();
        currMusicPath = data.getString(MusicConstants.MUSIC_SELECTED);
        currState = data.getInt(MusicConstants.MUSIC_STATE);
        currPlayingPosition = data.getInt(MusicConstants.MUSIC_PLAYING_POSITION);
        currPlayingDuration = data.getInt(MusicConstants.MUSIC_PLAYING_DURATION);
        int dataType = config.getDataType();
        if (dataType == 1 && currState == State.IN_PROGRESS) {
            mMainHandler.sendMessage(getMessage(UPDATE_PROGRESS));
        } else {
            mMainHandler.sendMessage(getMessage(UPDATE_STATE));
        }
    }

    /**
     * 发送消息
     * @param signal
     * @return
     */
    private Message getMessage(int signal) {
        Message msg = Message.obtain();
        msg.what = signal;
        Bundle b = new Bundle();
        if (signal == UPDATE_PROGRESS) {
            b.putInt(MusicConstants.MUSIC_PLAYING_POSITION, currPlayingPosition);
            b.putInt(MusicConstants.MUSIC_PLAYING_DURATION, currPlayingDuration);
            msg.setData(b);
        } else if (signal == UPDATE_STATE) {
            b.putString(MusicConstants.MUSIC_SELECTED, currMusicPath);
            b.putInt(MusicConstants.MUSIC_STATE, currState);
            msg.setData(b);
        }
        return msg;
    }

    private static final int UPDATE_STATE = 0;
    private static final int UPDATE_PROGRESS = 1;

    private Handler mMainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle b = msg.getData();
            if (b == null) return false;
            switch (msg.what) {
                case UPDATE_STATE:
                    int state = b.getInt(MusicConstants.MUSIC_STATE);
                    String path = b.getString(MusicConstants.MUSIC_SELECTED);
                    MusicManager.get().createAudioStatePublisher().notifySubscribers(state, path);
                    break;
                case UPDATE_PROGRESS:
                    int position = b.getInt(MusicConstants.MUSIC_PLAYING_POSITION);
                    int duration = b.getInt(MusicConstants.MUSIC_PLAYING_DURATION);
                    MusicManager.get().createProgressPublisher().notifySubscribers(position, duration);
                    break;
                default:
                    break;
            }
            return true;
        }
    });
}
