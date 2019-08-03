package com.dreampany.lca.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.Base
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.misc.Constants
import com.google.common.collect.Maps
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by roman on 2019-07-28
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Coin.ID],
        unique = true
    )],
    primaryKeys = [Constants.Coin.ID]
)
data class Coin(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var source: CoinSource? = Constants.Default.NULL,
    var name: String? = Constants.Default.NULL,
    var symbol: String? = Constants.Default.NULL,
    var slug: String? = Constants.Default.NULL,
    var rank: Int = Constants.Default.INT,

    @PropertyName(Constants.Coin.MARKET_PAIRS)
    private var marketPairs: Int = Constants.Default.INT,

    @PropertyName(Constants.Coin.CIRCULATING_SUPPLY)
    private var circulatingSupply: Double = Constants.Default.DOUBLE,

    @PropertyName(Constants.Coin.TOTAL_SUPPLY)
    private var totalSupply: Double = Constants.Default.DOUBLE,

    @PropertyName(Constants.Coin.MAX_SUPPLY)
    private var maxSupply: Double = Constants.Default.DOUBLE,

    @PropertyName(Constants.Coin.LAST_UPDATED)
    private var lastUpdated: Long = Constants.Default.LONG,

    @PropertyName(Constants.Coin.DATE_ADDED)
    private var dateAdded: Long = Constants.Default.LONG,

    var tags: MutableList<String>? = Constants.Default.NULL,

    @Ignore
    @Exclude
    private var quotes: MutableMap<Currency, Quote>? =  Constants.Default.NULL

) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {}

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {}

/*    @Ignore
    private constructor (parcel: Parcel) : this(parcel.readLong(), parcel.readString()!!) {
        source = parcel.readParcelable(CoinSource::class.java.classLoader)
        name = parcel.readString()
        symbol = parcel.readString()
        slug = parcel.readString()
        rank = parcel.readInt()
        marketPairs = parcel.readInt()
        circulatingSupply = parcel.readDouble()
        totalSupply = parcel.readDouble()
        maxSupply = parcel.readDouble()
        lastUpdated = parcel.readLong()
        dateAdded = parcel.readLong()
        tags = parcel.createStringArrayList()
        quotes = parcel.readSerializable() as MutableMap<Currency, Quote>?

    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(time)
        dest.writeString(id)
        dest.writeParcelable(source, flags)
        dest.writeString(name)
        dest.writeString(symbol)
        dest.writeString(slug)
        dest.writeInt(rank)
        dest.writeInt(marketPairs)
        dest.writeDouble(circulatingSupply)
        dest.writeDouble(totalSupply)
        dest.writeDouble(maxSupply)
        dest.writeLong(lastUpdated)
        dest.writeLong(dateAdded)
        dest.writeStringList(tags)
        dest.writeSerializable(quotes as Serializable)
    }*/

/*    companion object CREATOR : Parcelable.Creator<Coin> {
        override fun createFromParcel(parcel: Parcel): Coin {
            return Coin(parcel)
        }

        override fun newArray(size: Int): Array<Coin?> {
            return arrayOfNulls(size)
        }
    }*/

    override fun toString(): String {
        return "Coin ($id) == $id"
    }

    @PropertyName(Constants.Coin.MARKET_PAIRS)
    fun setMarketPairs(marketPairs: Int) {
        this.marketPairs = marketPairs
    }

    @PropertyName(Constants.Coin.MARKET_PAIRS)
    fun getMarketPairs(): Int {
        return marketPairs
    }

    @PropertyName(Constants.Coin.CIRCULATING_SUPPLY)
    fun setCirculatingSupply(circulatingSupply: Double) {
        this.circulatingSupply = circulatingSupply
    }

    @PropertyName(Constants.Coin.CIRCULATING_SUPPLY)
    fun getCirculatingSupply(): Double {
        return circulatingSupply
    }

    @PropertyName(Constants.Coin.TOTAL_SUPPLY)
    fun setTotalSupply(totalSupply: Double) {
        this.totalSupply = totalSupply
    }

    @PropertyName(Constants.Coin.TOTAL_SUPPLY)
    fun getTotalSupply(): Double {
        return totalSupply
    }

    @PropertyName(Constants.Coin.MAX_SUPPLY)
    fun setMaxSupply(maxSupply: Double) {
        this.maxSupply = maxSupply
    }

    @PropertyName(Constants.Coin.MAX_SUPPLY)
    fun getMaxSupply(): Double {
        return maxSupply
    }

    @PropertyName(Constants.Coin.LAST_UPDATED)
    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    @PropertyName(Constants.Coin.LAST_UPDATED)
    fun getLastUpdated(): Long {
        return lastUpdated
    }

    @PropertyName(Constants.Coin.DATE_ADDED)
    fun setDateAdded(dateAdded: Long) {
        this.dateAdded = dateAdded
    }

    @PropertyName(Constants.Coin.DATE_ADDED)
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
        quotes!![quote.currency] = quote
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
        return if (quotes != null) {
            quotes!!.get(currency)
        } else null
    }

    @Exclude
    fun getLatestQuote(): Quote? {
        var latest: Quote? = null
        quotes?.forEach {
            if (latest == null || latest!!.time < it.value.time) {
                latest = it.value
            }
        }
        return latest
    }
}
