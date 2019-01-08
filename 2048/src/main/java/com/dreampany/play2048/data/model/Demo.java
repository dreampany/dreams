package com.dreampany.play2048.data.model;

import android.arch.persistence.room.Ignore;
import android.os.Parcel;

import com.dreampany.frame.data.model.Base;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Demo extends Base {

    @Ignore
    public Demo() {

    }

    @Ignore
    private Demo(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Demo> CREATOR = new Creator<Demo>() {
        @Override
        public Demo createFromParcel(Parcel in) {
            return new Demo(in);
        }

        @Override
        public Demo[] newArray(int size) {
            return new Demo[size];
        }
    };
}
