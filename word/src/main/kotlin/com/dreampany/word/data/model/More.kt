package com.dreampany.word.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.util.TimeUtil
import com.dreampany.word.ui.enums.MoreType


/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class More(
    time: Long,
    id: String
) : BaseKt(time, id) {

    lateinit var type: MoreType

    constructor(type: MoreType) : this(TimeUtil.currentTime(), type.name) {
        this.type = type
    }

    @Ignore
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
    }
}