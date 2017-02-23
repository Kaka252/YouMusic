package com.zhouyou.network.okhttp.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class GetRequest extends BaseRequest {

    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    protected RequestBody createRequestBody() {
        return null;
    }

    @Override
    protected Request createRequest(RequestBody requestBody) {
        return builder.get().build();
    }


}
