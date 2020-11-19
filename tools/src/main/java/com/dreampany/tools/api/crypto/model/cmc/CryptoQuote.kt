package com.dreampany.tools.api.crypto.model.cmc


import com.dreampany.framework.misc.constant.Constant
import com.dreampany.tools.api.crypto.misc.Constants


import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoQuote(
    var price: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.VOLUME_24H)
    private var volume24h: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.VOLUME_24H_REPORTED)
    private var volume24hReported: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.VOLUME_7D)
    private var volume7d: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.VOLUME_7D_REPORTED)
    private var volume7dReported: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.VOLUME_30D)
    private var volume30d: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.VOLUME_30D_REPORTED)
    private var volume30dReported: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.MARKET_CAP)
    private var marketCap: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.CHANGE_1H)
    private var percentChange1h: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.CHANGE_24H)
    private var percentChange24h: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.CHANGE_7D)
    private var percentChange7d: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.LAST_UPDATED)
    val lastUpdated: String = Constant.Default.STRING
)