package com.dreampany.crypto.api.model

import com.dreampany.crypto.api.misc.ApiConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoCoin(
    val id: Long,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerializedName(value = ApiConstants.Coin.CIRCULATING_SUPPLY)
    val circulatingSupply: Double,
    @SerializedName(value = ApiConstants.Coin.MAX_SUPPLY)
    val maxSupply: Double,
    @SerializedName(value = ApiConstants.Coin.TOTAL_SUPPLY)
    val totalSupply: Double,
    @SerializedName(value = ApiConstants.Coin.MARKET_PAIRS)
    val marketPairs: Int,
    @SerializedName(value = ApiConstants.Coin.RANK)
    val rank: Int,
    @SerializedName(value = ApiConstants.Coin.QUOTE)
    val quotes: HashMap<CryptoCurrency, CryptoQuote>,
    val tags: ArrayList<String>,
    @SerializedName(value = ApiConstants.Coin.DATE_ADDED)
    val dateAdded: String,
    @SerializedName(value = ApiConstants.Coin.LAST_UPDATED)
    val lastUpdated: String
)