package com.dreampany.lca.data.enums;

import android.os.Parcel;

import com.dreampany.frame.data.enums.Type;

public enum CoinSource implements Type {
    CMC, CC;

    @Override
    public boolean equals(Type type) {
        if (CoinSource.class.isInstance(type)) {
            CoinSource item = (CoinSource) type;
            return compareTo(item) == 0;
        }
        return false;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public String value() {
        return name();
    }

    @Override
    public String toLowerValue() {
        return name().toLowerCase();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CoinSource> CREATOR = new Creator<CoinSource>() {

        @Override
        public CoinSource createFromParcel(Parcel in) {
            return CoinSource.valueOf(in.readInt());
        }

        @Override
        public CoinSource[] newArray(int size) {
            return new CoinSource[size];
        }

    };

    public static CoinSource valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return CMC;
            case 1:
            default:
                return CC;
        }
    }
}