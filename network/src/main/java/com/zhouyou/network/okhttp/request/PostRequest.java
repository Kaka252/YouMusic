package com.zhouyou.network.okhttp.request;

import android.text.TextUtils;

import com.zhouyou.network.okhttp.FileParam;
import com.zhouyou.network.okhttp.param.Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class PostRequest extends BaseRequest {

    private List<FileParam> files = new ArrayList<>();

    public PostRequest(String url, Object tag, Params params, List<FileParam> files, Map<String, String> headers) {
        super(url, tag, params, headers);
        this.files = files;
    }

    @Override
    protected RequestBody createRequestBody() {
        if (files == null || files.isEmpty()) {
            return buildRequestParams().build();
        } else {
            return buildFileRequestParams().build();
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

        for (FileParam fp : files) {
            if (fp == null) continue;
            builder.addFormDataPart(fp.name, fp.fileName,
                    RequestBody.create(MediaType.parse("application/octet-stream"), fp.file));
        }
        return builder;
    }

}
