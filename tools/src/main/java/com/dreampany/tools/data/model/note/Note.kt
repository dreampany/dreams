package com.dreampany.tools.data.model.note

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 3/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Note(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var title: String = Constant.Default.STRING,
    var description: String? = Constant.Default.NULL,
    var tags: List<String>? = Constant.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {}

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {}

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Note
        return Objects.equal(this.id, item.id)
    }
}