package com.dreampany.crypto.api.model

import com.dreampany.crypto.api.misc.ApiConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 29/2/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoExchange(
    @SerializedName(value = ApiConstants.Exchange.MARKET) val market: String,
    @SerializedName(value = ApiConstants.Exchange.FROM_SYMBOL) val fromSymbol: String,
    @SerializedName(value = ApiConstants.Exchange.TO_SYMBOL) val toSymbol: String,
    @SerializedName(value = ApiConstants.Exchange.PRICE) val price: Double,
    @SerializedName(value = ApiConstants.Exchange.VOLUME_24H) val volume24h: Double,
    @SerializedName(value = ApiConstants.Exchange.CHANGE_24H) val change24h: Double,
    @SerializedName(value = ApiConstants.Exchange.CHANGE_PCT_24H) val changePct24h: Double
)