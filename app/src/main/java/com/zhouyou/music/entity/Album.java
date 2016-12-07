package com.zhouyou.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class Album implements Parcelable {
    public long id;
    public String name;
    public Artist artist;
    public String picUrl;

    public Album() {
    }

    protected Album(Parcel in) {
        id = in.readLong();
        name = in.readString();
        artist = in.readParcelable(Artist.class.getClassLoader());
        picUrl = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
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
