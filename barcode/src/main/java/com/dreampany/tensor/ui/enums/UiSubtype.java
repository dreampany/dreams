package com.dreampany.tensor.ui.enums;

import android.os.Parcel;

import com.dreampany.frame.data.enums.Type;

/**
 * Created by Hawladar Roman on 30/4/18.
 * Dreampany
 * dreampanymail@gmail.com
 */
public enum UiSubtype implements Type {
    EDIT, VIEW;

    @Override
    public boolean equals(Type type) {
        if (UiSubtype.class.isInstance(type)) {
            UiSubtype item = (UiSubtype) type;
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

    public static final Creator<UiSubtype> CREATOR = new Creator<UiSubtype>() {

        public UiSubtype createFromParcel(Parcel in) {
            return UiSubtype.values()[in.readInt()];
        }

        public UiSubtype[] newArray(int size) {
            return new UiSubtype[size];
        }

    };

    public static UiSubtype valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return EDIT;
            case 1:
                return VIEW;
            default:
                return null;
        }
    }
}
