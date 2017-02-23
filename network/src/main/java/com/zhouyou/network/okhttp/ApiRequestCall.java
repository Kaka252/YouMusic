package com.zhouyou.network.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.zhouyou.network.okhttp.callback.BaseCallback;
import com.zhouyou.network.okhttp.callback.MainHandler;
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
    public void async(final BaseCallback callback) {
        Call call = newCall();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (call.isCanceled()) {
                    failResponseCallback(callback, call, null);
                    return;
                }
                failResponseCallback(callback, call, e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()) {
                    failResponseCallback(callback, call, null);
                    return;
                }
                if (response.isSuccessful()) {
                    Object result = callback.parseResponse(response);
                    successResponseCallback(callback, result);
                } else {
                    failResponseCallback(callback, call, null);
                }
            }
        });
    }

    private void failResponseCallback(final BaseCallback callback, final Call call, final Exception e) {
        MainHandler.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e);
            }
        });
    }

    private void successResponseCallback(final BaseCallback callback, final Object result) {
        MainHandler.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(result);
            }
        });
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
