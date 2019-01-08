package com.dreampany.frame.data.enums;

import android.os.Parcelable;

public interface Type extends Parcelable {
    boolean equals(Type type);

    String value();

    String toLowerValue();

    int ordinalValue();
}