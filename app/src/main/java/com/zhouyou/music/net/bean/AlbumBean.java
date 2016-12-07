package com.zhouyou.music.net.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class AlbumBean implements Parcelable {
    public long id;
    public String name;
    public ArtistBean artist;
    public String picUrl;

    public AlbumBean() {
    }

    protected AlbumBean(Parcel in) {
        id = in.readLong();
        name = in.readString();
        artist = in.readParcelable(ArtistBean.class.getClassLoader());
        picUrl = in.readString();
    }

    public static final Creator<AlbumBean> CREATOR = new Creator<AlbumBean>() {
        @Override
        public AlbumBean createFromParcel(Parcel in) {
            return new AlbumBean(in);
        }

        @Override
        public AlbumBean[] newArray(int size) {
            return new AlbumBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeParcelable(artist, i);
        parcel.writeString(picUrl);
    }
}
