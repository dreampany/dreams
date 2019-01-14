package com.dreampany.lca.data.enums;

import android.os.Parcel;
import com.dreampany.frame.data.enums.Type;

public enum IcoStatus implements Type {
    LIVE(0), UPCOMING(1), FINISHED(2);

    private int code;

    IcoStatus(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Type type) {
        if (IcoStatus.class.isInstance(type)) {
            IcoStatus item = (IcoStatus) type;
            return compareTo(item) == 0;
        }
        return false;
    }

    @Override
    public int ordinalValue() {
        return code;
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

    public static final Creator<IcoStatus> CREATOR = new Creator<IcoStatus>() {

        @Override
        public IcoStatus createFromParcel(Parcel in) {
            return IcoStatus.valueOf(in.readInt());
        }

        @Override
        public IcoStatus[] newArray(int size) {
            return new IcoStatus[size];
        }

    };

    public static IcoStatus valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return LIVE;
            case 1:
                return UPCOMING;
            case 2:
            default:
                return FINISHED;
        }
    }
}