package com.zhouyou.network.okhttp.request;

import android.text.TextUtils;

import com.zhouyou.network.okhttp.param.Params;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
        if (params.hasFile()) {
            return buildFileRequestParams().build();
        } else {
            return buildRequestParams().build();
        }
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

    private MultipartBody.Builder buildFileRequestParams() {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            if (TextUtils.isEmpty(key)) continue;
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                    RequestBody.create(null, params.get(key)));
        }

        for (String key : params.fileSet()) {
            if (TextUtils.isEmpty(key)) continue;
            File file = params.getFile(key);
            if (file == null || !file.exists()) continue;
            builder.addFormDataPart(key, file.getName(),
                    RequestBody.create(MediaType.parse("application/octet-stream"), file));
        }
        return builder;
    }

}
