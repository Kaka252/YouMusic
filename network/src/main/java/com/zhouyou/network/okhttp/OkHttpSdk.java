package com.zhouyou.network.okhttp;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * 作者：ZhouYou
 * 日期：2017/2/21.
 */
public class OkHttpSdk {

    private static volatile OkHttpClient client;

    private static final long DEFAULT_MILLIS_SECOND = 10000;

    private OkHttpSdk() {

    }

    public static void initialize() {
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

    public static OkHttpClient getClient() {
        if (client == null) {
            throw new NullPointerException("OkHttp has not been initialized yet.");
        }
        return client;
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
}
