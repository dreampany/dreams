package com.dreampany.play2048.data.model

import android.os.Parcel
import android.os.Parcelable
import com.dreampany.frame.data.model.Base
import com.dreampany.play2048.ui.enums.MoreType


/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
data class More(val type: MoreType?) : Base() {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable<MoreType>(MoreType::class.java.getClassLoader()) ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeParcelable(type, flags)
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