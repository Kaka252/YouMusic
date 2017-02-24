package com.zhouyou.network.okhttp.request;

import android.text.TextUtils;

import com.zhouyou.network.okhttp.param.Params;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class PostRequest extends BaseRequest {

    public PostRequest(String url, Object tag, Params params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    protected RequestBody createRequestBody() {
        return buildRequestParams().build();
    }

    @Override
    protected Request createRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    private FormBody.Builder buildRequestParams() {
        FormBody.Builder builder = new FormBody.Builder();
        if (params.isEmpty()) return builder;
        for (String key : params.keySet()) {
            if (TextUtils.isEmpty(key)) continue;
            builder.add(key, params.get(key));
        }
        return builder;
    }

}
