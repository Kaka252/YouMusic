package com.zhouyou.network.okhttp;

import android.content.Context;

import com.zhouyou.network.okhttp.config.HttpConfig;
import com.zhouyou.network.okhttp.method.BatchRequestBuilder;
import com.zhouyou.network.okhttp.method.GetRequestBuilder;
import com.zhouyou.network.okhttp.method.PostRequestBuilder;
import com.zhouyou.network.okhttp.param.Params;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 作者：ZhouYou
 * 日期：2017/2/21.
 */
public class OkHttpSdk {

    public static OkHttpSdk getInstance() {
        return OkHttpProxy.SDK;
    }

    private static final class OkHttpProxy {
        private static final OkHttpSdk SDK = new OkHttpSdk();
    }

    private static volatile OkHttpClient client;

    private OkHttpSdk() {
        if (client == null) {
            synchronized (OkHttpSdk.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder().build();
                }
            }
        }
    }

    /**
     * 初始化配置
     *
     * @param context
     */
    public static void initConfig(Context context) {
        Cache cache = HttpConfig.initCache(context);
        X509TrustManager trustManager = HttpConfig.initInsecureTrustManager();
        SSLSocketFactory sslSocketFactory = HttpConfig.initInsecureSslSocketFactory(trustManager);
        client.newBuilder()
                .writeTimeout(HttpConfig.DEFAULT_MILLIS_SECOND, TimeUnit.MILLISECONDS)
                .readTimeout(HttpConfig.DEFAULT_MILLIS_SECOND, TimeUnit.MILLISECONDS)
                .connectTimeout(HttpConfig.DEFAULT_MILLIS_SECOND, TimeUnit.MILLISECONDS)
                .cache(cache)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
    }

    /**
     * 获取OKhttp实例
     *
     * @return
     */
    public OkHttpClient getClient() {
        if (client == null) {
            throw new NullPointerException("OkHttp has not been initialized yet.");
        }
        return client;
    }

    /**
     * 构建Get请求方法
     *
     * @return
     */
    public GetRequestBuilder get() {
        return new GetRequestBuilder();
    }

    public GetRequestBuilder get(String url) {
        return new GetRequestBuilder(url);
    }

    public GetRequestBuilder get(String url, Params params) {
        return new GetRequestBuilder(url, params);
    }

    /**
     * 构建批量请求方法
     *
     * @return
     */
    public BatchRequestBuilder batch() {
        return new BatchRequestBuilder();
    }

    public BatchRequestBuilder batch(String url) {
        return new BatchRequestBuilder(url);
    }

    public BatchRequestBuilder batch(String url, String batchKey) {
        return new BatchRequestBuilder(url, batchKey);
    }

    /**
     * 构建Post请求方法
     *
     * @return
     */
    public PostRequestBuilder post() {
        return new PostRequestBuilder();
    }

    public PostRequestBuilder post(String url) {
        return new PostRequestBuilder(url);
    }

    public PostRequestBuilder post(String url, Params params) {
        return new PostRequestBuilder(url, params);
    }

    /**
     * 取消网络请求
     *
     * @param tag
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (call == null || call.request() == null) continue;
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (call == null || call.request() == null) continue;
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
    }

}
