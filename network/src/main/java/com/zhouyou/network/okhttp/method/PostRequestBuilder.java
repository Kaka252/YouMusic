package com.zhouyou.network.okhttp.method;

import android.support.annotation.NonNull;

import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.FileParam;
import com.zhouyou.network.okhttp.interfaces.IParams;
import com.zhouyou.network.okhttp.param.Params;
import com.zhouyou.network.okhttp.request.PostRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class PostRequestBuilder extends BaseRequestBuilder<PostRequestBuilder> implements IParams {

    private List<FileParam> files = new ArrayList<>();

    public PostRequestBuilder() {
        this("", null);
    }

    public PostRequestBuilder(String url, Params params) {
        this(url, params, null);
    }

    public PostRequestBuilder(String url, Params params, List<FileParam> files) {
        this.url = url;
        this.params = params;
        this.files = files;
    }

    @Override
    public ApiRequestCall build() {
        return new PostRequest(url, tag, params, files, headers).createRequestCall();
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

    public PostRequestBuilder file(@NonNull FileParam fileParam) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.add(fileParam);
        return this;
    }

    public PostRequestBuilder files(@NonNull List<FileParam> fileParams) {
        if (files == null) {
            files = new ArrayList<>();
        }
        files.addAll(fileParams);
        return this;
    }
}
