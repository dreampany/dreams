package com.dreampany.scan.data.enums;

import android.os.Parcel;

import com.dreampany.frame.data.enums.Type;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public enum ScanType implements Type {
    TEXT(0), BARCODE(1);

    private int code;

    ScanType(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Type type) {
        if (ScanType.class.isInstance(type)) {
            ScanType item = (ScanType) type;
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

    public static final Creator<ScanType> CREATOR = new Creator<ScanType>() {

        public ScanType createFromParcel(Parcel in) {
            return ScanType.valueOf(in.readInt());
        }

        public ScanType[] newArray(int size) {
            return new ScanType[size];
        }
    };

    public static ScanType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return TEXT;
            case 1:
            default:
                return BARCODE;
        }
    }
}
