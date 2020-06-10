package com.dreampany.crypto.api.model.gecko

import com.dreampany.framework.misc.constant.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 10/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class GeckoTicker(
    val base: String,
    val target: String,
    val market: GeckoMarket,
    val last : Double,
    val volume : Double,
    @SerializedName("converted_last")
    val convertedLast: GeckoConvertedLast,
    @SerializedName("converted_volume")
    val convertedVolume: GeckoConvertedLast,
    val timestamp: Long,
    @SerializedName("last_traded_at")
    val lastTradedAt: Long,
    @SerializedName("last_fetch_at")
    val lastFetchAt: Long
)