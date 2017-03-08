package com.zhouyou.network.okhttp;

import com.zhouyou.network.okhttp.method.GetRequestBuilder;
import com.zhouyou.network.okhttp.method.PostRequestBuilder;
import com.zhouyou.network.okhttp.param.Params;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
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

    private static final long DEFAULT_MILLIS_SECOND = 10000;

    private OkHttpSdk() {
        if (client == null) {
            synchronized (OkHttpSdk.class) {
                if (client == null) {
                    X509TrustManager x509TrustManager = initInsecureTrustManager();
                    SSLSocketFactory sslSocketFactory = initInsecureSslSocketFactory(x509TrustManager);
                    client = new OkHttpClient.Builder()
                            .writeTimeout(DEFAULT_MILLIS_SECOND, TimeUnit.MILLISECONDS)
                            .readTimeout(DEFAULT_MILLIS_SECOND, TimeUnit.MILLISECONDS)
                            .connectTimeout(DEFAULT_MILLIS_SECOND, TimeUnit.MILLISECONDS)
                            .sslSocketFactory(sslSocketFactory, x509TrustManager)
                            .build();
                }
            }
        }
    }

    private static SSLSocketFactory initInsecureSslSocketFactory(TrustManager trustManager) {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 信任所有证书
     */
    private static X509TrustManager initInsecureTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private CookieJar initCookieJar() {
        return new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return null;
            }
        };
    }

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
        return get("");
    }

    public GetRequestBuilder get(String url) {
        return get(url, null);
    }

    public GetRequestBuilder get(String url, Params params) {
        return new GetRequestBuilder(url, params);
    }

    /**
     * 构建Post请求方法
     *
     * @return
     */
    public PostRequestBuilder post() {
        return new PostRequestBuilder();
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
