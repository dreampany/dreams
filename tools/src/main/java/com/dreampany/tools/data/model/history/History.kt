package com.dreampany.tools.data.model.history

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.dreampany.tools.data.model.crypto.Coin
import com.google.common.base.Objects
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 9/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.ID]
)
data class History(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var source: HistorySource = HistorySource.DEFAULT,
    var type: HistoryType = HistoryType.DEFAULT,
    var subtype: HistorySubtype = HistorySubtype.DEFAULT,
    var day: Int = Constants.Default.INT,
    var month: Int = Constants.Default.INT,
    var year: Int = Constants.Default.INT,
    var text: String? = Constants.Default.NULL,
    var html: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    constructor(
        id: String,
        source: HistorySource,
        type: HistoryType,
        subtype: HistorySubtype,
        day: Int,
        month: Int,
        year: Int
    ) : this(
        time = Util.currentMillis(),
        id = id,
        source = source,
        type = type,
        subtype = subtype,
        day = day,
        month = month,
        year = year
    )

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Coin
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String {
        return "History ($id) == $id"
    }
}