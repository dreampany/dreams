package com.dreampany.tools.data.model.crypto

import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 10/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Ticker(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var base: String = Constant.Default.STRING,
    var target: String = Constant.Default.STRING,
    var market: Market = Market.DEFAULT,
    var last: Double = Constant.Default.DOUBLE,
    var volume: Double = Constant.Default.DOUBLE,
    var convertedLast: ConvertedLast = ConvertedLast.DEFAULT,
    var convertedVolume: ConvertedVolume = ConvertedVolume.DEFAULT,
    var timestamp: Long = Constant.Default.LONG,
    var lastTradedAt: Long = Constant.Default.LONG,
    var lastFetchAt: Long = Constant.Default.LONG
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Coin
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Ticker ($id) == $id"
}