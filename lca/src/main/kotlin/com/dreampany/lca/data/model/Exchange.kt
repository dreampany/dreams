package com.dreampany.lca.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
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
        value = [Constants.Exchange.ID],
        unique = true
    )],
    primaryKeys = [Constants.Exchange.ID]
)
data class Exchange (
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var exchange: String = Constants.Default.STRING,
    var fromSymbol: String = Constants.Default.STRING,
    var toSymbol: String = Constants.Default.STRING,
    var volume24h: Double = Constants.Default.DOUBLE,
    var volume24hTo: Double = Constants.Default.DOUBLE
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {}
    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {}

    override fun toString(): String {
        return "Exchange{" +
                "exchange=" + exchange +
                ", toSymbol='" + toSymbol + '\''.toString() +
                ", fromSymbol='" + fromSymbol + '\''.toString() +
                '}'.toString()
    }
}