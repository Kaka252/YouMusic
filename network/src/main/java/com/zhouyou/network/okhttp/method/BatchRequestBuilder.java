package com.zhouyou.network.okhttp.method;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.param.Params;
import com.zhouyou.network.okhttp.request.BatchRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2017/3/8.
 */
public class BatchRequestBuilder extends BaseRequestBuilder<BatchRequestBuilder> {

    private static final String TAG = "BatchRequestBuilder";

    private List<GetRequestBuilder> requestBuilders;

    public BatchRequestBuilder() {
        this.requestBuilders = new ArrayList<>();
    }

    private String key;

    public BatchRequestBuilder batch(String key, @NonNull List<GetRequestBuilder> requestBuilders) {
        this.key = key;
        this.requestBuilders.addAll(requestBuilders);
        return this;
    }

    public BatchRequestBuilder batch(String key, @NonNull GetRequestBuilder builder) {
        this.key = key;
        requestBuilders.add(builder);
        return this;
    }

    public void put(String key) {
        params.put(key, batchParams());
    }

    @Override
    public ApiRequestCall build() {
        return new BatchRequest(url, tag, params, headers).createRequestCall();
    }

    private String batchParams() {
        if (requestBuilders.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (GetRequestBuilder builder : requestBuilders) {
            if (builder == null) continue;
            String mUrl = builder.url;
            if (TextUtils.isEmpty(mUrl)) continue;
            Params mParams = builder.params;
            if (mParams == null || mParams.isEmpty()) continue;
            mUrl = mParams.join(mUrl);
            sb.append("\"").append(mUrl).append("\"");
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        Log.d(TAG, "batch: " + sb.toString());
        return sb.toString();
    }
}
