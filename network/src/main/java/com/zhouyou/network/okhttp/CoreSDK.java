package com.zhouyou.network.okhttp;

import okhttp3.OkHttpClient;

/**
 * 作者：ZhouYou
 * 日期：2017/2/21.
 */
public class CoreSDK {

    private static volatile OkHttpClient client;

    private CoreSDK() {

    }

    public static void get() {
        if (client == null) {
            synchronized (CoreSDK.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
    }

    public void initialize() {

    }
}
