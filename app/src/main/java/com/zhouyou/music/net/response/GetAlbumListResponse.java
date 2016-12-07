package com.zhouyou.music.net.response;

import com.zhouyou.music.entity.Song;
import com.zhouyou.network.AbsApiResponse;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class GetAlbumListResponse extends AbsApiResponse {

    public int songCount;

    public List<Song> songs;
}
