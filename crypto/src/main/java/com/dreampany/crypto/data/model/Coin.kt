package com.dreampany.crypto.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.util.Util
import com.dreampany.crypto.data.enums.Currency
import com.dreampany.crypto.misc.constants.AppConstants
import com.google.common.base.Objects
import com.google.common.collect.Maps
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by roman on 2019-10-01
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
//@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constant.Keys.ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID]
)
data class Coin(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var name: String = Constant.Default.STRING,
    var symbol: String = Constant.Default.STRING,
    var slug: String = Constant.Default.STRING,
    @ColumnInfo(name = AppConstants.Keys.Coin.CIRCULATING_SUPPLY)
    private var circulatingSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = AppConstants.Keys.Coin.MAX_SUPPLY)
    private var maxSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = AppConstants.Keys.Coin.TOTAL_SUPPLY)
    private var totalSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = AppConstants.Keys.Coin.MARKET_PAIRS)
    private var marketPairs: Int = Constant.Default.INT,
    var rank: Int = Constant.Default.INT,
    @Ignore
    //@Exclude
    var quotes: HashMap<Currency, Quote> = Maps.newHashMap(),
    var tags: List<String>? = Constant.Default.NULL,
    @ColumnInfo(name = AppConstants.Keys.Coin.DATE_ADDED)
    private var dateAdded: Long = Constant.Default.LONG,
    @ColumnInfo(name = AppConstants.Keys.Coin.LAST_UPDATED)
    private var lastUpdated: Long = Constant.Default.LONG
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Coin
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Coin ($id) == $id"

    //@PropertyName(AppConstants.Keys.Coin.CIRCULATING_SUPPLY)
    fun setCirculatingSupply(circulatingSupply: Double) {
        this.circulatingSupply = circulatingSupply
    }

    //@PropertyName(AppConstants.Keys.Coin.CIRCULATING_SUPPLY)
    fun getCirculatingSupply(): Double {
        return circulatingSupply
    }

    //@PropertyName(AppConstants.Keys.Coin.TOTAL_SUPPLY)
    fun setTotalSupply(totalSupply: Double) {
        this.totalSupply = totalSupply
    }

    //@PropertyName(AppConstants.Keys.Coin.TOTAL_SUPPLY)
    fun getTotalSupply(): Double {
        return totalSupply
    }

    //@PropertyName(AppConstants.Keys.Coin.MAX_SUPPLY)
    fun setMaxSupply(maxSupply: Double) {
        this.maxSupply = maxSupply
    }

    //@PropertyName(AppConstants.Keys.Coin.MAX_SUPPLY)
    fun getMaxSupply(): Double {
        return maxSupply
    }

    //@PropertyName(AppConstants.Keys.Coin.MARKET_PAIRS)
    fun setMarketPairs(marketPairs: Int) {
        this.marketPairs = marketPairs
    }

    //@PropertyName(AppConstants.Keys.Coin.MARKET_PAIRS)
    fun getMarketPairs(): Int {
        return marketPairs
    }

    //@PropertyName(AppConstants.Keys.Coin.LAST_UPDATED)
    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    //@PropertyName(AppConstants.Keys.Coin.LAST_UPDATED)
    fun getLastUpdated(): Long {
        return lastUpdated
    }

   // @PropertyName(AppConstants.Keys.Coin.DATE_ADDED)
    fun setDateAdded(dateAdded: Long) {
        this.dateAdded = dateAdded
    }

   // @PropertyName(AppConstants.Keys.Coin.DATE_ADDED)
    fun getDateAdded(): Long = dateAdded

    //@Exclude
    fun getLastUpdatedDate(): Date = Date(getLastUpdated())

    fun addQuote(quote: Quote) = quotes.put(quote.currency, quote)

   // @Exclude
    fun getQuotes(): Map<Currency, Quote> = quotes

    //@Exclude
    fun getQuotesAsList(): List<Quote> = quotes.values.toList()

    fun clearQuote() = quotes.clear()

    fun hasQuote(): Boolean = quotes.isNotEmpty()

    fun hasQuote(currency: String): Boolean = quotes.containsKey(Currency.valueOf(currency))

    fun hasQuote(currency: Currency): Boolean = quotes.containsKey(currency)

    fun hasQuote(currencies: Array<Currency>): Boolean {
        if (quotes.isEmpty()) {
            return false
        }
        for (currency in currencies) {
            if (!quotes.containsKey(currency)) {
                return false
            }
        }
        return true
    }

    fun addQuotes(quotes: List<Quote>) {
        for (quote in quotes) {
            addQuote(quote)
        }
    }

    fun getQuote(currency: Currency): Quote? {
        if (quotes.isEmpty()) return null
        return quotes.get(currency)
    }

    //@Exclude
    fun getLatestQuote(): Quote? {
        var latest: Quote? = null
        quotes.forEach { entry ->
            if (latest?.time.value < entry.value.time)
                latest = entry.value
        }
        return latest
    }
}