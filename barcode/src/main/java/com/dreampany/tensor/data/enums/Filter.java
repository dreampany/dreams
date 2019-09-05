package com.dreampany.tensor.data.enums;

import android.os.Parcel;
import android.os.Parcelable;

import com.dreampany.framework.data.enums.Type;

public enum Filter implements Type {
    ALL, ACTIVE, COMPLETED;

    @Override
    public boolean equals(Type type) {
        if (Filter.class.isInstance(type)) {
            Filter item = (Filter) type;
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

    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {

        public Filter createFromParcel(Parcel in) {
            return Filter.valueOf(in.readInt());
        }

        public Filter[] newArray(int size) {
            return new Filter[size];
        }

    };

    public static Filter valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return ALL;
            case 1:
                return ACTIVE;
            case 2:
                return COMPLETED;
            default:
                return null;
        }
    }
}
