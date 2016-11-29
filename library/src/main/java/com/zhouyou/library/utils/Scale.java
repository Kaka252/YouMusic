package com.zhouyou.library.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by monch on 16/7/7.
 */
public class Scale {

    private static int mDisplayWidth, mDisplayHeight;

    public static int dp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                value, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getDisplayWidth(Context context) {
        if (mDisplayWidth == 0) {
            Point point = new Point();
            WindowManager windowManager = (WindowManager)
                    context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getSize(point);
            mDisplayWidth = point.x;
        }
        return mDisplayWidth;
    }

    public static int getDisplayHeight(Context context) {
        if (mDisplayHeight == 0) {
            Point point = new Point();
            WindowManager windowManager = (WindowManager)
                    context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getSize(point);
            mDisplayHeight = point.y;
        }
        return mDisplayHeight;
    }

}
