package com.zhouyou.network.okhttp.interfaces;

import com.zhouyou.network.okhttp.method.BaseRequestBuilder;
import com.zhouyou.network.okhttp.param.Params;

import java.util.Map;

/**
 * 作者：ZhouYou
 * 日期：2017/2/23.
 */
public interface IParams {

    BaseRequestBuilder addParam(String key, String value);
    BaseRequestBuilder addParams(Map<String, String> p);
    BaseRequestBuilder addParams(Params params);
}
