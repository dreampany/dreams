package com.dreampany.tools.data.model.crypto

import androidx.room.*
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.tools.data.enums.crypto.Category
import com.dreampany.tools.misc.constants.Constants
import com.google.common.base.Objects
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize

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

    var category: Category = Category.COIN,
    @ColumnInfo(name = Constants.Keys.Coin.ICON)
    private var icon: String? = Constant.Default.NULL,
    var description: String? = Constant.Default.NULL,
    var notice: String? = Constant.Default.NULL,
    var tags: List<String>? = Constant.Default.NULL,
    @Embedded
    var platform: Platform? = Constant.Default.NULL,
    var urls: Map<String, List<String>>?=  Constant.Default.NULL,

    var rank: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Coin.MARKET_PAIRS)
    private var marketPairs: Long = Constant.Default.LONG,

    @ColumnInfo(name = Constants.Keys.Coin.CIRCULATING_SUPPLY)
    private var circulatingSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.TOTAL_SUPPLY)
    private var totalSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.MAX_SUPPLY)
    private var maxSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.MARKET_CAP)
    private var marketCap: Double = Constant.Default.DOUBLE,

    @ColumnInfo(name = Constants.Keys.Coin.LAST_UPDATED)
    private var lastUpdated: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Coin.DATE_ADDED)
    private var dateAdded: Long = Constant.Default.LONG,
) : Base() {

    @Ignore
    constructor() : this(time = currentMillis) {

    }

    constructor(id: String) : this(time = currentMillis, id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Coin
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String = "Coin: $id"

    @PropertyName(Constants.Keys.Coin.ICON)
    fun setIcon(icon: String?) {
        this.icon = icon
    }

    @PropertyName(Constants.Keys.Coin.ICON)
    fun getIcon(): String? = icon

    @PropertyName(Constants.Keys.Coin.MARKET_PAIRS)
    fun setMarketPairs(marketPairs: Long) {
        this.marketPairs = marketPairs
    }

    @PropertyName(Constants.Keys.Coin.MARKET_PAIRS)
    fun getMarketPairs(): Long = marketPairs


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
    fun getTotalSupply(): Double = totalSupply

    @PropertyName(Constants.Keys.Coin.MAX_SUPPLY)
    fun setMaxSupply(maxSupply: Double) {
        this.maxSupply = maxSupply
    }

    @PropertyName(Constants.Keys.Coin.MAX_SUPPLY)
    fun getMaxSupply(): Double = maxSupply

    @PropertyName(Constants.Keys.Coin.MARKET_CAP)
    fun setMarketCap(marketCap: Double) {
        this.marketCap = marketCap
    }

    @PropertyName(Constants.Keys.Coin.MARKET_CAP)
    fun getMarketCap(): Double = marketCap

    @PropertyName(Constants.Keys.Coin.LAST_UPDATED)
    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    @PropertyName(Constants.Keys.Coin.LAST_UPDATED)
    fun getLastUpdated(): Long = lastUpdated

    @PropertyName(Constants.Keys.Coin.DATE_ADDED)
    fun setDateAdded(dateAdded: Long) {
        this.dateAdded = dateAdded
    }

    @PropertyName(Constants.Keys.Coin.DATE_ADDED)
    fun getDateAdded(): Long = dateAdded

/*    fun addQuote(quote: Quote) = this.quote.put(quote.getCurrencyId(), quote)

    @Exclude
    fun getQuotesAsList(): List<Quote> = quote.values.toList()

    fun clearQuote() = quote.clear()

    fun hasQuote(): Boolean = quote.isNotEmpty()

    fun hasQuote(currency: Currency): Boolean = quote.containsKey(currency.id)

    fun hasQuote(currencies: Array<Currency>): Boolean {
        if (quote.isEmpty()) {
            return false
        }
        for (currency in currencies) {
            if (!quote.containsKey(currency.id)) {
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
        if (quote.isEmpty()) return null
        return quote.get(currency.id)
    }

    @Exclude
    fun getLatestQuote(): Quote? {
        var latest: Quote? = null
        quote.forEach { entry ->
            if (latest?.time.value < entry.value.time)
                latest = entry.value
        }
        return latest
    }*/
}