package com.zhouyou.network.okhttp.method;

import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.interfaces.IParams;
import com.zhouyou.network.okhttp.param.Params;
import com.zhouyou.network.okhttp.request.GetRequest;

import java.util.Map;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class GetRequestBuilder extends BaseRequestBuilder<GetRequestBuilder> implements IParams {

    public GetRequestBuilder() {
        this("");
    }

    public GetRequestBuilder(String url) {
        this(url, null);
    }

    public GetRequestBuilder(String url, Params ps) {
        this.url = url;
        params = ps;
        if (params == null) {
            params = new Params();
        }
    }

    @Override
    public GetRequestBuilder addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    @Override
    public GetRequestBuilder addParams(Map<String, String> p) {
        params.put(p);
        return this;
    }

    @Override
    public GetRequestBuilder addParams(Params params) {
        if (params == null) {
            throw new NullPointerException("Params must not be null.");
        }
        this.params = params;
        return this;
    }

    @Override
    public ApiRequestCall build() {
        if (!params.isEmpty()) {
            url = params.join(url);
        }
        return new GetRequest(url, tag, params, headers).createRequestCall();
    }
}
