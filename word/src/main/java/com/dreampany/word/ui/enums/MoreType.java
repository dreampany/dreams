package com.dreampany.word.ui.enums;

import android.os.Parcel;

import com.dreampany.frame.data.enums.Type;

/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public enum MoreType implements Type {
    APPS, RATE_US, FEEDBACK, INVITE, SETTINGS, LICENSE, ABOUT;

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

    public static final Creator<MoreType> CREATOR = new Creator<MoreType>() {

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
                return APPS;
            case 1:
                return RATE_US;
            case 2:
                return FEEDBACK;
            case 3:
                return INVITE;
            case 4:
                return SETTINGS;
            case 5:
                return LICENSE;
            case 6:
            default:
                return ABOUT;
        }
    }
}