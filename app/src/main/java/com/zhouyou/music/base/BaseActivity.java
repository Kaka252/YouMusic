package com.zhouyou.music.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.zhouyou.music.config.Constants;
import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.MusicPlaySDK;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected MusicPlaySDK sdk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdk = MusicPlaySDK.get();
        registerHomeKeyEventReceiver(this);
        initReceiver();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.RECEIVER_AUDIO_STATE_CHANGE);
        registerReceiver(receiver, filter);
    }

    /**
     * 接收状态改变的广播
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!TextUtils.equals(intent.getAction(), Constants.RECEIVER_AUDIO_STATE_CHANGE))
                return;
            int state = intent.getIntExtra(Constants.DATA_INT, 0);
            Audio audio = intent.getParcelableExtra(Constants.DATA_ENTITY);
            onAudioStateChanged(audio, state);
        }
    };

    /**
     * 监听音频播放的状态改变
     *
     * @param audio 音频
     * @param state 状态
     */
    protected abstract void onAudioStateChanged(Audio audio, int state);

    private void registerHomeKeyEventReceiver(Context context) {
        // 注册用户按HOME键的广播接收器
        context.registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void unregisterHomeKeyEventReceiver(Context context) {
        // 解除注册用户按HOME键的广播接收器
        context.unregisterReceiver(mHomeKeyEventReceiver);
    }

    /**
     * 检测app是否处于后台运行的标识
     */
    private static boolean isRunningInBackground = false;

    /**
     * 该广播用于接收用户按下HOME键的监听
     */
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isRunningInBackground) return;
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra("reason");
                if (TextUtils.equals(reason, "homekey")) {
                    isRunningInBackground = true;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterHomeKeyEventReceiver(this);
        if (receiver != null) unregisterReceiver(receiver);
    }
}
