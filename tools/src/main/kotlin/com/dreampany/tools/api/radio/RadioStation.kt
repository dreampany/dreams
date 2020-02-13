package com.dreampany.tools.api.radio

import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class RadioStation(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    @SerializedName(value = Constants.Keys.Station.Remote.CHANGE_UUID)
    var changeUuid: String? = Constants.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.STATION_UUID)
    var stationUuid: String? = Constants.Default.NULL,
    var name: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL,
    var homepage: String? = Constants.Default.NULL,
    var favicon: String? = Constants.Default.NULL,
    var ip: String? = Constants.Default.NULL,
    var codec: String? = Constants.Default.NULL,
    var bitrate: Int = Constants.Default.INT,
    var tags: String? = Constants.Default.NULL,
    var country: String? = Constants.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.COUNTRY_CODE)
    var countryCode: String? = Constants.Default.NULL,
    var state: String? = Constants.Default.NULL,
    var language: String? = Constants.Default.NULL,
    var votes: Int = Constants.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.NEGATIVE_VOTES)
    var negativeVotes: Int = Constants.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.CLICK_COUNT)
    var clickCount: Int = Constants.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.CLICK_TREND)
    var clickTrend: Int = Constants.Default.INT,
    var hls: Int = Constants.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_CHECK_OK)
    var lastCheckOk: Int = Constants.Default.INT,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_CHANGE_TIME)
    var lastChangeTime: String? = Constants.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_CHECK_TIME)
    var lastCheckTime: String? = Constants.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.LAST_CHECK_OK_TIME)
    var lastCheckOkTime: String? = Constants.Default.NULL,
    @SerializedName(value = Constants.Keys.Station.Remote.CLICK_TIMESTAMP)
    var clickTimestamp: String? = Constants.Default.NULL
) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as RadioStation
        return Objects.equal(this.id, item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun toString(): String {
        return "RadioStation [$id] [$url] [$countryCode]"
    }
}