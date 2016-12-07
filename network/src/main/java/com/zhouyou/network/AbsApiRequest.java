package com.zhouyou.network;

import android.text.TextUtils;
import android.util.Log;

import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;
import com.zhouyou.network.config.Params;

import java.lang.reflect.Field;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public abstract class AbsApiRequest<T extends AbsApiResponse> {

    private IRespCallback<T> callback;

    public AbsApiRequest(IRespCallback<T> callBack) {
        this.callback = callBack;
    }

    public OnResponseListener<T> httpRespCallback() {
        return new OnResponseListener<T>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<T> response) {
                if (callback == null) return;
                if (response.get().isOK()) {
                    callback.onSuccess(what, response.get());
                } else {
                }
            }

            @Override
            public void onFailed(int what, Response<T> response) {
                if (callback == null) return;
                callback.onError(what, response.get());
            }

            @Override
            public void onFinish(int what) {

            }
        };
    }

    protected abstract String getUrl();

    protected abstract RequestMethod getMethod();

    /**
     * 制作带参数的路径
     */
    public String makeURLForParams() {
        String url = getUrl();
        if (getMethod() == RequestMethod.POST) {
            return url;
        }
        Params params = getParams();
        if (TextUtils.isEmpty(url)) return null;
        if (params == null || !params.hasParams()) return url;
        url += ("?" + params.getParams());
        return url;
    }

    public Params getParams() {
        Params params = new Params();
        Field[] fields = getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            try {
                sb.append(field.getName()).append(" | ");
                params.put(field.getName(), field.get(this) + "");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Log.e("Params", sb.toString());
        return params;
    }
}
