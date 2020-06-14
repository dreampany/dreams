package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.misc.constants.CryptoConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoQuote(
    val price: Double,
    @SerializedName(value = CryptoConstants.Quote.VOLUME_24H)
    val volume24h: Double,
    @SerializedName(value = CryptoConstants.Quote.MARKET_CAP)
    val marketCap: Double,
    @SerializedName(value = CryptoConstants.Quote.CHANGE_1H)
    val change1h: Double,
    @SerializedName(value = CryptoConstants.Quote.CHANGE_24H)
    val change24h: Double,
    @SerializedName(value = CryptoConstants.Quote.CHANGE_7D)
    val change7d: Double,
    @SerializedName(value = CryptoConstants.Quote.LAST_UPDATED)
    val lastUpdated: String
)