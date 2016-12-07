package com.zhouyou.network;

import com.alibaba.fastjson.JSON;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.RestRequest;
import com.yolanda.nohttp.rest.StringRequest;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class ObjRequestHandler<T extends AbsApiResponse> extends RestRequest<T> {

    private Class<T> clazz;

    public ObjRequestHandler(String url, RequestMethod requestMethod, Class<T> clazz) {
        super(url, requestMethod);
        this.clazz = clazz;
    }

    @Override
    public T parseResponse(Headers responseHeaders, byte[] responseBody) throws Throwable {
        String json = StringRequest.parseResponseString(responseHeaders, responseBody);
        return JSON.parseObject(json, clazz);
    }
}
