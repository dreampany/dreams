package com.dreampany.lca.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.lca.misc.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by Roman-372 on 8/2/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Graph.ID],
        unique = true
    )],
    primaryKeys = [Constants.Graph.ID]
)
data class Graph(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var slug: String = Constants.Default.STRING,
    var startTime: Long = Constants.Default.LONG,
    var endTime: Long = Constants.Default.LONG,
    var priceBtc: List<List<Float>>? = Constants.Default.NULL,
    var priceUsd: List<List<Float>>? = Constants.Default.NULL,
    var volumeUsd: List<List<Float>>? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {
    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {}
}