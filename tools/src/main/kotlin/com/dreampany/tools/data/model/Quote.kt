package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.enums.Currency
import com.dreampany.tools.misc.Constants
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
        value = [Constants.Database.Crypto.ID, Constants.Database.Crypto.CURRENCY],
        unique = true
    )],
    primaryKeys = [Constants.Database.Crypto.ID, Constants.Database.Crypto.CURRENCY]
)
data class Quote(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var currency: Currency = Currency.USD,
    var price: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Quote.VOLUME_24H)
    private var volume24h: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Quote.MARKET_CAP)
    private var marketCap: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Quote.CHANGE_1H)
    private var change1h: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Quote.CHANGE_24H)
    private var change24h: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Quote.CHANGE_7D)
    private var change7d: Double = Constants.Default.DOUBLE,
    @ColumnInfo(name = Constants.Quote.LAST_UPDATED)
    private var lastUpdated: Long = Constants.Default.LONG
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Quote
        return Objects.equal(item.id, id) && Objects.equal(item.currency, currency)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, currency)
    }

    @PropertyName(Constants.Database.Crypto.VOLUME_24H)
    fun setVolume24h(volume24h: Double) {
        this.volume24h = volume24h
    }

    @PropertyName(Constants.Database.Crypto.VOLUME_24H)
    fun getVolume24h(): Double {
        return volume24h
    }

    @PropertyName(Constants.Database.Crypto.MARKET_CAP)
    fun setMarketCap(marketCap: Double) {
        this.marketCap = marketCap
    }

    @PropertyName(Constants.Database.Crypto.MARKET_CAP)
    fun getMarketCap(): Double {
        return marketCap
    }

    @PropertyName(Constants.Database.Crypto.CHANGE_1H)
    fun setChange1h(change1h: Double) {
        this.change1h = change1h
    }

    @PropertyName(Constants.Database.Crypto.CHANGE_1H)
    fun getChange1h(): Double {
        return change1h
    }

    @PropertyName(Constants.Database.Crypto.CHANGE_24H)
    fun setChange24h(change24h: Double) {
        this.change24h = change24h
    }

    @PropertyName(Constants.Database.Crypto.CHANGE_24H)
    fun getChange24h(): Double {
        return change24h
    }

    @PropertyName(Constants.Database.Crypto.CHANGE_7D)
    fun setChange7d(change7d: Double) {
        this.change7d = change7d
    }

    @PropertyName(Constants.Database.Crypto.CHANGE_7D)
    fun getChange7d(): Double {
        return change7d
    }

    @PropertyName(Constants.Database.Crypto.LAST_UPDATED)
    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    @PropertyName(Constants.Database.Crypto.LAST_UPDATED)
    fun getLastUpdated(): Long {
        return lastUpdated
    }
}