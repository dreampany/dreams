package com.dreampany.tools.data.model.crypto

import androidx.room.*
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.framework.misc.exts.value
import com.dreampany.tools.data.enums.crypto.Category
import com.dreampany.tools.data.enums.crypto.Currency
import com.dreampany.tools.misc.constants.Constants
import com.google.common.base.Objects
import com.google.common.collect.Maps
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize
import java.util.*

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
    @ColumnInfo(name = Constants.Keys.Coin.ICON)
    private var icon: String? = Constant.Default.NULL,
    var category: Category = Category.COIN,
    var notice: String? = Constant.Default.NULL,
    var description: String? = Constant.Default.NULL,
    var tags: List<String>? = Constant.Default.NULL,
    @Embedded
    var platform: Platform? = Constant.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Coin.CIRCULATING_SUPPLY)
    private var circulatingSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.MAX_SUPPLY)
    private var maxSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.TOTAL_SUPPLY)
    private var totalSupply: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Coin.MARKET_PAIRS)
    private var marketPairs: Int = Constant.Default.INT,
    var rank: Int = Constant.Default.INT,
    @ColumnInfo(name = Constants.Keys.Coin.DATE_ADDED)
    private var dateAdded: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Coin.LAST_UPDATED)
    private var lastUpdated: Long = Constant.Default.LONG,
    @Ignore
    @Exclude
    var quotes: HashMap<Currency, Quote> = Maps.newHashMap()
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

    @PropertyName(Constants.Keys.Coin.MARKET_PAIRS)
    fun setMarketPairs(marketPairs: Int) {
        this.marketPairs = marketPairs
    }

    @PropertyName(Constants.Keys.Coin.MARKET_PAIRS)
    fun getMarketPairs(): Int = marketPairs

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

    @Exclude
    fun getLastUpdatedDate(): Date = Date(getLastUpdated())

    fun addQuote(quote: Quote) = quotes.put(quote.currency, quote)

    @Exclude
    fun getQuotes(): Map<Currency, Quote> = quotes

    @Exclude
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

    @Exclude
    fun getLatestQuote(): Quote? {
        var latest: Quote? = null
        quotes.forEach { entry ->
            if (latest?.time.value < entry.value.time)
                latest = entry.value
        }
        return latest
    }
}