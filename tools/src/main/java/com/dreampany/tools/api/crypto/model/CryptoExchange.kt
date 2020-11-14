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
    @SerializedName(Constants.Keys.Exchange.MARKET) val market: String,
    @SerializedName(Constants.Keys.Exchange.FROM_SYMBOL) val fromSymbol: String,
    @SerializedName(Constants.Keys.Exchange.TO_SYMBOL) val toSymbol: String,
    @SerializedName(Constants.Keys.Exchange.PRICE) val price: Double,
    @SerializedName(Constants.Keys.Exchange.VOLUME_24H) val volume24h: Double,
    @SerializedName(Constants.Keys.Exchange.CHANGE_24H) val change24h: Double,
    @SerializedName(Constants.Keys.Exchange.CHANGE_PCT_24H) val changePct24h: Double
)