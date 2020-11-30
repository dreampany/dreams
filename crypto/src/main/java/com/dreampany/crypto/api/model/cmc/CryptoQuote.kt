package com.dreampany.crypto.api.model.cmc


import com.dreampany.framework.misc.constant.Constant
import com.dreampany.crypto.api.misc.Constants


import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoQuote(
    val price: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.VOLUME_24H)
    val volume24h: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.VOLUME_24H_REPORTED)
    val volume24hReported: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.VOLUME_7D)
    val volume7d: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.VOLUME_7D_REPORTED)
    val volume7dReported: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.VOLUME_30D)
    val volume30d: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.VOLUME_30D_REPORTED)
    val volume30dReported: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.MARKET_CAP)
    val marketCap: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.CHANGE_1H)
    val percentChange1h: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.CHANGE_24H)
    val percentChange24h: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.CHANGE_7D)
    val percentChange7d: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.LAST_UPDATED)
    val lastUpdated: String = Constant.Default.STRING
)