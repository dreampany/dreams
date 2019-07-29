package com.dreampany.lca.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.frame.data.model.BaseKt
import com.dreampany.frame.util.TimeUtil
import com.dreampany.lca.data.enums.CoinSource
import com.dreampany.lca.data.enums.Currency
import com.dreampany.lca.misc.Constants
import com.google.common.collect.Maps
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import java.io.Serializable
import java.util.*

/**
 * Created by roman on 2019-07-28
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Coin.ID],
        unique = true
    )],
    primaryKeys = [Constants.Coin.ID]
)
class Coin(
    override var time: Long,
    override var id: String
) : BaseKt() {

    var source: CoinSource? = null
    var name: String? = null
    var symbol: String? = null
    var slug: String? = null
    var rank: Int = 0
    @PropertyName(Constants.Coin.MARKET_PAIRS)
    private var marketPairs: Int = 0
    @PropertyName(Constants.Coin.CIRCULATING_SUPPLY)
    private var circulatingSupply: Double = 0.0
    @PropertyName(Constants.Coin.TOTAL_SUPPLY)
    private var totalSupply: Double = 0.0
    @PropertyName(Constants.Coin.MAX_SUPPLY)
    private var maxSupply: Double = 0.0
    @PropertyName(Constants.Coin.LAST_UPDATED)
    private var lastUpdated: Long = 0
    @PropertyName(Constants.Coin.DATE_ADDED)
    private var dateAdded: Long = 0
    var tags: MutableList<String>? = null
    @Ignore
    @Exclude
    private var quotes: MutableMap<Currency, Quote>? = null

    @Ignore
    constructor() : this("") {

    }

    constructor(id: String) : this(TimeUtil.currentTime(), id) {

    }

    @Ignore
    private constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readString()!!) {
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
    }

    companion object CREATOR : Parcelable.Creator<Coin> {
        override fun createFromParcel(parcel: Parcel): Coin {
            return Coin(parcel)
        }

        override fun newArray(size: Int): Array<Coin?> {
            return arrayOfNulls(size)
        }
    }

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
