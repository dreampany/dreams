package com.dreampany.translate.ui.model

import android.os.Parcel
import android.os.Parcelable
import com.dreampany.frame.data.model.BaseParcelKt
import com.dreampany.frame.data.model.TaskKt
import com.dreampany.translate.ui.enums.UiSubtype
import com.dreampany.translate.ui.enums.UiType

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class UiTaskKt<T : BaseParcelKt> : TaskKt<T> {

    var fullscreen: Boolean = false
    var type: UiType? = null
    var subtype: UiSubtype? = null

    private constructor(`in`: Parcel) : super(`in`) {
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
        val CREATOR: Parcelable.Creator<UiTaskKt<*>> = object : Parcelable.Creator<UiTaskKt<*>> {

            override fun createFromParcel(`in`: Parcel): UiTaskKt<*> {
                return UiTaskKt<BaseParcelKt>(`in`)
            }

            override fun newArray(size: Int): Array<UiTaskKt<*>?> {
                return arrayOfNulls(size)
            }
        }
    }
}