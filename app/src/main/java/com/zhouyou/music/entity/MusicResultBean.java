package com.zhouyou.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/7.
 */
public class MusicResultBean implements Parcelable {

    public int songCount;

    public List<Song> songs;

    public MusicResultBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.songCount);
        dest.writeTypedList(this.songs);
    }

    protected MusicResultBean(Parcel in) {
        this.songCount = in.readInt();
        this.songs = in.createTypedArrayList(Song.CREATOR);
    }

    public static final Creator<MusicResultBean> CREATOR = new Creator<MusicResultBean>() {
        @Override
        public MusicResultBean createFromParcel(Parcel source) {
            return new MusicResultBean(source);
        }

        @Override
        public MusicResultBean[] newArray(int size) {
            return new MusicResultBean[size];
        }
    };
}
