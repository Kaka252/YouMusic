package com.zhouyou.remote.server;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;

import java.util.ArrayList;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class MPOperationCenter extends IMusicControlInterface.Stub implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = MPOperationCenter.class.getSimpleName();

    private static final MediaPlayer PLAYER = new MediaPlayer();
    private Context context;
    /*返回主进程的接收回调*/
    private IMusicReceiver receiver;
    /*当前的播放状态*/
    private int currState = 0;
    /*当前的播放音乐的url*/
    private String currPlayingMusicPath;
    /*当前的播放列表*/
    private ArrayList<String> playList = new ArrayList<>();
    /*偏好*/
    private SharedPreferences sp;

    public MPOperationCenter(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @Override
    public void init() throws RemoteException {
        PLAYER.setOnErrorListener(this);
        PLAYER.setOnPreparedListener(this);
        PLAYER.setOnCompletionListener(this);
        switchMediaState(State.IDLE);
    }

    /**
     * 获取播放列表，且开始播放指定歌曲
     *
     * @param data 数据
     * @throws RemoteException
     */
    @Override
    public void playMusicList(Intent data) throws RemoteException {
        if (data == null || data.getExtras() == null) {
            switchMediaState(State.ERROR);
            return;
        }
        Bundle b = data.getExtras();
        ArrayList<String> playList = b.getStringArrayList("playList");
        String selectMusic = b.getString("selectedMusic");
        if (playList == null || playList.size() <= 0) {
            switchMediaState(State.ERROR);
            return;
        }
        if (TextUtils.isEmpty(selectMusic)) {
            switchMediaState(State.ERROR);
            return;
        }

        this.playList = playList;
        this.currPlayingMusicPath = selectMusic;
        play(currPlayingMusicPath);
    }

    /**
     * 播放音乐
     *
     * @param path 音乐播放路径
     * @throws RemoteException
     */
    private void play(String path) throws RemoteException {
        if (TextUtils.isEmpty(path)) {
            switchMediaState(State.ERROR);
            return;
        }
        try {
            if (isReset()) {
                PLAYER.reset();
                switchMediaState(State.IDLE);
            }
            if (currState == State.IDLE) {
                Uri uri = Uri.parse(path);
                PLAYER.setDataSource(context, uri);
            }
            switchMediaState(State.INITIALIZED);
            if (currState != State.ERROR) {
                PLAYER.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            if (currState == State.INITIALIZED || currState == State.STOPPED) {
                switchMediaState(State.PREPARING);
            }
        } catch (Exception e) {
            Log.e("MusicError", e.toString());
            switchMediaState(State.ERROR);
        }
    }

    /**
     * 切换播放状态
     *
     * @param state 播放状态
     * @throws RemoteException
     */
    @Override
    public void switchMediaState(int state) throws RemoteException {
        currState = state;
        onMainProcessCallback();
        printLog(state); // 打印日志
        switch (state) {
            case State.IDLE: // 闲置
                break;
            case State.INITIALIZED: // 初始化
                break;
            case State.PREPARING: // 正在准备
                PLAYER.prepareAsync();
                break;
            case State.PREPARED: // 准备就绪
//                if (currentPosition > 0 && currentPosition <= getCurrentAudioDuration()) {
//                    mediaPlayer.seekTo(currentPosition);
//                }
                PLAYER.start();
                saveLastPlayedMusic();
                switchMediaState(State.IN_PROGRESS);
                break;
            case State.IN_PROGRESS: // 播放中
                break;
            case State.PAUSED: // 暂停
                PLAYER.pause();
            case State.COMPLETED: // 播放完成
                handler.sendEmptyMessageDelayed(ACTION_PLAY_NEXT, 100);
//                handler.sendEmptyMessageDelayed(isPlayBack ? ACTION_PLAY_BACK : ACTION_PLAY_NEXT, 100);
                break;
            case State.STOPPED: // 播放终断
                PLAYER.stop();
                break;
            case State.END: // 结束
                PLAYER.stop();
                break;
            case State.ERROR: // 错误
                PLAYER.reset();
                break;
            default:
                break;
        }
    }

    private static final int ACTION_PLAY_NEXT = 1; // 播放下一首

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_PLAY_NEXT:
                    playNext();
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    /**
     * 播放下一首
     */
    private void playNext() {
        int index = playList.indexOf(currPlayingMusicPath);
        if (index >= playList.size()) {
            index = 0;
        } else {
            index += 1;
        }
        currPlayingMusicPath = playList.get(index);
        try {
            play(currPlayingMusicPath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放上一首
     */
    private void playBack() {
        int index = playList.indexOf(currPlayingMusicPath);
        if (index < 0) {
            index = playList.size() - 1;
        } else {
            index -= 1;
        }
        currPlayingMusicPath = playList.get(index);
        try {
            play(currPlayingMusicPath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取到音乐播放器的状态和当前播放音乐的id后返回主进程操作
     */
    private void onMainProcessCallback() throws RemoteException {
        if (TextUtils.isEmpty(currPlayingMusicPath)) {
            currPlayingMusicPath = getLastPlayedMusic();
        }
        Log.d(TAG, "音乐的路径 : " + currPlayingMusicPath);
        receiver.onReceive(currPlayingMusicPath, currState);
    }

    /**
     * 注册接收回调
     *
     * @param receiver
     */
    @Override
    public void registerReceiver(IMusicReceiver receiver) {
        this.receiver = receiver;
    }

    /**
     * 重置状态
     *
     * @return
     */
    private boolean isReset() {
        return currState == State.IDLE || currState == State.INITIALIZED || currState == State.PREPARED ||
                currState == State.IN_PROGRESS || currState == State.PAUSED || currState == State.STOPPED ||
                currState == State.COMPLETED || currState == State.ERROR;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            switchMediaState(State.COMPLETED);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        try {
            switchMediaState(State.ERROR);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            switchMediaState(State.PREPARED);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存上一次播放的音乐id
     */
    private void saveLastPlayedMusic() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("musicPath", currPlayingMusicPath);
        editor.apply();
    }

    /**
     * 获取上一次播放的音乐id
     *
     * @return
     */
    private String getLastPlayedMusic() {
        return sp.getString("musicPath", "");
    }

    /**
     * 打印日志
     */
    private void printLog(int state) {
        switch (state) {
            case State.IDLE:
                Log.d(TAG, "AudioState: " + state + " - 闲置");
                break;
            case State.INITIALIZED: // 初始化
                Log.d(TAG, "AudioState: " + state + " - 初始化");
                break;
            case State.PREPARING: // 正在准备
                Log.d(TAG, "AudioState: " + state + " - 正在准备");
                break;
            case State.PREPARED: // 准备就绪
                Log.d(TAG, "AudioState: " + state + " - 准备就绪");
                break;
            case State.IN_PROGRESS: // 播放中
                Log.d(TAG, "AudioState: " + state + " - 正在播放");
                break;
            case State.PAUSED: // 暂停
                Log.d(TAG, "AudioState: " + state + " - 暂停");
                break;
            case State.COMPLETED: // 播放完成
                Log.d(TAG, "AudioState: " + state + " - 播放完成");
                break;
            case State.STOPPED: // 播放终断
                Log.d(TAG, "AudioState: " + state + " - 播放终断");
                break;
            case State.END: // 结束
                Log.d(TAG, "AudioState: " + state + " - 结束");
                break;
            case State.ERROR: // 错误
                Log.d(TAG, "AudioState: " + state + " - 错误");
                break;
            default:
                break;
        }
    }
}
