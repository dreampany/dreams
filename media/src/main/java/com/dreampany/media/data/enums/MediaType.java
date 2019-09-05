package com.dreampany.media.data.enums;

import android.os.Parcel;

import com.dreampany.framework.data.enums.Type;

public enum MediaType implements Type {
    APK(0), IMAGE(1), AUDIO(2), VIDEO(3), DOCUMENT(4), FILE(5), NONE(6);

    private int code;

    MediaType(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Type type) {
        if (MediaType.class.isInstance(type)) {
            MediaType item = (MediaType) type;
            return compareTo(item) == 0;
        }
        return false;
    }

    @Override
    public int ordinalValue() {
        return code;
    }

    public String toLowerValue() {
        return name().toLowerCase();
    }


    @Override
    public String value() {
        return name();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaType> CREATOR = new Creator<MediaType>() {
        @Override
        public MediaType createFromParcel(Parcel in) {
            return MediaType.valueOf(in.readInt());
        }

        @Override
        public MediaType[] newArray(int size) {
            return new MediaType[size];
        }
    };

    public static MediaType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return APK;
            case 1:
                return IMAGE;
            case 2:
                return AUDIO;
            case 3:
                return VIDEO;
            case 4:
                return DOCUMENT;
            case 5:
                return FILE;
            case 6:
            default:
                return NONE;
        }
    }
}