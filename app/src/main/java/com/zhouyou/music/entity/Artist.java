package com.zhouyou.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class Artist implements Parcelable {
    public long id;
    public String name;
    public String picUrl;

    protected Artist(Parcel in) {
        id = in.readLong();
        name = in.readString();
        picUrl = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
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
