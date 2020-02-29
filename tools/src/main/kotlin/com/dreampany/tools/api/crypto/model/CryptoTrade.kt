package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoTrade(
    @SerializedName(value = Constants.Trade.EXCHANGE) val exchange: String,
    @SerializedName(value = Constants.Trade.FROM_SYMBOL) val fromSymbol: String,
    @SerializedName(value = Constants.Trade.TO_SYMBOL) val toSymbol: String,
    @SerializedName(value = Constants.Trade.VOLUME_24H) val volume24h: Double,
    @SerializedName(value = Constants.Trade.VOLUME_24H_TO) val volume24hTo: Double
)