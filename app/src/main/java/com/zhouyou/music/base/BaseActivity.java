package com.zhouyou.music.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.zhouyou.music.entity.Audio;
import com.zhouyou.music.media.MusicPlaySDK;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected MusicPlaySDK sdk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sdk = MusicPlaySDK.get();
        super.onCreate(savedInstanceState);
        registerHomeKeyEventReceiver(this);
    }

    /**
     * 注册用户按HOME键的广播接收器
     *
     * @param context
     */
    private void registerHomeKeyEventReceiver(Context context) {
        context.registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    /**
     * 解除注册用户按HOME键的广播接收器
     *
     * @param context
     */
    private void unregisterHomeKeyEventReceiver(Context context) {
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
    }
}
