package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-08-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.Word.LEFTER, Constants.Keys.Word.RIGHTER],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Word.LEFTER, Constants.Keys.Word.RIGHTER]
)
data class Antonym(
    @ColumnInfo(name = Constants.Keys.Word.LEFTER)
    var left: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Word.RIGHTER)
    var right: String = Constants.Default.STRING
) : BaseParcel() {

    init {
        if (left.compareTo(right) < 0) {
            val temp = left
            left = right
            right = temp
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Antonym
        return Objects.equal(item.left, left) && Objects.equal(item.right, right)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(left, right)
    }
}