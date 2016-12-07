package com.zhouyou.music.net.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class ArtistBean implements Parcelable {
    public long id;
    public String name;
    public String picUrl;

    public ArtistBean() {
    }

    protected ArtistBean(Parcel in) {
        id = in.readLong();
        name = in.readString();
        picUrl = in.readString();
    }

    public static final Creator<ArtistBean> CREATOR = new Creator<ArtistBean>() {
        @Override
        public ArtistBean createFromParcel(Parcel in) {
            return new ArtistBean(in);
        }

        @Override
        public ArtistBean[] newArray(int size) {
            return new ArtistBean[size];
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
        parcel.writeString(picUrl);
    }
}
