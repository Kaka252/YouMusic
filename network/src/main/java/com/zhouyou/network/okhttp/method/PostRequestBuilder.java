package com.zhouyou.network.okhttp.method;

import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.interfaces.IParams;
import com.zhouyou.network.okhttp.param.Params;
import com.zhouyou.network.okhttp.request.PostRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class PostRequestBuilder extends BaseRequestBuilder<PostRequestBuilder> implements IParams {

    private Map<String, File> files = new HashMap<>();

    public PostRequestBuilder() {
    }

    public PostRequestBuilder(String url) {
        this.url = url;
    }

    public PostRequestBuilder(String url, Params params) {
        this.url = url;
        this.params = params;
    }

    public PostRequestBuilder(String url, Params params, Map<String, File> files) {
        this.url = url;
        this.params = params;
        this.files = files;
    }

    @Override
    public ApiRequestCall build() {
        return new PostRequest(url, tag, params, headers).createRequestCall();
    }

    @Override
    public PostRequestBuilder addParam(String key, String value) {
        if (params == null) {
            params = new Params();
        }
        params.put(key, value);
        return this;
    }

    @Override
    public PostRequestBuilder addParams(Map<String, String> p) {
        if (params == null) {
            params = new Params();
        }
        params.put(p);
        return this;
    }

    @Override
    public PostRequestBuilder addParams(Params params) {
        this.params = params;
        return this;
    }

    public PostRequestBuilder addFile(String key, File f) {
        if (f == null) {
            throw new NullPointerException("File must not be null.");
        }
        files.put(key, f);
        return this;
    }

    public PostRequestBuilder addFiles(Map<String, File> fs) {
        if (fs == null) {
            throw new NullPointerException("File must not be null.");
        }
        files.putAll(fs);
        return this;
    }
}
