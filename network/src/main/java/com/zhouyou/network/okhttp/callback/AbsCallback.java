package com.zhouyou.network.okhttp.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public abstract class AbsCallback<T> {
    /**
     * UI Thread
     *
     * @param call
     * @param e
     */
    public abstract void onError(Call call, Exception e);

    /**
     * Child Thread
     *
     * @param resp
     * @return
     * @throws IOException
     */
    public abstract T parseResponse(Response resp) throws IOException;

    /**
     * UI Thread
     *
     * @param resp
     */
    public abstract void onResponse(T resp);
}
