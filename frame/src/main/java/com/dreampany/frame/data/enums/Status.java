package com.dreampany.frame.data.enums;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hawladar Roman on 5/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */

public enum Status implements Type {
    SUCCESS, ERROR, READING, EMPTY;

    @Override
    public boolean equals(Type type) {
        if (Status.class.isInstance(type)) {
            Status item = (Status) type;
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

    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {

        public Status createFromParcel(Parcel in) {
            return Status.valueOf(in.readInt());
        }

        public Status[] newArray(int size) {
            return new Status[size];
        }

    };

    public static Status valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return SUCCESS;
            case 1:
                return ERROR;
            case 2:
                return READING;
            default:
                return null;
        }
    }
}
