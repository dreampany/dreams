package com.dreampany.tube.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.tube.misc.AppConstants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 21/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [AppConstants.Keys.Related.LEFTER, AppConstants.Keys.Related.RIGHTER],
        unique = true
    )], primaryKeys = [AppConstants.Keys.Related.LEFTER, AppConstants.Keys.Related.RIGHTER]
)
data class Related(
    @ColumnInfo(name = AppConstants.Keys.Related.LEFTER)
    var left: String,
    @ColumnInfo(name = AppConstants.Keys.Related.RIGHTER)
    var right: String
) : BaseParcel() {

    init {
        if (left.compareTo(right) <= 0) {
            this.left = right
            this.right = left
        }
    }

    override fun hashCode(): Int = Objects.hashCode(left, right)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Related
        return Objects.equal(this.left, item.left) && Objects.equal(this.right, item.right)
    }

    override fun toString(): String = "Related (left:right) == $left:$right"

    fun other(part: String) = if (left == part) right else left
}