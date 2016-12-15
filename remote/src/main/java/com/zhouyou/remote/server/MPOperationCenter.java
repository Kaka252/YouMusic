package com.zhouyou.remote.server;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicMsgFactory;

/**
 * 作者：ZhouYou
 * 日期：2016/12/13.
 */
public class MPOperationCenter extends IMusicControlInterface.Stub implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = MPOperationCenter.class.getSimpleName();

    private static final MediaPlayer MDPLAYER = new MediaPlayer();
    private Context context;
    /*返回主进程的接收回调*/
    private IMusicReceiver receiver;
    /*当前的播放状态*/
    private int currState = 0;

    public MPOperationCenter(Context context) {
        this.context = context;
    }

    @Override
    public void init() throws RemoteException {
        MDPLAYER.setOnErrorListener(this);
        MDPLAYER.setOnPreparedListener(this);
        MDPLAYER.setOnCompletionListener(this);
        switchMediaState(State.IDLE);
    }

    /**
     * 播放音乐
     *
     * @param intent 音乐数据
     * @throws RemoteException
     */
    @Override
    public void play(Music intent) throws RemoteException {
        String audioPath = intent.getAudioPath();
        if (TextUtils.isEmpty(audioPath)) {
            switchMediaState(State.ERROR);
            return;
        }
        try {
            if (isReset()) {
                MDPLAYER.reset();
                switchMediaState(State.IDLE);
            }
            if (currState == State.IDLE) {
                Uri uri = Uri.parse(audioPath);
                MDPLAYER.setDataSource(context, uri);
            }
            switchMediaState(State.INITIALIZED);
            if (currState != State.ERROR) {
                MDPLAYER.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            if (currState == State.INITIALIZED || currState == State.STOPPED) {
//                    PrefUtils.put(Constants.DATA_INT, audio.id);
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
        printLog(state); // 打印日志
        switch (state) {
            case State.IDLE: // 闲置
                break;
            case State.INITIALIZED: // 初始化
                break;
            case State.PREPARING: // 正在准备
                MDPLAYER.prepareAsync();
                break;
            case State.PREPARED: // 准备就绪
//                if (currentPosition > 0 && currentPosition <= getCurrentAudioDuration()) {
//                    mediaPlayer.seekTo(currentPosition);
//                }
                MDPLAYER.start();
                switchMediaState(State.IN_PROGRESS);
                break;
            case State.IN_PROGRESS: // 播放中
                break;
            case State.PAUSED: // 暂停
                MDPLAYER.pause();
            case State.COMPLETED: // 播放完成
                break;
            case State.STOPPED: // 播放终断
                MDPLAYER.stop();
                break;
            case State.END: // 结束
                MDPLAYER.stop();
                break;
            case State.ERROR: // 错误
                MDPLAYER.reset();
                break;
            default:
                break;
        }
        receiver.onReceive(state);
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
