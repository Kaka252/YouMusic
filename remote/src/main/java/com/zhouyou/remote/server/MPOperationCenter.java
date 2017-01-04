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
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.Mode;
import com.zhouyou.remote.State;
import com.zhouyou.remote.MusicConfig;
import com.zhouyou.remote.client.MusicStateMessageFactory;
import com.zhouyou.remote.constants.MusicConstants;

import java.util.ArrayList;
import java.util.Random;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
class MPOperationCenter extends IMusicControlInterface.Stub implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnSeekCompleteListener {

    private static final String TAG = MPOperationCenter.class.getSimpleName();

    private static final MediaPlayer PLAYER = new MediaPlayer();
    private Context context;
    /*返回主进程的接收回调*/
    private IMusicReceiver receiver;
    /*当前的播放状态*/
    private int currState = 0;
    /*当前播放模式*/
    private int mode;
    /*当前的播放音乐的url*/
    private String currPlayingMusicPath;
    /*当前播放音乐的进度*/
    private int currentPosition = 0;
    /*当前播放音乐的时长*/
    private int duration;
    /*偏好*/
    private SharedPreferences sp;

    MPOperationCenter(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @Override
    public void init() throws RemoteException {
        PLAYER.setOnErrorListener(this);
        PLAYER.setOnPreparedListener(this);
        PLAYER.setOnCompletionListener(this);
        PLAYER.setOnSeekCompleteListener(this);
        doMediaPlayerAction(makeStateChange(State.IDLE));
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
            doMediaPlayerAction(makeStateChange(State.ERROR));
            return;
        }
        Bundle b = data.getExtras();
        String selectMusic = b.getString(MusicConstants.MUSIC_SELECTED);
        int seekPosition = b.getInt(MusicConstants.MUSIC_PLAYING_POSITION);
        if (TextUtils.isEmpty(selectMusic)) {
            doMediaPlayerAction(makeStateChange(State.ERROR));
            return;
        }
        this.currPlayingMusicPath = selectMusic;
        if (seekPosition > 0) {
            currentPosition = seekPosition;
            play(false);
        } else {
            play();
        }
    }

    private Intent makeStateChange(int state, int seekPosition) {
        return MusicStateMessageFactory.createMusicStateMessage(state, seekPosition);
    }

    private Intent makeStateChange(int state) {
        return makeStateChange(state, -1);
    }

    /**
     * 播放音乐，默认清除
     *
     * @throws RemoteException
     */
    private void play() throws RemoteException {
        play(true);
    }

