package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-09
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@IgnoreExtraProperties
@Entity(
    indices = [Index(
        value = [Constants.Station.ID],
        unique = true
    )],
    primaryKeys = [Constants.Station.ID]
)
data class Station(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    @ColumnInfo(name = Constants.Station.CHANGE_UUID)
    private var changeUuid: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Station.STATION_UUID)
    private var stationUuid: String? = Constants.Default.NULL,
    var name: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL,
    var homepage: String? = Constants.Default.NULL,
    var favicon: String? = Constants.Default.NULL,
    var ip: String? = Constants.Default.NULL,
    var codec: String? = Constants.Default.NULL,
    var bitrate: Int = Constants.Default.INT,
    var tags: String? = Constants.Default.NULL,
    var country: String? = Constants.Default.NULL,
    var countryCode: String? = Constants.Default.NULL,
    var state: String? = Constants.Default.NULL,
    var language: String? = Constants.Default.NULL,
    var votes: Int = Constants.Default.INT,
    @ColumnInfo(name = Constants.Station.NEGATIVE_VOTES)
    private var negativeVotes: Int = Constants.Default.INT,
    @ColumnInfo(name = Constants.Station.CLICK_COUNT)
    private var clickCount: Int = Constants.Default.INT,
    @ColumnInfo(name = Constants.Station.CLICK_TREND)
    private var clickTrend: Int = Constants.Default.INT,
    var hls: Boolean = Constants.Default.BOOLEAN,
    @ColumnInfo(name = Constants.Station.LAST_CHECK_OK)
    private var lastCheckOk: Boolean = Constants.Default.BOOLEAN,
    @ColumnInfo(name = Constants.Station.LAST_CHANGE_TIME)
    private var lastChangeTime: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Station.LAST_CHECK_TIME)
    private var lastCheckTime: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Station.LAST_CHECK_OK_TIME)
    private var lastCheckOkTime: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Station.CLICK_TIMESTAMP)
    private var clickTimestamp: String? = Constants.Default.NULL


) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Server
        return Objects.equal(this.id, item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }
}
