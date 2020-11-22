package com.dreampany.tools.data.model.crypto

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
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
        value = [Constant.Keys.ID, Constants.Keys.Quote.CURRENCY_ID],
        unique = true
    )],
    primaryKeys = [Constant.Keys.ID, Constants.Keys.Quote.CURRENCY_ID]
)
data class Quote(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Quote.CURRENCY_ID)
    @PropertyName(Constants.Keys.Quote.CURRENCY_ID)
    var currencyId: String = Constant.Default.STRING,
    var price: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.VOLUME_24H)
    private var volume24h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.VOLUME_24H_REPORTED)
    private var volume24hReported: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.VOLUME_7D)
    private var volume7d: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.VOLUME_7D_REPORTED)
    private var volume7dReported: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.VOLUME_30D)
    private var volume30d: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.VOLUME_30D_REPORTED)
    private var volume30dReported: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.MARKET_CAP)
    private var marketCap: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.CHANGE_1H)
    private var percentChange1h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.CHANGE_24H)
    private var percentChange24h: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.CHANGE_7D)
    private var percentChange7d: Double = Constant.Default.DOUBLE,
    @ColumnInfo(name = Constants.Keys.Quote.LAST_UPDATED)
    private var lastUpdated: Long = Constant.Default.LONG
) : Base() {

    @Ignore
    constructor() : this(time = currentMillis) {

    }

    constructor(id: String) : this(time = currentMillis, id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id, currencyId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Quote
        return Objects.equal(item.id, id) && Objects.equal(item.currencyId, currencyId)
    }

    override fun toString(): String = "Quote: $id"

/*    @PropertyName(Constants.Keys.Quote.CURRENCY_ID)
    fun setCurrencyId(currencyId: String) {
        this.currencyId = currencyId
    }

    @PropertyName(Constants.Keys.Quote.CURRENCY_ID)
    fun getCurrencyId(): String = currencyId*/

    @PropertyName(Constants.Keys.Quote.VOLUME_24H)
    fun setVolume24h(volume24h: Double) {
        this.volume24h = volume24h
    }

    @PropertyName(Constants.Keys.Quote.VOLUME_24H)
    fun getVolume24h(): Double = volume24h

    @PropertyName(Constants.Keys.Quote.VOLUME_24H)
    fun setVolume24hReported(volume24hReported: Double) {
        this.volume24hReported = volume24hReported
    }

    @PropertyName(Constants.Keys.Quote.VOLUME_24H_REPORTED)
    fun getVolume24hReported(): Double = volume24hReported

    @PropertyName(Constants.Keys.Quote.VOLUME_7D)
    fun setVolume7d(volume7d: Double) {
        this.volume7d = volume7d
    }

    @PropertyName(Constants.Keys.Quote.VOLUME_7D)
    fun getVolume7d(): Double = volume7d

    @PropertyName(Constants.Keys.Quote.VOLUME_7D_REPORTED)
    fun setVolume7dReported(volume7dReported: Double) {
        this.volume7dReported = volume7dReported
    }

    @PropertyName(Constants.Keys.Quote.VOLUME_7D_REPORTED)
    fun getVolume7dReported(): Double = volume7dReported


    @PropertyName(Constants.Keys.Quote.VOLUME_30D)
    fun setVolume30d(volume30d: Double) {
        this.volume30d = volume30d
    }

    @PropertyName(Constants.Keys.Quote.VOLUME_30D)
    fun getVolume30d(): Double = volume30d

    @PropertyName(Constants.Keys.Quote.VOLUME_30D_REPORTED)
    fun setVolume30dReported(volume30dReported: Double) {
        this.volume30dReported = volume30dReported
    }

    @PropertyName(Constants.Keys.Quote.VOLUME_30D_REPORTED)
    fun getVolume30dReported(): Double = volume30dReported

    @PropertyName(Constants.Keys.Quote.MARKET_CAP)
    fun setMarketCap(marketCap: Double) {
        this.marketCap = marketCap
    }

    @PropertyName(Constants.Keys.Quote.MARKET_CAP)
    fun getMarketCap(): Double = marketCap

    @PropertyName(Constants.Keys.Quote.CHANGE_1H)
    fun setPercentChange1h(percentChange1h: Double) {
        this.percentChange1h = percentChange1h
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_1H)
    fun getPercentChange1h(): Double = percentChange1h

    @PropertyName(Constants.Keys.Quote.CHANGE_24H)
    fun setPercentChange24h(percentChange24h: Double) {
        this.percentChange24h = percentChange24h
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_24H)
    fun getPercentChange24h(): Double = percentChange24h

    @PropertyName(Constants.Keys.Quote.CHANGE_7D)
    fun setPercentChange7d(percentChange7d: Double) {
        this.percentChange7d = percentChange7d
    }

    @PropertyName(Constants.Keys.Quote.CHANGE_7D)
    fun getPercentChange7d(): Double = percentChange7d

    @PropertyName(Constants.Keys.Quote.LAST_UPDATED)
    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    @PropertyName(Constants.Keys.Quote.LAST_UPDATED)
    fun getLastUpdated(): Long = lastUpdated
}