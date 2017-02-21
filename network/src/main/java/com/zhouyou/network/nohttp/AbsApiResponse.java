package com.zhouyou.network.nohttp;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public abstract class AbsApiResponse {

    public long code;

    public String msg;

    public String result;

    public boolean isOK() {
        return code == 200;
    }
}
