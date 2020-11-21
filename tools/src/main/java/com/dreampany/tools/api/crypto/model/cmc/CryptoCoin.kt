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
data class CryptoCoin(
    val id : String = Constant.Default.STRING,
    val name: String = Constant.Default.STRING,
    val symbol: String = Constant.Default.STRING,
    val slug: String = Constant.Default.STRING,

    val category: String? = Constant.Default.NULL,
    @SerializedName(Constants.Keys.CMC.ICON)
    val icon: String? = Constant.Default.NULL,
    val description: String? = Constant.Default.NULL,
    val notice: String? = Constant.Default.NULL,
    val tags: List<String>? = Constant.Default.NULL,
    val platform: CryptoPlatform? = Constant.Default.NULL,
    val urls: Map<String, List<String>>? = Constant.Default.NULL,

    @SerializedName(Constants.Keys.CMC.RANK)
    val rank: Long = Constant.Default.LONG,
    @SerializedName(Constants.Keys.CMC.MARKET_PAIRS)
    val marketPairs: Long = Constant.Default.LONG,

    @SerializedName(Constants.Keys.CMC.CIRCULATING_SUPPLY)
    val circulatingSupply: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.TOTAL_SUPPLY)
    val totalSupply: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.MAX_SUPPLY)
    val maxSupply: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CMC.MARKET_CAP)
    val marketCap: Double = Constant.Default.DOUBLE,

    @SerializedName(Constants.Keys.CMC.LAST_UPDATED)
    val lastUpdated: String = Constant.Default.STRING,
    @SerializedName(Constants.Keys.CMC.DATE_ADDED)
    val dateAdded: String = Constant.Default.STRING,
    val quote: Map<String, CryptoQuote>
)