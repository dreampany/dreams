package com.dreampany.tools.api.crypto.model.cmc

import com.dreampany.tools.misc.constants.CryptoConstants
import com.google.gson.annotations.SerializedName

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoCoin(
    val id: String,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerializedName(value = CryptoConstants.Coin.CIRCULATING_SUPPLY)
    val circulatingSupply: Double,
    @SerializedName(value = CryptoConstants.Coin.MAX_SUPPLY)
    val maxSupply: Double,
    @SerializedName(value = CryptoConstants.Coin.TOTAL_SUPPLY)
    val totalSupply: Double,
    @SerializedName(value = CryptoConstants.Coin.MARKET_PAIRS)
    val marketPairs: Int,
    @SerializedName(value = CryptoConstants.Coin.RANK)
    val rank: Int,
    val tags: ArrayList<String>,
    @SerializedName(value = CryptoConstants.Coin.DATE_ADDED)
    val dateAdded: String,
    @SerializedName(value = CryptoConstants.Coin.LAST_UPDATED)
    val lastUpdated: String,
    @SerializedName(value = CryptoConstants.Coin.QUOTE)
    val quotes: HashMap<CryptoCurrency, CryptoQuote>
)