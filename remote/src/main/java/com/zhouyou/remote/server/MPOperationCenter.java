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
import android.widget.Toast;

import com.zhouyou.remote.IMusicControlInterface;
import com.zhouyou.remote.IMusicReceiver;
import com.zhouyou.remote.Music;
import com.zhouyou.remote.State;
import com.zhouyou.remote.client.MusicStateMessageFactory;
import com.zhouyou.remote.constants.MusicConstants;

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
    /*当前播放音乐的进度*/
    private int currentPosition = 0;
    /*当前播放音乐的时长*/
    private int duration;
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
        ArrayList<String> playList = b.getStringArrayList(MusicConstants.MUSIC_PLAY_LIST);
        String selectMusic = b.getString(MusicConstants.MUSIC_SELECTED);
        int playAction = b.getInt(MusicConstants.MUSIC_PLAY_ACTION);
        int seekPosition = b.getInt(MusicConstants.MUSIC_PLAYING_POSITION);
        if (playList == null || playList.size() <= 0) {
            doMediaPlayerAction(makeStateChange(State.ERROR));
            return;
        }
        if (TextUtils.isEmpty(selectMusic)) {
            doMediaPlayerAction(makeStateChange(State.ERROR));
            return;
        }
        this.currPlayingMusicPath = selectMusic;
        this.playList = playList;
        if (seekPosition > 0) {
            currentPosition = seekPosition;
        }
        switch (playAction) {
            case 1:
                playNext();
                break;
            case 2:
                playBack();
                break;
            default:
                play();
                break;
        }
    }

    private Intent makeStateChange(int state) {
        return MusicStateMessageFactory.createMusicStateMessage(state, currentPosition);
    }

    /**
     * 播放音乐
     *
     * @throws RemoteException
     */
    private void play() throws RemoteException {
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
                doMediaPlayerAction(makeStateChange(State.PREPARING));
            }
        } catch (Exception e) {
            Log.e("MusicError", e.toString());
            doMediaPlayerAction(makeStateChange(State.ERROR));
        }
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
        boolean isPlayBack = action.getBooleanExtra(MusicConstants.MUSIC_PLAY_BACK, false);
        int seekPosition = action.getIntExtra(MusicConstants.MUSIC_PLAYING_POSITION, -1);

        onMainProcessStateChangeNotify();
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
                handler.sendEmptyMessage(ACTION_PROGRESS_UPDATE);
                break;
            case State.PAUSED: // 暂停
                PLAYER.pause();
                break;
            case State.COMPLETED: // 播放完成
                handler.sendEmptyMessage(ACTION_INIT_PLAY);
                handler.sendEmptyMessageDelayed(isPlayBack ? ACTION_PLAY_BACK : ACTION_PLAY_NEXT, 16);
                break;
            case State.STOPPED: // 播放终断
                PLAYER.stop();
                break;
            case State.END: // 结束
                PLAYER.stop();
                break;
            case State.ERROR: // 错误
                PLAYER.reset();
                handler.sendEmptyMessageDelayed(ACTION_PLAY_NEXT, 2000);
                break;
            default:
                break;
        }
    }

    private static final int ACTION_INIT_PLAY = 0;
    private static final int ACTION_PLAY_NEXT = 1; // 播放下一首
    private static final int ACTION_PLAY_BACK = 2; // 播放上一首
    private static final int ACTION_PROGRESS_UPDATE = 3; // 更新播放时间
    private static final int ACTION_PROGRESS_SUSPEND = 4; // 暂停

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_INIT_PLAY:
                    currentPosition = 0;
                    break;
                case ACTION_PLAY_NEXT:
                    playNext();
                    break;
                case ACTION_PLAY_BACK:
                    playBack();
                    break;
                case ACTION_PROGRESS_UPDATE:
                    try {
                        onMainProcessStateChangeNotify();
                        handler.sendEmptyMessageDelayed(ACTION_PROGRESS_UPDATE, 1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case ACTION_PROGRESS_SUSPEND:
                    handler.removeMessages(ACTION_PROGRESS_UPDATE);
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
        if (index >= playList.size() - 1) {
            index = 0;
        } else {
            index += 1;
        }
        currPlayingMusicPath = playList.get(index);
        try {
            play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放上一首
     */
    private void playBack() {
        int index = playList.indexOf(currPlayingMusicPath);
        if (index <= 0) {
            index = playList.size() - 1;
        } else {
            index -= 1;
        }
        currPlayingMusicPath = playList.get(index);
        try {
            play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

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
     * 判断播放列表是否被初始化
     *
     * @return
     */
    private boolean hasPlayListInitialized() {
        return playList != null && playList.size() > 0;
    }

    /**
     * 获取到音乐播放器的状态和当前播放音乐的id后返回主进程操作
     */
    private void onMainProcessStateChangeNotify() throws RemoteException {
        if (TextUtils.isEmpty(currPlayingMusicPath)) {
            currPlayingMusicPath = getLastPlayedMusic();
        }
        Log.d(TAG, "音乐的路径 : " + currPlayingMusicPath);
        Intent intent = new Intent();
        intent.putExtra(MusicConstants.MUSIC_STATE, currState);
        intent.putExtra(MusicConstants.MUSIC_PLAY_LIST, hasPlayListInitialized());
        intent.putExtra(MusicConstants.MUSIC_SELECTED, currPlayingMusicPath);
        intent.putExtra(MusicConstants.MUSIC_PLAYING_POSITION, getPlayingPosition());
        intent.putExtra(MusicConstants.MUSIC_PLAYING_DURATION, getPlayingDuration());
        receiver.onReceive(intent);
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
            doMediaPlayerAction(makeStateChange(State.PREPARED));
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
