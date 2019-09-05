package com.dreampany.match.ui.enums;

import android.os.Parcel;

import com.dreampany.framework.data.enums.Type;

/**
 * Created by Hawladar Roman on 30/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 */
public enum UiType implements Type {
    COIN, MORE;

    @Override
    public boolean equals(Type type) {
        if (UiType.class.isInstance(type)) {
            UiType item = (UiType) type;
            return compareTo(item) == 0;
        }
        return false;
    }

    @Override
    public String value() {
        return name();
    }

    @Override
    public String toLowerValue() {
        return value().toLowerCase();
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UiType> CREATOR = new Creator<UiType>() {

        public UiType createFromParcel(Parcel in) {
            return UiType.values()[in.readInt()];
        }

        public UiType[] newArray(int size) {
            return new UiType[size];
        }

    };

    public static UiType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return COIN;
            case 1:
                return MORE;
            default:
                return null;
        }
    }
}
