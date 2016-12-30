package com.zhouyou.music.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zhouyou.library.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/11/23.
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 透明状态栏
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        registerHomeKeyEventReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private static final int PERMISSION_REQUEST_CODE = 0x1;

    protected boolean isApplyingPermissions() {
        String[] permissions = getPermissions();
        if (permissions != null && permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
            return true;
        }
        return false;
    }

    private String[] getPermissions() {
        List<String> list = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 读取扩展卡数据权限
            if (!PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                Log.d("===Permission add===", "READ_EXTERNAL_STORAGE");
            }
        }
        if (!list.isEmpty()) {
            return list.toArray(new String[list.size()]);
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterHomeKeyEventReceiver(this);
    }
}
