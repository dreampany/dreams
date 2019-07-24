package com.dreampany.history.data.model

import com.dreampany.history.ui.enums.MoreType
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.util.TimeUtil
import kotlinx.android.parcel.Parcelize


/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Parcelize
data class More(
    override var time: Long,
    override var id: String,
    var type : MoreType
) : BaseKt() {

    constructor(type: MoreType) : this(TimeUtil.currentTime(), type.name, type) {
        this.type = type
    }

 /*   @Ignore
    private constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readString()!!) {
        type = MoreType.valueOf(parcel.readString()!!)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(time)
        dest.writeString(id)
        dest.writeString(type.name)
    }

    companion object CREATOR : Parcelable.Creator<More> {
        override fun createFromParcel(parcel: Parcel): More {
            return More(parcel)
        }

        override fun newArray(size: Int): Array<More?> {
            return arrayOfNulls(size)
        }
    }*/
}