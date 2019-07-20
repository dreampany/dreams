package com.dreampany.frame.data.model

import com.dreampany.translation.data.model.TextTranslation
import com.google.common.base.Objects

/**
 * Created by Roman-372 on 7/4/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseKt(open var time: Long, open var id: String) : BaseParcelKt() {

/*    protected constructor(parcel: Parcel) : this(
        time = parcel.readLong(),
        id = parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(time)
        parcel.writeString(id)
    }*/

    override fun describeContents() = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as TextTranslation
        return Objects.equal(item.id, id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

}