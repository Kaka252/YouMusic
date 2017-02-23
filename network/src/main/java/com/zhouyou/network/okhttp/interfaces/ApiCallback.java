package com.zhouyou.network.okhttp.interfaces;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public interface ApiCallback {

    void onStart();

    void threadMode();

    void onError();

    void onComplete();
}
