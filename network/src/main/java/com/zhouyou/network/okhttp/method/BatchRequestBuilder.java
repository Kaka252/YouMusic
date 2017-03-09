package com.zhouyou.network.okhttp.method;

import android.text.TextUtils;
import android.util.Log;

import com.zhouyou.network.okhttp.ApiRequestCall;
import com.zhouyou.network.okhttp.interfaces.IParams;
import com.zhouyou.network.okhttp.param.Params;
import com.zhouyou.network.okhttp.request.BatchRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：ZhouYou
 * 日期：2017/3/8.
 */
public class BatchRequestBuilder extends BaseRequestBuilder<BatchRequestBuilder> implements IParams {

    private static final String TAG = "BatchRequestBuilder";

    private List<GetRequestBuilder> requestBuilders;

    private String batchKey;

    public void batchKey(String batchKey) {
        this.batchKey = batchKey;
    }

    public BatchRequestBuilder(String url, String batchKey) {
        this.url = url;
        this.batchKey = batchKey;
        this.requestBuilders = new ArrayList<>();
    }

    public BatchRequestBuilder addRequest(List<GetRequestBuilder> requestBuilders) {
        if (requestBuilders != null && !requestBuilders.isEmpty()) {
            this.requestBuilders.addAll(requestBuilders);
        }
        return this;
    }

    public BatchRequestBuilder addRequest(GetRequestBuilder builder) {
        if (builder != null) {
            requestBuilders.add(builder);
        }
        return this;
    }

    @Override
    public ApiRequestCall build() {
        addParam(batchKey, getBatchParams());
        return new BatchRequest(url, tag, params, headers).createRequestCall();
    }

    private String getBatchParams() {
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
            sb.append("\"").append("method=").append(mUrl).append("\"");
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        Log.d(TAG, "batch: " + sb.toString());
        return sb.toString();
    }

    @Override
    public BatchRequestBuilder addParam(String key, String value) {
        if (params == null) {
            params = new Params();
        }
        params.put(key, value);
        return this;
    }

    @Override
    public BatchRequestBuilder addParams(Map<String, String> p) {
        if (params == null) {
            params = new Params();
        }
        params.put(p);
        return this;
    }

    @Override
    public BatchRequestBuilder addParams(Params params) {
        this.params = params;
        return this;
    }
}
