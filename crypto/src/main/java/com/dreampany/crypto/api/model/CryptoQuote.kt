package com.dreampany.crypto.api.model

import com.dreampany.crypto.api.misc.ApiConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoQuote(
    val price: Double,
    @SerializedName(value = ApiConstants.Quote.VOLUME_24H)
    val volume24h: Double,
    @SerializedName(value = ApiConstants.Quote.MARKET_CAP)
    val marketCap: Double,
    @SerializedName(value = ApiConstants.Quote.CHANGE_1H)
    val change1h: Double,
    @SerializedName(value = ApiConstants.Quote.CHANGE_24H)
    val change24h: Double,
    @SerializedName(value = ApiConstants.Quote.CHANGE_7D)
    val change7d: Double,
    @SerializedName(value = ApiConstants.Quote.LAST_UPDATED)
    val lastUpdated: String
)