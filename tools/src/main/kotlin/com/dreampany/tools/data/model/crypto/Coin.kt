package com.dreampany.tools.data.model.crypto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.extension.resolve
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.common.collect.Maps
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by roman on 2019-10-01
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Keys.Coin.ID],
        unique = true
    )],
    primaryKeys = [Constants.Keys.Coin.ID]
)
data class Coin(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var name: String? = Constants.Default.NULL,
    var symbol: String? = Constants.Default.NULL,
    var slug: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Coin.CIRCULATING_SUPPLY)
    private var circulatingSupply: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.MAX_SUPPLY)
    private var maxSupply: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.TOTAL_SUPPLY)
    private var totalSupply: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.MARKET_PAIRS)
    private var marketPairs: Int = Constants.Default.INT,
    var rank: Int = Constants.Default.INT,
    @Ignore
    @Exclude
    var quotes: HashMap<Currency, Quote>? = Constants.Default.NULL,
    var tags: ArrayList<String>? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Coin.DATE_ADDED)
    private var dateAdded: Long = Constants.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Coin.LAST_UPDATED)
    private var lastUpdated: Long = Constants.Default.LONG

) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Coin
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String {
        return "Coin ($id) == $id"
    }

    @PropertyName(Constants.Keys.Coin.CIRCULATING_SUPPLY)
    fun setCirculatingSupply(circulatingSupply: Double) {
        this.circulatingSupply = circulatingSupply
    }

    @PropertyName(Constants.Keys.Coin.CIRCULATING_SUPPLY)
    fun getCirculatingSupply(): Double {
        return circulatingSupply
    }

    @PropertyName(Constants.Keys.Coin.TOTAL_SUPPLY)
    fun setTotalSupply(totalSupply: Double) {
        this.totalSupply = totalSupply
    }

    @PropertyName(Constants.Keys.Coin.TOTAL_SUPPLY)
    fun getTotalSupply(): Double {
        return totalSupply
    }

    @PropertyName(Constants.Keys.Coin.MAX_SUPPLY)
    fun setMaxSupply(maxSupply: Double) {
        this.maxSupply = maxSupply
    }

    @PropertyName(Constants.Keys.Coin.MAX_SUPPLY)
    fun getMaxSupply(): Double {
        return maxSupply
    }

    @PropertyName(Constants.Keys.Coin.MARKET_PAIRS)
    fun setMarketPairs(marketPairs: Int) {
        this.marketPairs = marketPairs
    }

    @PropertyName(Constants.Keys.Coin.MARKET_PAIRS)
    fun getMarketPairs(): Int {
        return marketPairs
    }

    @PropertyName(Constants.Keys.Coin.LAST_UPDATED)
    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    @PropertyName(Constants.Keys.Coin.LAST_UPDATED)
    fun getLastUpdated(): Long {
        return lastUpdated
    }

    @PropertyName(Constants.Keys.Coin.DATE_ADDED)
    fun setDateAdded(dateAdded: Long) {
        this.dateAdded = dateAdded
    }

    @PropertyName(Constants.Keys.Coin.DATE_ADDED)
    fun getDateAdded(): Long {
        return dateAdded
    }

    @Exclude
    fun getLastUpdatedDate(): Date {
        return Date(getLastUpdated())
    }

    fun addQuote(quote: Quote) {
        if (quotes == null) {
            quotes = Maps.newHashMap()
        }
        quotes?.put(quote.currency, quote)
    }

    @Exclude
    fun getQuotes(): Map<Currency, Quote>? {
        return quotes
    }

    @Exclude
    fun getQuotesAsList(): List<Quote>? {
        return if (quotes == null) {
            null
        } else ArrayList(quotes!!.values)
    }

    fun clearQuote() {
        quotes?.clear()
    }

    fun hasQuote(): Boolean {
        return !quotes.isNullOrEmpty()
    }

    fun hasQuote(currency: String): Boolean {
        return if (quotes == null) {
            false
        } else quotes!!.containsKey(Currency.valueOf(currency))
    }

    fun hasQuote(currency: Currency): Boolean {
        return if (quotes == null) {
            false
        } else quotes!!.containsKey(currency)
    }

    fun hasQuote(currencies: Array<Currency>): Boolean {
        if (quotes == null) {
            return false
        }
        for (currency in currencies) {
            if (!quotes!!.containsKey(currency)) {
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
        if (quotes.isNullOrEmpty()) return null
        return quotes!!.get(currency)
    }

    @Exclude
    fun getLatestQuote(): Quote? {
        var latest: Quote? = null
        quotes?.forEach {entry->
            if (latest?.time.resolve() < entry.value.time) {
                latest = entry.value
            }
        }
        return latest
    }
}