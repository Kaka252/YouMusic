package com.zhouyou.network.nohttp;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public interface IRespCallback<T extends AbsApiResponse> {

    void onSuccess(int what, T resp);

    void onError(int what, T error);
}
