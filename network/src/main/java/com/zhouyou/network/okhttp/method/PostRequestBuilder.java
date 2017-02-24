package com.zhouyou.network.okhttp.method;

import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.interfaces.IParams;
import com.zhouyou.network.okhttp.param.Params;
import com.zhouyou.network.okhttp.request.PostRequest;

import java.util.Map;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class PostRequestBuilder extends BaseRequestBuilder<PostRequestBuilder> implements IParams {
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
}
