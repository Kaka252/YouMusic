package com.zhouyou.network.config;

import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class Params {

    protected class P {
        String key;
        String value;
    }

    protected class F {
        String name;
        File file;
    }

    private boolean upload = false;

    private LinkedHashMap<String, String> pListMaps = new LinkedHashMap<>();
    private LinkedList<P> pList = new LinkedList<>();
    private LinkedList<F> fList = new LinkedList<>();

    public void put(String key, String value) {
        if (TextUtils.isEmpty(key) || value == null) return;
        P p = new P();
        p.key = key;
        p.value = value;
        pList.add(p);
        pListMaps.put(p.key, p.value);
    }

    public String getParams() {
        StringBuilder sb = new StringBuilder();
        for (P p : pList) {
            String param = p.value;
            if (!TextUtils.isEmpty(param)) {
                try {
                    param = URLEncoder.encode(p.value, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                param = "";
            }
            sb.append(p.key).append("=").append(param).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public LinkedHashMap<String, String> getMap() {
        return pListMaps;
    }

    public boolean hasParams() {
        return pList.size() > 0;
    }

    public void put(String name, File file) {
        F f = new F();
        f.name = name;
        f.file = file;
        fList.add(f);
        upload = true;
    }

    boolean hasUpload() {
        return upload;
    }

    public LinkedList<F> getFList() {
        return fList;
    }
}
