package com.zhouyou.network.okhttp.method;

import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.interfaces.IParams;
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
    public BaseRequestBuilder addParam(String key, String value) {
        return null;
    }

    @Override
    public BaseRequestBuilder addParams(Map<String, String> params) {
        return null;
    }
}