    /**
     * 播放音乐
     *
     * @param isClearProgress 跳转到播放进度
     * @throws RemoteException
     */
    private void play(boolean isClearProgress) throws RemoteException {
        try {
            if (isReset()) {
                PLAYER.reset();
                doMediaPlayerAction(makeStateChange(State.IDLE));
            }
            if (currState == State.IDLE) {
                Uri uri = Uri.parse(currPlayingMusicPath);
                PLAYER.setDataSource(context, uri);
            }
            doMediaPlayerAction(makeStateChange(State.INITIALIZED));
            if (currState != State.ERROR) {
                PLAYER.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            if (currState == State.INITIALIZED || currState == State.STOPPED) {
                if (isClearProgress) {
                    handler.sendEmptyMessage(ACTION_INIT_PLAY);
                }
                doMediaPlayerAction(makeStateChange(State.PREPARING));
            }
        } catch (Exception e) {
            Log.e("MusicError", e.toString());
            doMediaPlayerAction(makeStateChange(State.ERROR));
        }
    }

    /**
     * 设置模式
     *
     * @param mode
     */
    @Override
    public void setMode(int mode) throws RemoteException {
        this.mode = mode;
        onMainProcessStateChangeNotify(0);
    }

    /**
     * 切换播放状态
     *
     * @param action 播放状态
     * @throws RemoteException
     */
    @Override
    public void doMediaPlayerAction(Intent action) throws RemoteException {
        currState = action.getIntExtra(MusicConstants.MUSIC_STATE, 0);
        int seekPosition = action.getIntExtra(MusicConstants.MUSIC_PLAYING_POSITION, -1);

        onMainProcessStateChangeNotify(0);
        printLog(currState); // 打印日志
        switch (currState) {
            case State.IDLE: // 闲置
                break;
            case State.INITIALIZED: // 初始化
                break;
            case State.PREPARING: // 正在准备
                PLAYER.prepareAsync();
                saveLastPlayedMusic();
                break;
            case State.PREPARED: // 准备就绪
                if (seekPosition > 0) {
                    PLAYER.seekTo(seekPosition);
                }
                PLAYER.start();
                doMediaPlayerAction(makeStateChange(State.IN_PROGRESS));
                break;
            case State.IN_PROGRESS: // 播放中
                // TODO 发送通知
                handler.sendEmptyMessage(ACTION_PROGRESS_UPDATE);
                break;
            case State.PAUSED: // 暂停
                PLAYER.pause();
                handler.sendEmptyMessage(ACTION_PROGRESS_SUSPEND);
                break;
            case State.COMPLETED: // 播放完成
//                handler.sendEmptyMessageDelayed(isPlayBack ? ACTION_PLAY_BACK : ACTION_PLAY_NEXT, 16);
                break;
            case State.STOPPED: // 播放终断
                PLAYER.stop();
                break;
            case State.END: // 结束
                PLAYER.stop();
                break;
            case State.ERROR: // 错误
                PLAYER.reset();
//                handler.sendEmptyMessageDelayed(ACTION_PLAY_NEXT, 2000);
                break;
            default:
                break;
        }
    }

    private static final int ACTION_INIT_PLAY = 0;
    private static final int ACTION_PROGRESS_UPDATE = 1; // 更新播放时间
    private static final int ACTION_PROGRESS_SUSPEND = 2; // 暂停

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_INIT_PLAY:
                    currentPosition = 0;
                    break;
                case ACTION_PROGRESS_UPDATE:
                    try {
                        onMainProcessStateChangeNotify(1);
                        handler.sendEmptyMessageDelayed(ACTION_PROGRESS_UPDATE, 1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case ACTION_PROGRESS_SUSPEND:
                    try {
                        onMainProcessStateChangeNotify(0);
                        handler.removeMessages(ACTION_PROGRESS_UPDATE);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    /**
     * 获取当前音乐播放进度
     *
     * @return
     */
    private int getPlayingPosition() {
        if (PLAYER.isPlaying()) {
            currentPosition = PLAYER.getCurrentPosition();
        }
        return currentPosition;
    }

    private int getPlayingDuration() {
        if (PLAYER.isPlaying()) {
            duration = PLAYER.getDuration();
        }
        return duration;
    }

    /**
     * 获取到音乐播放器的状态和当前播放音乐的id后返回主进程操作
     */
    private void onMainProcessStateChangeNotify(int dataType) throws RemoteException {
        if (TextUtils.isEmpty(currPlayingMusicPath)) {
            currPlayingMusicPath = getLastPlayedMusic();
        }
        Log.d(TAG, "音乐的路径 : " + currPlayingMusicPath);
        MusicConfig config = new MusicConfig();
        config.setDataType(dataType);
        Bundle b = new Bundle();
        b.putInt(MusicConstants.MUSIC_STATE, currState);
        b.putString(MusicConstants.MUSIC_SELECTED, currPlayingMusicPath);
        b.putInt(MusicConstants.MUSIC_PLAYING_POSITION, getPlayingPosition());
        b.putInt(MusicConstants.MUSIC_PLAYING_DURATION, getPlayingDuration());
        b.putInt(MusicConstants.MUSIC_MODE, mode);
        config.setExtra(b);
        receiver.onReceive(config);
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
            doMediaPlayerAction(makeStateChange(State.COMPLETED));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.d(TAG, "onSeekComplete");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        try {
            doMediaPlayerAction(makeStateChange(State.ERROR));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            doMediaPlayerAction(makeStateChange(State.PREPARED, currentPosition));
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
