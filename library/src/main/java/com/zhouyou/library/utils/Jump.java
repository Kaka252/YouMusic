package com.zhouyou.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 作者：ZhouYou
 * 日期：2017/2/16.
 *
 * (1)跳转
 * new Jump.Launcher(this).to(SearchActivity.class).setup().launch();
 * (2)销毁
 * new Jump.Destroyer(this).setup().finish();
 */
public class Jump {

    private Context context;
    private Class<?> clazz;
    private Intent data;
    private int animType;
    private int reqCode = -1;
    private boolean isFinish;

    public Jump(Context context, Class<?> clazz, Intent data, int animType, int reqCode, boolean isFinish) {
        this.context = context;
        this.clazz = clazz;
        this.data = data;
        this.animType = animType;
        this.reqCode = reqCode;
        this.isFinish = isFinish;
    }

    public Jump(Context context, int animType) {
        this(context, null, null, animType, -1, false);
    }

    public static class Launcher {

        private Context context;
        private Class<?> clazz;
        private Intent data;
        private int animType;
        private int reqCode = -1;
        private boolean isFinish;

        public Launcher(Context context) {
            this.context = context;
        }

        public Launcher to(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Launcher bind(Bundle data) {
            Intent intent = new Intent();
            intent.putExtras(data);
            return bind(intent);
        }

        public Launcher bind(Intent data) {
            this.data = data;
            return this;
        }

        public Launcher setAnim(int animType) {
            this.animType = animType;
            return this;
        }

        public Launcher setReqCode(int reqCode) {
            this.reqCode = reqCode;
            return this;
        }

        public Launcher setFinish(boolean isFinish) {
            this.isFinish = isFinish;
            return this;
        }

        public Jump setup() {
            return new Jump(context, clazz, data, animType, reqCode, isFinish);
        }
    }

    public void launch() {
        if (context == null) {
            throw new NullPointerException("Context has not been initialized yet.");
        }
        if (clazz == null) {
            throw new NullPointerException("Class has not been setup yet.");
        }
        Intent intent = new Intent(context, clazz);
        if (data != null) {
            intent.putExtras(data);
        }
        if (reqCode >= 0 && context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, reqCode);
        } else {
            context.startActivity(intent);
        }
        if (isFinish && context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    public static class Destroyer {
        private Context context;
        private int animType;

        public Destroyer(Context context) {
            this.context = context;
        }

        public Destroyer setAnim(int animType) {
            this.animType = animType;
            return this;
        }

        public Jump setup() {
            return new Jump(context, animType);
        }
    }

    public void finish() {
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
