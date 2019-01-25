package com.dreampany.crime.data.model;

import androidx.room.Ignore;
import android.os.Parcel;

import com.dreampany.frame.data.model.Base;

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class Crime extends Base {

    @Ignore
    public Crime() {

    }

    @Ignore
    private Crime(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Crime> CREATOR = new Creator<Crime>() {
        @Override
        public Crime createFromParcel(Parcel in) {
            return new Crime(in);
        }

        @Override
        public Crime[] newArray(int size) {
            return new Crime[size];
        }
    };
}
