package com.dreampany.tensor.data.enums;

import android.os.Parcel;
import android.os.Parcelable;

import com.dreampany.framework.data.enums.Type;

public enum MoreType implements Type {
    APPS, RATE_US, ABOUT_US, FEEDBACK, SETTINGS;

    @Override
    public boolean equals(Type type) {
        if (MoreType.class.isInstance(type)) {
            MoreType item = (MoreType) type;
            return compareTo(item) == 0;
        }
        return false;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
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

    public static final Parcelable.Creator<MoreType> CREATOR = new Parcelable.Creator<MoreType>() {

        public MoreType createFromParcel(Parcel in) {
            return MoreType.valueOf(in.readInt());
        }

        public MoreType[] newArray(int size) {
            return new MoreType[size];
        }

    };

    public static MoreType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return SETTINGS;
            default:
                return null;
        }
    }
}