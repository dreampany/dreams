package com.dreampany.nearby.data.model.media

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.util.Util
import com.dreampany.nearby.data.model.User
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 28/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.ID]
)
data class Document(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING
) : Media() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Document
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Document ($id) == $id"
}