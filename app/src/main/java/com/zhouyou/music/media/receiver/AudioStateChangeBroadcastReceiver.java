package com.zhouyou.music.media.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zhouyou.music.config.Constants;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.MusicPlaySDK;

/**
 * 作者：ZhouYou
 * 日期：2016/11/24.
 */
public class AudioStateChangeBroadcastReceiver extends BroadcastReceiver {

    private OnAudioStateChangeListener listener;

    private MusicPlaySDK sdk;

    public AudioStateChangeBroadcastReceiver() {
        sdk = MusicPlaySDK.get();
    }

    public void setOnAudioStateChangeListener(OnAudioStateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), Constants.RECEIVER_AUDIO_STATE_CHANGE)) {
            int state = intent.getIntExtra(Constants.DATA_INT, 0);
            Audio audio = intent.getParcelableExtra(Constants.DATA_ENTITY);
            if (listener != null) listener.onAudioStateChange(audio, state);
        }
    }
}
