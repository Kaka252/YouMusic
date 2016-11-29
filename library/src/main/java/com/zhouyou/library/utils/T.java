package com.zhouyou.library.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhouyou on 2014/12/19.
 */
public class T {

    private static Toast toast;

    /**
     * 禁止实例化
     */
    private T() {
    }

    /**
     * 显示短Toast
     *
     * @param text
     */
    public static void ss(int text) {
        s(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短Toast
     *
     * @param text
     */
    public static void ss(CharSequence text) {
        s(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短Toast
     *
     * @param context
     * @param text
     */
    public static void ss(Context context, int text) {
        s(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短Toast
     *
     * @param context
     * @param text
     */
    public static void ss(Context context, CharSequence text) {
        s(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示长Toast
     *
     * @param text
     */
    public static void sl(int text) {
        s(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长Toast
     *
     * @param text
     */
    public static void sl(CharSequence text) {
        s(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长Toast
     *
     * @param context
     * @param text
     */
    public static void sl(Context context, int text) {
        s(context, text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长Toast
     *
     * @param context
     * @param text
     */
    public static void sl(Context context, CharSequence text) {
        s(context, text, Toast.LENGTH_LONG);
    }

    /**
     * 显示自定义时长Toast
     *
     * @param text
     * @param duration
     */
    public static void s(int text, int duration) {
        s(getContext(), text, duration);
    }

    /**
     * 显示自定义时长Toast
     *
     * @param text
     * @param duration
     */
    public static void s(CharSequence text, int duration) {
        s(getContext(), text, duration);
    }

    /**
     * 显示自定义时长Toast
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void s(Context context, int text, int duration) {
        s(context, getText(text), duration);
    }

    /**
     * 显示自定义时长Toast
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void s(Context context, CharSequence text, int duration) {
        final Message message = handler.obtainMessage();
        message.obj = text;
        message.arg1 = duration;
        message.sendToTarget();
    }

    private static Context getContext() {
        return Lib.getApplication().getApplicationContext();
    }

    private static Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String text = (String) msg.obj;
            int duration = msg.arg1;
            if (!TextUtils.isEmpty(text)) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = makeToast(getContext(), text, duration);
                toast.show();
            }
            return true;
        }
    });

    private static String getText(int text) {
        return getContext().getResources().getString(text);
    }

    public static Toast makeToast(Context context, CharSequence text,
                                  int duration) {
        View mToastView = getToastView(context, text);
        Toast result = new Toast(context);
        result.setView(mToastView);
        result.setDuration(duration);
        return result;
    }

    /**
     * Toast View 的 LayoutParams
     */
    private static final ViewGroup.LayoutParams M_LAYOUT_PARAMS = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    /**
     * Toast View 的 TextColor
     */
    private static final int TEXT_COLOR = Color.rgb(250, 250, 250);

    /**
     * Toast View 的 TextSize;
     */
    private static final int TEXT_SIZE = 16;

    private static View getToastView(Context context, CharSequence text) {
        int padding = Scale.dp2px(context, 10);
        TextView mTextView = new TextView(context);
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        M_LAYOUT_PARAMS.width = display.getWidth() - (padding * 2);
        mTextView.setLayoutParams(M_LAYOUT_PARAMS);
        mTextView.setPadding(padding, padding, padding, padding);
        mTextView.setBackgroundDrawable(getViewGradientDrawable(padding / 2));
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE);
        mTextView.setTextColor(TEXT_COLOR);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setText(text);
        return mTextView;
    }

    private static GradientDrawable mGradientDrawable;

    private static GradientDrawable getViewGradientDrawable(int radius) {
        if (mGradientDrawable == null) {
            mGradientDrawable = new GradientDrawable();
            mGradientDrawable.setCornerRadius(radius);
            mGradientDrawable.setColor(Color.parseColor("#fa716a"));
        }
        return mGradientDrawable;
    }
}
