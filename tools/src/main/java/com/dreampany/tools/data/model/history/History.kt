package com.dreampany.tools.data.model.history

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.data.model.Link
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistoryState
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
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class History(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var source: HistorySource = HistorySource.DEFAULT,
    var state: HistoryState = HistoryState.DEFAULT,
    var day: Int = Constant.Default.INT,
    var month: Int = Constant.Default.INT,
    var year: Int = Constant.Default.INT,
    var text: String? = Constant.Default.NULL,
    var html: String? = Constant.Default.NULL,
    var url: String? = Constant.Default.NULL,
    var links : List<Link>? = Constant.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    constructor(
        id: String,
        source: HistorySource,
        state: HistoryState,
        day: Int,
        month: Int,
        year: Int
    ) : this(
        time = Util.currentMillis(),
        id = id,
        source = source,
        state = state,
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

    override fun toString(): String = "History ($id) == $id"
}