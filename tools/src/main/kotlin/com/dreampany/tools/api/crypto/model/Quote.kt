package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class Quote(
    val price: Double,
    @SerializedName(value = Constants.Quote.VOLUME_24H)
    val volume24h: Double,
    @SerializedName(value = Constants.Quote.MARKET_CAP)
    val marketCap: Double,
    @SerializedName(value = Constants.Quote.CHANGE_1H)
    val change1h: Double,
    @SerializedName(value = Constants.Quote.CHANGE_24H)
    val change24h: Double,
    @SerializedName(value = Constants.Quote.CHANGE_7D)
    val change7d: Double,
    @SerializedName(value = Constants.Quote.LAST_UPDATED)
    val lastUpdated: String
) {
}