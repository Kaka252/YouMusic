package com.zhouyou.network.okhttp.param;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 作者：ZhouYou
 * 日期：2017/2/24.
 * 参数
 */
public class Params {

    private static final String TAG = "URLParams";

    private Map<String, String> params;

    public Params() {
        params = new HashMap<>();
    }

    public void put(String key, String value) {
        params.put(key, value);
    }

    public void put(Map<String, String> params) {
        this.params = params;
    }

    public boolean isEmpty() {
        return params == null || params.isEmpty();
    }

    public Set<String> keySet() {
        return params.keySet();
    }

    public String get(String key) {
        return params.get(key);
    }

    /**
     * 拼装url和params
     * @param url
     * @return
     */
    public String join(String url) {
        if (TextUtils.isEmpty(url) || params == null || params.isEmpty()) return url;
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (TextUtils.isEmpty(key)) continue;
            builder.appendQueryParameter(key, params.get(key));
        }
        Log.d(TAG, builder.build().toString());
        return builder.build().toString();
    }

}
