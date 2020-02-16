package com.dreampany.tools.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-11-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Keys.Word.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Word.ID]
)
data class TextMessage(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    override var to: String = Constants.Default.STRING,
    override var from: String = Constants.Default.STRING,
    var text: String = Constants.Default.STRING

) : Message() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Word
        return Objects.equal(this.id, item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }
}