package com.zhouyou.network.okhttp.callback;

import com.zhouyou.network.okhttp.gson.GsonMapper;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * 作者：ZhouYou
 * 日期：2017/2/24.
 */
public abstract class GsonCallback<T> extends AbsCallback<T> {

    @Override
    public T parseResponse(Response resp) throws IOException {
        String result = resp.body().string();
        Class<T> clazz = transform();
        if (clazz == String.class) {
            return (T) result;
        } else {
            return GsonMapper.getInstance().getGson().fromJson(result, clazz);
        }
    }

    private Class<T> transform() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] type = parameterizedType.getActualTypeArguments();
        return (Class<T>) type[0];
    }
}
