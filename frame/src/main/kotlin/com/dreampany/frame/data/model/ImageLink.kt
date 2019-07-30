package com.dreampany.frame.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-07-24
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Link.REF, Constants.Link.URL],
        unique = true
    )],
    primaryKeys = [Constants.Link.REF, Constants.Link.URL]
)
data class ImageLink(
    var ref: String,
    var url: String,
    var title: String
) : BaseParcelKt() {

    @Ignore
    constructor() : this("", "", "") {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ImageLink
        return Objects.equal(item.ref, ref) && Objects.equal(item.url, url)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(ref, url)
    }
}