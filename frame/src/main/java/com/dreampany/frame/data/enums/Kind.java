/*
package com.dreampany.frame.data.enums;

import android.os.Parcel;

*/
/**
 * Created by Hawladar Roman on 5/17/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 *//*


public enum Kind implements Type {
    READ, WRITE;

    @Override
    public boolean equals(Type type) {
        if (Kind.class.isInstance(type)) {
            Kind item = (Kind) type;
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

    public static final Creator<Kind> CREATOR = new Creator<Kind>() {

        public Kind createFromParcel(Parcel in) {
            return Kind.valueOf(in.readInt());
        }

        public Kind[] newArray(int size) {
            return new Kind[size];
        }

    };

    public static Kind valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return READ;
            case 1:
                return WRITE;
            default:
                return null;
        }
    }
}
*/
