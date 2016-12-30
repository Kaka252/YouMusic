package com.zhouyou.remote;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：ZhouYou
 * 日期：2016/12/30.
 */
public class MusicConfig implements Parcelable {



    /**
     * 0 - 状态信息 | 1 - 进度信息
     */
    private int dataType;

    private Bundle extra;

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setExtra(Bundle extra) {
        this.extra = extra;
    }

    public Bundle getExtra() {
        return extra;
    }

    public int getDataType() {
        return dataType;
    }

    public MusicConfig() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dataType);
        dest.writeBundle(this.extra);
    }

    protected MusicConfig(Parcel in) {
        this.dataType = in.readInt();
        this.extra = in.readBundle(getClass().getClassLoader());
    }

    public static final Creator<MusicConfig> CREATOR = new Creator<MusicConfig>() {
        @Override
        public MusicConfig createFromParcel(Parcel source) {
            return new MusicConfig(source);
        }

        @Override
        public MusicConfig[] newArray(int size) {
            return new MusicConfig[size];
        }
    };
}
