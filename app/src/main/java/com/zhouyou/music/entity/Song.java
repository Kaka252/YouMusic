package com.zhouyou.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/12/6.
 */
public class Song implements Parcelable {

    public long id;
    public String name;
    public List<Artist> artists;
    public Album album;
    public String audio;
    public int djProgramId;
    public String page;

    public Song() {
    }

    protected Song(Parcel in) {
        id = in.readLong();
        name = in.readString();
        artists = in.createTypedArrayList(Artist.CREATOR);
        album = in.readParcelable(Album.class.getClassLoader());
        audio = in.readString();
        djProgramId = in.readInt();
        page = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
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
        return "Song{" +
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
