package com.zhouyou.music.media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.zhouyou.music.base.App;
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

    private static final String[] AUDIO_KEYS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TITLE_KEY,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST_KEY,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM_KEY,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.IS_RINGTONE,
            MediaStore.Audio.Media.IS_PODCAST,
            MediaStore.Audio.Media.IS_ALARM,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.IS_NOTIFICATION,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATA
    };

    private Context context;
    private ContentResolver resolver;
    private int currState;
    private Audio currAudio;
    private MediaPlayer mediaPlayer;
    private OnAudioPlayCallback callback;

    public void setOnAudioPlayCallback(OnAudioPlayCallback callback) {
        this.callback = callback;
    }

    private static class SDKHolder {
        private static final MusicPlaySDK SDK = new MusicPlaySDK();
    }

    public static MusicPlaySDK get() {
        return SDKHolder.SDK;
    }

    private MusicPlaySDK() {
        context = App.get().getApplicationContext();
        resolver = context.getContentResolver();
    }

    /**
     * 初始化
     */
    private void init() {
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
        if (callback != null) callback.onStateChanged(currAudio, currState);
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
    private void startPlay() {
        mediaPlayer.start();
        changeState(AudioPlayState.STARTED);
    }

    /**
     * 获取音频列表
     *
     * @return
     */
    public List<Audio> getAudioList() {
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_KEYS, null, null, null);
        if (cursor == null) return null;
        List<Audio> audioList = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            for (final String key : AUDIO_KEYS) {
                final int columnIndex = cursor.getColumnIndex(key);
                final int type = cursor.getType(columnIndex);
                switch (type) {
                    case Cursor.FIELD_TYPE_BLOB:
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        float floatValue = cursor.getFloat(columnIndex);
                        bundle.putFloat(key, floatValue);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        int intValue = cursor.getInt(columnIndex);
                        bundle.putInt(key, intValue);
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        String strValue = cursor.getString(columnIndex);
                        bundle.putString(key, strValue);
                        break;
                }
            }
            Audio audio = new Audio(bundle);
            audioList.add(audio);
        }
        cursor.close();
        return audioList;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        startPlay();
    }
}
