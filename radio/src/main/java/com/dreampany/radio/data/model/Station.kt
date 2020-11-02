package com.dreampany.radio.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.currentMillis
import com.dreampany.framework.misc.util.Util
import com.dreampany.radio.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 31/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
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
data class Station(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    @ColumnInfo(name = Constants.Keys.Station.CHANGE_UUID)
    private var changeUuid: String? = Constant.Default.NULL,
    var name: String? = Constant.Default.NULL,
    var url: String? = Constant.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Station.URL_RESOLVED)
    private var urlResolved: String? = Constant.Default.NULL,
    var homepage: String? = Constant.Default.NULL,
    var favicon: String? = Constant.Default.NULL,
    var tags: List<String>? = Constant.Default.NULL,
    var country: String? = Constant.Default.NULL,
    @ColumnInfo(name = Constants.Keys.Station.COUNTRY_CODE)
    private var countryCode: String? = Constant.Default.NULL,
    var state: String? = Constant.Default.NULL,
    var languages: List<String>? = Constant.Default.NULL,
    var votes: Int = Constant.Default.INT,
    @ColumnInfo(name = Constants.Keys.Station.LAST_CHANGE_TIME)
    private var lastChangeTime: Long = Constant.Default.LONG,
    var codecs: List<String>? = Constant.Default.NULL,
    var bitrate: Int = Constant.Default.INT,
    var hls: Boolean = Constant.Default.BOOLEAN,
    @ColumnInfo(name = Constants.Keys.Station.LAST_CHECK_OK)
    private var lastCheckOk: Boolean = Constant.Default.BOOLEAN,
    @ColumnInfo(name = Constants.Keys.Station.LAST_CHECK_TIME)
    private var lastCheckTime: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Station.LAST_CHECK_OK_TIME)
    private var lastCheckOkTime: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Station.LAST_LOCAL_CHECK_TIME)
    var lastLocalCheckTime: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Station.CLICK_TIMESTAMP)
    private var clickTimestamp: Long = Constant.Default.LONG,
    @ColumnInfo(name = Constants.Keys.Station.CLICK_COUNT)
    private var clickCount: Int = Constant.Default.INT,
    @ColumnInfo(name = Constants.Keys.Station.CLICK_TREND)
    private var clickTrend: Int = Constant.Default.INT
) : Base() {

/*    @Parcelize
    enum class Order(val value: String) : Parcelable {
        NAME(Constants.Keys.Station.Order.NAME),
        VOTES(Constants.Keys.Station.Order.VOTES),
        CLICK_COUNT(Constants.Keys.Station.Order.CLICK_COUNT)
    }*/

    @Ignore
    constructor() : this(time = currentMillis) {

    }

    constructor(id: String) : this(time = currentMillis, id = id) {

    }

    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Station
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String =  "Station [$name] [$url]"

    @PropertyName(Constants.Keys.Station.CHANGE_UUID)
    fun setChangeUuid(changeUuid: String?) {
        this.changeUuid = changeUuid
    }

    @PropertyName(Constants.Keys.Station.CHANGE_UUID)
    fun getChangeUuid(): String? = changeUuid

    @PropertyName(Constants.Keys.Station.URL_RESOLVED)
    fun setUrlResolved(urlResolved: String?) {
        this.urlResolved = urlResolved
    }

    @PropertyName(Constants.Keys.Station.URL_RESOLVED)
    fun getUrlResolved(): String? = urlResolved

    @PropertyName(Constants.Keys.Station.COUNTRY_CODE)
    fun setCountryCode(countryCode: String?) {
        this.countryCode = countryCode
    }

    @PropertyName(Constants.Keys.Station.COUNTRY_CODE)
    fun getCountryCode(): String? {
        return countryCode
    }

    @PropertyName(Constants.Keys.Station.CLICK_COUNT)
    fun setClickCount(clickCount: Int) {
        this.clickCount = clickCount
    }

    @PropertyName(Constants.Keys.Station.CLICK_COUNT)
    fun getClickCount(): Int = clickCount

    @PropertyName(Constants.Keys.Station.CLICK_TREND)
    fun setClickTrend(clickTrend: Int) {
        this.clickTrend = clickTrend
    }

    @PropertyName(Constants.Keys.Station.CLICK_TREND)
    fun getClickTrend(): Int = clickTrend

    @PropertyName(Constants.Keys.Station.LAST_CHECK_OK)
    fun setLastCheckOk(lastCheckOk: Boolean) {
        this.lastCheckOk = lastCheckOk
    }

    @PropertyName(Constants.Keys.Station.LAST_CHECK_OK)
    fun getLastCheckOk(): Boolean = lastCheckOk

    @PropertyName(Constants.Keys.Station.LAST_CHANGE_TIME)
    fun setLastChangeTime(lastChangeTime: Long) {
        this.lastChangeTime = lastChangeTime
    }

    @PropertyName(Constants.Keys.Station.LAST_CHANGE_TIME)
    fun getLastChangeTime(): Long = lastChangeTime

    @PropertyName(Constants.Keys.Station.LAST_CHECK_TIME)
    fun setLastCheckTime(lastCheckTime: Long) {
        this.lastCheckTime = lastCheckTime
    }

    @PropertyName(Constants.Keys.Station.LAST_CHECK_TIME)
    fun getLastCheckTime(): Long = lastCheckTime

    @PropertyName(Constants.Keys.Station.LAST_CHECK_OK_TIME)
    fun setLastCheckOkTime(lastCheckOkTime: Long) {
        this.lastCheckOkTime = lastCheckOkTime
    }

    @PropertyName(Constants.Keys.Station.LAST_CHECK_OK_TIME)
    fun getLastCheckOkTime(): Long = lastCheckOkTime

    @PropertyName(Constants.Keys.Station.CLICK_TIMESTAMP)
    fun setClickTimestamp(clickTimestamp: Long) {
        this.clickTimestamp = clickTimestamp
    }

    @PropertyName(Constants.Keys.Station.CLICK_TIMESTAMP)
    fun getClickTimestamp(): Long = clickTimestamp
}