package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoExchange(
    @SerializedName(value = Constants.Exchange.MARKET) val market: String,
    @SerializedName(value = Constants.Exchange.FROM_SYMBOL) val fromSymbol: String,
    @SerializedName(value = Constants.Exchange.TO_SYMBOL) val toSymbol: String,
    @SerializedName(value = Constants.Exchange.PRICE) val price: Double,
    @SerializedName(value = Constants.Exchange.VOLUME_24H) val volume24h: Double,
    @SerializedName(value = Constants.Exchange.CHANGE_24H) val change24h: Double,
    @SerializedName(value = Constants.Exchange.CHANGE_PCT_24H) val changePct24h: Double
)