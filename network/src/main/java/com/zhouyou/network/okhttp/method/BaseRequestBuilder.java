package com.zhouyou.network.okhttp.method;

import com.zhouyou.network.okhttp.ApiRequestCall;

import java.io.File;
import java.util.Map;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public abstract class BaseRequestBuilder<T extends BaseRequestBuilder> {

    protected String url;

    protected Object tag;

    protected Map<String, String> params;

    protected Map<String, File> files;

    protected Map<String, String> headers;

    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public abstract ApiRequestCall build();
}
