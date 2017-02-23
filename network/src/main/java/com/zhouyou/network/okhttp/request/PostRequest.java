package com.zhouyou.network.okhttp.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class PostRequest extends BaseRequest {

    public PostRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    protected RequestBody createRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        return null;
    }

    @Override
    protected Request createRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }


}
