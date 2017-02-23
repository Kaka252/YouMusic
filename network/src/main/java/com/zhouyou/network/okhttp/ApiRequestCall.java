package com.zhouyou.network.okhttp;

import android.support.annotation.NonNull;

import com.zhouyou.network.okhttp.request.BaseRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public class ApiRequestCall {

    private Request request;
    private Call call;
    private BaseRequest baseRequest;
    private OkHttpClient client;

    public ApiRequestCall(@NonNull BaseRequest baseRequest) {
        client = OkHttpSdk.getInstance().getClient();
        this.baseRequest = baseRequest;
    }

    private Request setupRequest() {
        return baseRequest.setupRequest();
    }

    private Call newCall() {
        request = setupRequest();
        call = client.newCall(request);
        return call;
    }

    /**
     * 异步调用
     * @param callback
     */
    public void async(Callback callback) {
        Call call = newCall();
        call.enqueue(callback);
    }

    public Response sync() throws IOException {
        Call call = newCall();
        return call.execute();
    }

    public Request getRequest() {
        return request;
    }

    public Call getCall() {
        return call;
    }
}
