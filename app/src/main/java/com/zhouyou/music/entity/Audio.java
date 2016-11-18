package com.zhouyou.music.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

/**
 * 作者：ZhouYou
 * 日期：2016/11/18.
 */
public class Audio implements Parcelable {

    public String title;
    public String titleKey;
    public String artist;
    public String artistKey;
    public String composer;
    public String album;
    public String albumKey;
    public String displayName;
    public String mimeType;
    public String path;

    public int id;
    public int artistId;
    public int albumId;
    public int year;
    public int track;

    public int duration = 0;
    public int size = 0;

    public boolean isRingtone = false;
    public boolean isPodcast = false;
    public boolean isAlarm = false;
    public boolean isMusic = false;
    public boolean isNotification = false;

    public Audio(Bundle bundle) {
        id = bundle.getInt(MediaStore.Audio.Media._ID);
        title = bundle.getString(MediaStore.Audio.Media.TITLE);
        titleKey = bundle.getString(MediaStore.Audio.Media.TITLE_KEY);
        artist = bundle.getString(MediaStore.Audio.Media.ARTIST);
        artistKey = bundle.getString(MediaStore.Audio.Media.ARTIST_KEY);
        composer = bundle.getString(MediaStore.Audio.Media.COMPOSER);
        album = bundle.getString(MediaStore.Audio.Media.ALBUM);
        albumKey = bundle.getString(MediaStore.Audio.Media.ALBUM_KEY);
        displayName = bundle.getString(MediaStore.Audio.Media.DISPLAY_NAME);
        year = bundle.getInt(MediaStore.Audio.Media.YEAR);
        mimeType = bundle.getString(MediaStore.Audio.Media.MIME_TYPE);
        path = bundle.getString(MediaStore.Audio.Media.DATA);

        artistId = bundle.getInt(MediaStore.Audio.Media.ARTIST_ID);
        albumId = bundle.getInt(MediaStore.Audio.Media.ALBUM_ID);
        track = bundle.getInt(MediaStore.Audio.Media.TRACK);
        duration = bundle.getInt(MediaStore.Audio.Media.DURATION);
        size = bundle.getInt(MediaStore.Audio.Media.SIZE);
        isRingtone = bundle.getInt(MediaStore.Audio.Media.IS_RINGTONE) == 1;
        isPodcast = bundle.getInt(MediaStore.Audio.Media.IS_PODCAST) == 1;
        isAlarm = bundle.getInt(MediaStore.Audio.Media.IS_ALARM) == 1;
        isMusic = bundle.getInt(MediaStore.Audio.Media.IS_MUSIC) == 1;
        isNotification = bundle.getInt(MediaStore.Audio.Media.IS_NOTIFICATION) == 1;
    }

    protected Audio(Parcel in) {
        title = in.readString();
        titleKey = in.readString();
        artist = in.readString();
        artistKey = in.readString();
        composer = in.readString();
        album = in.readString();
        albumKey = in.readString();
        displayName = in.readString();
        mimeType = in.readString();
        path = in.readString();
        id = in.readInt();
        artistId = in.readInt();
        albumId = in.readInt();
        year = in.readInt();
        track = in.readInt();
        duration = in.readInt();
        size = in.readInt();
        isRingtone = in.readByte() != 0;
        isPodcast = in.readByte() != 0;
        isAlarm = in.readByte() != 0;
        isMusic = in.readByte() != 0;
        isNotification = in.readByte() != 0;
    }

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(titleKey);
        dest.writeString(artist);
        dest.writeString(artistKey);
        dest.writeString(composer);
        dest.writeString(album);
        dest.writeString(albumKey);
        dest.writeString(displayName);
        dest.writeString(mimeType);
        dest.writeString(path);
        dest.writeInt(id);
        dest.writeInt(artistId);
        dest.writeInt(albumId);
        dest.writeInt(year);
        dest.writeInt(track);
        dest.writeInt(duration);
        dest.writeInt(size);
        dest.writeByte((byte) (isRingtone ? 1 : 0));
        dest.writeByte((byte) (isPodcast ? 1 : 0));
        dest.writeByte((byte) (isAlarm ? 1 : 0));
        dest.writeByte((byte) (isMusic ? 1 : 0));
        dest.writeByte((byte) (isNotification ? 1 : 0));
    }
}
