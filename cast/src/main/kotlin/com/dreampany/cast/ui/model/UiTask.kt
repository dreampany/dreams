package com.dreampany.cast.ui.model

import com.dreampany.cast.ui.enums.UiSubtype
import com.dreampany.cast.ui.enums.UiType
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.data.model.TaskKt
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class UiTask<T : BaseKt>(
    val fullscreen: Boolean,
    val type: UiType,
    val subtype: UiSubtype
) : TaskKt<T>() {

/*    var type: UiType? = null
    var subtype: UiSubtype? = null*/

/*    private constructor(`in`: Parcel) : super(`in`) {
        fullscreen = `in`.readByte().toInt() != 0
        type = UiType.valueOf(`in`.readInt())
        subtype = UiSubtype.valueOf(`in`.readInt())
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeByte((if (fullscreen) 1 else 0).toByte())
        dest.writeInt(if (type == null) -1 else type!!.ordinal)
        dest.writeInt(if (subtype == null) -1 else subtype!!.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UiTask<*>> = object : Parcelable.Creator<UiTask<*>> {

            override fun createFromParcel(`in`: Parcel): UiTask<*> {
                return UiTask<BaseParcelKt>(`in`)
            }

            override fun newArray(size: Int): Array<UiTask<*>?> {
                return arrayOfNulls(size)
            }
        }
    }*/
}