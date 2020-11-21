package com.dreampany.tools.api.crypto.model.cmc

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 10/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoGraph(
    val slug: String,
    val startTime: Long,
    val endTime: Long,
    @SerializedName(Constants.Keys.CMC.PRICE_BTC)
    val priceBtc: List<List<Float>>?,
    @SerializedName(Constants.Keys.CMC.PRICE_USD)
    val priceUsd: List<List<Float>>?,
    @SerializedName(Constants.Keys.CMC.VOLUME_USD)
    val volumeUsd: List<List<Float>>?
)