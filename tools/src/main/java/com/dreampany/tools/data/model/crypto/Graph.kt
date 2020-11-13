package com.dreampany.tools.data.model.crypto

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.tools.data.enums.crypto.Currency
import com.github.mikephil.charting.data.LineData
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-01
 * Copyright (c) 2019 bjit. All rights reserved.
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
data class Graph(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var slug: String = Constant.Default.STRING,
    var startTime: Long = Constant.Default.LONG,
    var endTime: Long = Constant.Default.LONG,
    var priceBtc: List<List<Float>>? = Constant.Default.NULL,
    var priceUsd: List<List<Float>>? = Constant.Default.NULL,
    var volumeUsd: List<List<Float>>? = Constant.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = currentMillis) {

    }

    constructor(id: String) : this(time = currentMillis, id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Graph
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Graph: $id"

    var data: LineData? = null
}