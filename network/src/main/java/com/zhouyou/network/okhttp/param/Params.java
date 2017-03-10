package com.zhouyou.network.okhttp.param;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
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

    private Map<String, File> fileParams;

    public Params() {
        params = new HashMap<>();
        fileParams = new HashMap<>();
    }

    public void put(String key, String value) {
        params.put(key, value);
    }

    public void put(Map<String, String> ps) {
        if (ps == null) return;
        params.putAll(ps);
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

    public Set<String> fileSet() {
        return fileParams.keySet();
    }

    public File getFile(String key) {
        return fileParams.get(key);
    }

    public void putFile(String key, File f) {
        fileParams.put(key, f);
    }

    public void putFiles(Map<String, File> fs) {
        if (fs == null) return;
        fileParams.putAll(fs);
    }

    public boolean hasFile() {
        return fileParams == null || fileParams.isEmpty();
    }



    /**
     * 拼装url和params
     *
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
