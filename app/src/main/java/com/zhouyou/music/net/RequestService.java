package com.zhouyou.music.net;

import com.zhouyou.music.net.bean.MusicResultBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 作者：ZhouYou
 * 日期：2017/1/17.
 */
public interface RequestService {

    @GET
    Call<MusicResultBean> getMusicJson();
}
