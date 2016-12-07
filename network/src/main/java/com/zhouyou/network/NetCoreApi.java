package com.zhouyou.network;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.RequestQueue;
import com.zhouyou.network.config.Params;

import java.lang.reflect.ParameterizedType;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class NetCoreApi {

    private static RequestQueue requestQueue;
    private static final int MAX_REQUEST_THREAD = 3;

    static {
        requestQueue = NoHttp.newRequestQueue(MAX_REQUEST_THREAD);
    }

    public static <T extends AbsApiResponse> void doGet(AbsApiRequest<T> request) {
        ParameterizedType parameterizedType = (ParameterizedType) request.getClass().getGenericSuperclass();
        Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
        ObjRequestHandler<T> handler = new ObjRequestHandler<>(request.makeURLForParams(), request.getMethod(), clazz);
        requestQueue.add(0, handler, request.httpRespCallback());
    }

    public static <T extends AbsApiResponse> void doPost(AbsApiRequest<T> request) {
        ParameterizedType parameterizedType = (ParameterizedType) request.getClass().getGenericSuperclass();
        Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
        ObjRequestHandler<T> handler = new ObjRequestHandler<>(request.getUrl(), request.getMethod(), clazz);
        Params params = request.getParams();
        handler.add(params.getMap());
        requestQueue.add(0, handler, request.httpRespCallback());
    }
}
