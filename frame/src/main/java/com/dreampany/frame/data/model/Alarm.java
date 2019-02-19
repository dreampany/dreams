package com.dreampany.frame.data.model;

import android.os.Parcel;
import androidx.annotation.NonNull;

/**
 * Created by Roman-372 on 2/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
public abstract class Alarm extends Base {

    protected String title;
    protected String description;

    protected Alarm() {
        super();
    }

    protected Alarm(Parcel in) {
        super(in);
        title = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeString(description);
    }
}
