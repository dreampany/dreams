package com.dreampany.tools.api.crypto.model

import com.dreampany.tools.api.crypto.misc.Constants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class Coin(
    val id: Long,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerializedName(value = Constants.Coin.CIRCULATING_SUPPLY)
    val circulatingSupply: Double,
    @SerializedName(value = Constants.Coin.MAX_SUPPLY)
    val maxSupply: Double,
    @SerializedName(value = Constants.Coin.TOTAL_SUPPLY)
    val totalSupply: Double,
    @SerializedName(value = Constants.Coin.MARKET_PAIRS)
    val marketPairs: Int,
    @SerializedName(value = Constants.Coin.RANK)
    val rank: Int,
    @SerializedName(value = Constants.Coin.QUOTE)
    val quotes: HashMap<Currency, Quote>,
    val tags: ArrayList<String>,
    @SerializedName(value = Constants.Coin.DATE_ADDED)
    val dateAdded: String,
    @SerializedName(value = Constants.Coin.LAST_UPDATED)
    val lastUpdated: String
) {
}