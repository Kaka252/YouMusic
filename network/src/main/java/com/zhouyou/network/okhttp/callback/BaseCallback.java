package com.zhouyou.network.okhttp.callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public abstract class BaseCallback<T> {

    public abstract void onError(Call call, Exception e);

    public abstract T parseResponse(Response resp);

    public abstract void onResponse(T resp);
}
