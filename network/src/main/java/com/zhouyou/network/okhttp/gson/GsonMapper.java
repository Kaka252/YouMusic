package com.zhouyou.network.okhttp.gson;

import com.google.gson.Gson;

/**
 * 作者：ZhouYou
 * 日期：2017/2/24.
 */
public class GsonMapper {

    private static volatile GsonMapper instance;

    private Gson gson;

    private GsonMapper() {
        gson = new Gson();
    }

    public static GsonMapper getInstance() {
        if (instance == null) {
            synchronized (GsonMapper.class) {
                if (instance == null) {
                    instance = new GsonMapper();
                }
            }
        }
        return instance;
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

}
