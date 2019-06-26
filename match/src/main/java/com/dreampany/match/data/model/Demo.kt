package com.dreampany.match.data.model

import android.os.Parcel
import android.os.Parcelable

import com.dreampany.frame.data.model.Base

import androidx.room.Ignore

/**
 * Created by Hawladar Roman on 6/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class Demo : Base {

    @Ignore
    constructor() {

    }

    @Ignore
    private constructor(`in`: Parcel) : super(`in`) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
    }

    companion object CREATOR : Parcelable.Creator<More> {
        override fun createFromParcel(parcel: Parcel): More {
            return More(parcel)
        }

        override fun newArray(size: Int): Array<More?> {
            return arrayOfNulls(size)
        }
    }
}
