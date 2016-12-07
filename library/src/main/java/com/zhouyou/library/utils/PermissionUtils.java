package com.zhouyou.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 作者：ZhouYou
 * 日期：2016/12/7.
 */
public class PermissionUtils {

    /**
     * 没有权限
     */
    public static final int NONE = PackageManager.PERMISSION_DENIED;
    /**
     * 正在申请权限
     */
    public static final int APPLY = -999;
    /**
     * 有权限
     */
    public static final int HOLD = PackageManager.PERMISSION_GRANTED;

    /**
     * 检测当前是否有参数中的权限并在允许申请时进行申请
     *
     * @param activity Activity上下文
     * @param requestCode 请求code
     * @param permission 权限名称
     * @return NONE\APPLY\HOLD
     */
    public static int checkPermissionAndApply(Activity activity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // 未申请过此权限
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission}, requestCode);
                return APPLY;
            }
            return NONE;
        }
        return HOLD;
    }

    /**
     * 检测当前是否有参数中的权限
     * @param context 上下文
     * @param permission 权限名称
     * @return true 有此权限 false 无此权限
     */
    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }
}
