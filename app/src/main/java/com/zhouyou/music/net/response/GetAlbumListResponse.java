package com.zhouyou.music.net.response;

import com.zhouyou.music.net.bean.MusicResultBean;
import com.zhouyou.network.nohttp.AbsApiResponse;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class GetAlbumListResponse extends AbsApiResponse {

    public GetAlbumListResponse() {
    }

    public MusicResultBean result;

    public MusicResultBean getResult() {
        return result;
    }
}
