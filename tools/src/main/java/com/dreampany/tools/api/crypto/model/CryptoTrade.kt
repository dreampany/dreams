package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.CryptoConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoTrade(
    @SerializedName(value = CryptoConstants.Trade.EXCHANGE) val exchange: String,
    @SerializedName(value = CryptoConstants.Trade.FROM_SYMBOL) val fromSymbol: String,
    @SerializedName(value = CryptoConstants.Trade.TO_SYMBOL) val toSymbol: String,
    @SerializedName(value = CryptoConstants.Trade.VOLUME_24H) val volume24h: Double,
    @SerializedName(value = CryptoConstants.Trade.VOLUME_24H_TO) val volume24hTo: Double
)