package com.dreampany.crypto.api.model

import com.dreampany.crypto.api.misc.ApiConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoTrade(
    @SerializedName(value = ApiConstants.Trade.EXCHANGE) val exchange: String,
    @SerializedName(value = ApiConstants.Trade.FROM_SYMBOL) val fromSymbol: String,
    @SerializedName(value = ApiConstants.Trade.TO_SYMBOL) val toSymbol: String,
    @SerializedName(value = ApiConstants.Trade.VOLUME_24H) val volume24h: Double,
    @SerializedName(value = ApiConstants.Trade.VOLUME_24H_TO) val volume24hTo: Double
)