package com.dreampany.tools.data.model

import androidx.room.Ignore
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.enums.MoreType
import kotlinx.android.parcel.Parcelize


/**
 * Created by Hawladar Roman on 7/24/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Parcelize
data class More(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var type : MoreType = MoreType.DEFAULT
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    constructor(type: MoreType) : this(type.name) {
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