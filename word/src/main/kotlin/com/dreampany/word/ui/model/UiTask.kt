package com.dreampany.word.ui.model

import android.os.Parcel
import com.dreampany.word.ui.enums.UiSubtype
import com.dreampany.word.ui.enums.UiType
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.data.model.Task
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

class UiTask<T : BaseKt>(
    val fullscreen: Boolean,
    val type: UiType,
    val subtype: UiSubtype,
    input: T?,
    comment: String?
) : Task<T>(input, comment) {


    private constructor(parcel: Parcel) : this(parcel.readInt() != 0, UiType.valueOf(parcel.readInt()), UiSubtype.valueOf(parcel.readInt())) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
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
    }
}