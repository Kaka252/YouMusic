package com.zhouyou.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * 作者：ZhouYou
 * 日期：2017/2/16.
 */
public class Jump {

    private static volatile Jump instance = null;

    private Context context;
    private int animType;
    private int reqCode = -1;
    private boolean isFinish;

    private Intent intent;

    private Jump(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    private static class Builder {

        private Context context;
        private Intent intent;

        Builder(Context context) {
            this.context = context;
            intent = new Intent();
        }

        public Jump build() {
            return new Jump(context, intent);
        }
    }

    public static Jump with(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context == null");
        }
        if (instance == null) {
            synchronized (Jump.class) {
                if (instance == null) {
                    instance = new Builder(context).build();
                }
            }
        }
        return instance;
    }

    public Jump to(Class<?> clazz) {
        intent.setClass(context, clazz);
        return this;
    }

    public Jump data(Bundle data) {
        intent.putExtras(data);
        return this;
    }

    public Jump data(Intent data) {
        intent.putExtras(data);
        return this;
    }

    public Jump anim(int animType) {
        this.animType = animType;
        return this;
    }

    public Jump reqCode(int reqCode) {
        this.reqCode = reqCode;
        return this;
    }

    public Jump finish(boolean isFinish) {
        this.isFinish = isFinish;
        return this;
    }

    public void launch() {
        if (intent == null) {
            throw new NullPointerException("intent == null");
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
}
