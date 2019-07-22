package com.dreampany.word.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.util.TimeUtil
import com.dreampany.translation.data.model.TextTranslation
import com.google.common.base.Objects

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
 class Load(
      time: Long,
      id: String
) : BaseKt(time, id) {

    var current: Int = 0
    var total: Int = 0

    constructor(current: Int, total: Int) : this(TimeUtil.currentTime(), "") {
        this.current = current
        this.total = total
    }

    @Ignore
    private constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readString()!!) {
        current = parcel.readInt()
        total = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(time)
        dest.writeString(id)
        dest.writeInt(current)
        dest.writeInt(total)
    }

    companion object CREATOR : Parcelable.Creator<Load> {
        override fun createFromParcel(parcel: Parcel): Load {
            return Load(parcel)
        }

        override fun newArray(size: Int): Array<Load?> {
            return arrayOfNulls(size)
        }
    }

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