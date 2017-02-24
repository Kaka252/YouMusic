package com.zhouyou.network.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * 作者：ZhouYou
 * 日期：2017/2/24.
 */
public abstract class StringCallback extends AbsCallback<String> {

    @Override
    public String parseResponse(Response resp) throws IOException {
        return resp.body().string();
    }
}
