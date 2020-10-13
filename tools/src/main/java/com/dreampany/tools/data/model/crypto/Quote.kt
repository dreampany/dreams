package com.dreampany.tools.data.model.crypto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.enums.crypto.Currency
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
        value = [Constant.Keys.ID, Constants.Keys.Quote.CURRENCY],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID, Constants.Keys.Quote.CURRENCY]
)
data class Quote(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var currency: Currency = Currency.USD,
    var price: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.VOLUME_24H)
    private var volume24h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.MARKET_CAP)
    private var marketCap: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.CHANGE_1H)
    private var change1h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.CHANGE_24H)
    private var change24h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.CHANGE_7D)
    private var change7d: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.LAST_UPDATED)
    private var lastUpdated: Long = Constant.Default.LONG
) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id, currency)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Quote
        return Objects.equal(item.id, id) && Objects.equal(item.currency, currency)
    }

    @PropertyName(Constants.Keys.Quote.VOLUME_24H)
    fun setVolume24h(volume24h: Double) {
        this.volume24h = volume24h
    }

    @PropertyName(Constants.Keys.Quote.VOLUME_24H)
    fun getVolume24h(): Double {
        return volume24h
    }

    @PropertyName(Constants.Keys.Quote.MARKET_CAP)
    fun setMarketCap(marketCap: Double) {
        this.marketCap = marketCap
    }

    @PropertyName(Constants.Keys.Quote.MARKET_CAP)
    fun getMarketCap(): Double {
        return marketCap
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_1H)
    fun setChange1h(change1h: Double) {
        this.change1h = change1h
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_1H)
    fun getChange1h(): Double {
        return change1h
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_24H)
    fun setChange24h(change24h: Double) {
        this.change24h = change24h
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_24H)
    fun getChange24h(): Double {
        return change24h
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_7D)
    fun setChange7d(change7d: Double) {
        this.change7d = change7d
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_7D)
    fun getChange7d(): Double {
        return change7d
    }

    @PropertyName(Constants.Keys.Quote.LAST_UPDATED)
    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    @PropertyName(Constants.Keys.Quote.LAST_UPDATED)
    fun getLastUpdated(): Long {
        return lastUpdated
    }
}