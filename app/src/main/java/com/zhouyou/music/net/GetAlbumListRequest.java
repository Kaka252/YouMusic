package com.zhouyou.music.net;

import com.yolanda.nohttp.RequestMethod;
import com.zhouyou.music.net.response.GetAlbumListResponse;
import com.zhouyou.network.AbsApiRequest;
import com.zhouyou.network.IRespCallback;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class GetAlbumListRequest extends AbsApiRequest<GetAlbumListResponse> {

    public int type;
    public String s;
    public int limit;
    public int offset;

    public GetAlbumListRequest(IRespCallback<GetAlbumListResponse> callback) {
        super(callback);
    }

    @Override
    public String getUrl() {
        return URLConfig.URL_GET_ALBUM_LIST;
    }

    @Override
    protected RequestMethod getMethod() {
        return RequestMethod.GET;
    }
}
