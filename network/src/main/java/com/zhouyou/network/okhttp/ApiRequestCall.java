package com.zhouyou.network.okhttp;

import android.support.annotation.NonNull;

import com.zhouyou.network.okhttp.callback.AbsCallback;
import com.zhouyou.network.okhttp.callback.MainThread;
import com.zhouyou.network.okhttp.request.BaseRequest;

import java.io.IOException;
import java.util.List;

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

    private List<Call> calls;

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
     *
     * @param callback
     */
    public void async(final AbsCallback callback) {
        Call call = newCall();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                failResponseCallback(callback, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        Object result = callback.parseResponse(response);
                        successResponseCallback(callback, result);
                    } else {
                        failResponseCallback(callback, call, null);
                    }
                } catch (Exception e) {
                    failResponseCallback(callback, call, e);
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            }
        });
    }

    /**
     * 请求失败后回调
     *
     * @param callback
     * @param call
     * @param e
     */
    private void failResponseCallback(final AbsCallback callback, final Call call, final Exception e) {
        MainThread.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (call.isCanceled()) {
                    callback.onError(call, null);
                } else {
                    callback.onError(call, e);
                }
            }
        });
    }

    /**
     * 请求成功后回调
     *
     * @param callback
     * @param result
     */
    private void successResponseCallback(final AbsCallback callback, final Object result) {
        MainThread.getInstance().execute(new Runnable() {
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
