package com.zhouyou.network.okhttp;

import android.text.TextUtils;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * 作者：ZhouYou
 * 日期：2017/2/22.
 */
public abstract class ApiRequest {

    /**
     * url
     */
    protected String url;
    /**
     * 参数
     */
    protected Map<String, String> params;
    /**
     * 请求头
     */
    protected Map<String, String> headers;
    /**
     * 标识，用来cancel
     */
    protected Object tag;

    public ApiRequest(String url, Map<String, String> params, Map<String, String> headers, Object tag) {
        this.url = url;
        this.params = params;
        this.headers = headers;
        this.tag = tag;
    }

    private void request() {
        Request request = new Request.Builder()
                .headers(buildHeaders())
                .url(url)
                .tag(tag)
                .build();
        OkHttpSdk.getClient().newCall(request);
    }

    private Headers buildHeaders() {
        Headers.Builder builder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) builder.build();
        for (String key : headers.keySet()) {
            if (TextUtils.isEmpty(key)) continue;
            builder.add(key, headers.get(key));
        }
        return builder.build();
    }

//    private CacheControl buildCacheControl() {
//
//    }

}
