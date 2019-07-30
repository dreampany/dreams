package com.dreampany.frame.data.model

import androidx.room.Ignore
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Link(var url: String, var title: String) : BaseParcelKt() {

    @Ignore
    constructor() : this ("", "") {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Link
        return Objects.equal(item.url, url)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(url)
    }
}