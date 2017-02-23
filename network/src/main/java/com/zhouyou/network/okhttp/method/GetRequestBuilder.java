package com.zhouyou.network.okhttp.method;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.interfaces.IParams;
import com.zhouyou.network.okhttp.request.GetRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class GetRequestBuilder extends BaseRequestBuilder<GetRequestBuilder> implements IParams {

    private static final String TAG = "GetRequestBuilder";

    @Override
    public GetRequestBuilder addParam(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    @Override
    public GetRequestBuilder addParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public ApiRequestCall build() {
        if (params != null && !params.isEmpty()) {
            url = getParams();
        }
        return new GetRequest(url, tag, params, headers).createRequestCall();
    }

    private String getParams() {
        if (TextUtils.isEmpty(url) || params == null || params.isEmpty()) return url;
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (TextUtils.isEmpty(key)) continue;
            builder.appendQueryParameter(key, params.get(key));
        }
        Log.d(TAG, builder.build().toString());
        return builder.build().toString();
    }

}
