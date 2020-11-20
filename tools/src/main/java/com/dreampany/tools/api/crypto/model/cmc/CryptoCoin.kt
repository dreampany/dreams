package com.dreampany.tools.api.crypto.model.cmc

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.tools.api.crypto.misc.Constants
import com.dreampany.tools.data.enums.crypto.Category
import com.dreampany.tools.data.model.crypto.Platform
import com.dreampany.tools.data.model.crypto.Quote
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
data class CryptoCoin(
    val name: String = Constant.Default.STRING,
    val symbol: String = Constant.Default.STRING,
    val slug: String = Constant.Default.STRING,
    val category: Category = Category.COIN,
    @SerializedName(Constants.Keys.CoinMarketCap.ICON)
    val icon: String? = Constant.Default.NULL,
    val description: String? = Constant.Default.NULL,
    val notice: String? = Constant.Default.NULL,
    val tags: List<String>? = Constant.Default.NULL,

    val platform: Platform? = Constant.Default.NULL,
    val urls: Map<String, List<String>>? = Constant.Default.NULL,

    val rank: Int = Constant.Default.INT,
    @SerializedName(Constants.Keys.CoinMarketCap.MARKET_PAIRS)
    val marketPairs: Int = Constant.Default.INT,

    @SerializedName(Constants.Keys.CoinMarketCap.CIRCULATING_SUPPLY)
    val circulatingSupply: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.TOTAL_SUPPLY)
    val totalSupply: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.MAX_SUPPLY)
    val maxSupply: Double = Constant.Default.DOUBLE,
    @SerializedName(Constants.Keys.CoinMarketCap.MARKET_CAP)
    val marketCap: Double = Constant.Default.DOUBLE,

    @SerializedName(Constants.Keys.CoinMarketCap.LAST_UPDATED)
    val lastUpdated: Long = Constant.Default.LONG,
    @SerializedName(Constants.Keys.CoinMarketCap.DATE_ADDED)
    val dateAdded: Long = Constant.Default.LONG,
    val quote: HashMap<String, Quote>
)