package com.zhouyou.music.net.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class SongBean implements Parcelable {

    public long id;
    public String name;
    public List<ArtistBean> artists;
    public AlbumBean album;
    public String audio;
    public int djProgramId;
    public String page;

    public SongBean() {
    }

    protected SongBean(Parcel in) {
        id = in.readLong();
        name = in.readString();
        artists = in.createTypedArrayList(ArtistBean.CREATOR);
        album = in.readParcelable(AlbumBean.class.getClassLoader());
        audio = in.readString();
        djProgramId = in.readInt();
        page = in.readString();
    }

    public static final Creator<SongBean> CREATOR = new Creator<SongBean>() {
        @Override
        public SongBean createFromParcel(Parcel in) {
            return new SongBean(in);
        }

        @Override
        public SongBean[] newArray(int size) {
            return new SongBean[size];
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
        parcel.writeTypedList(artists);
        parcel.writeParcelable(album, i);
        parcel.writeString(audio);
        parcel.writeInt(djProgramId);
        parcel.writeString(page);
    }

    @Override
    public String toString() {
        return "SongBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artists=" + artists +
                ", album=" + album +
                ", audio='" + audio + '\'' +
                ", djProgramId=" + djProgramId +
                ", page='" + page + '\'' +
                '}';
    }
}
