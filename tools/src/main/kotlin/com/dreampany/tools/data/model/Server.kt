package com.dreampany.tools.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.dreampany.framework.data.enums.Quality
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-06
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
@Entity(
    indices = [Index(
        value = [Constants.Server.ID],
        unique = true
    )],
    primaryKeys = [Constants.Server.ID]
)
data class Server(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING, //ip address
    var host: String? = Constants.Default.NULL,
    var score: Long = Constants.Default.LONG,
    var ping: Int = Constants.Default.INT,
    var speed: Int = Constants.Default.INT,
    @ColumnInfo(name = Constants.Server.COUNTRY_NAME)
    var countryName: String? = Constants.Default.NULL,
    @ColumnInfo(name = Constants.Server.COUNTRY_CODE)
    var countryCode: String? = Constants.Default.NULL,
    var sessions: Int = Constants.Default.INT,
    var uptime: Long = Constants.Default.LONG,
    var users: Long = Constants.Default.LONG,
    var traffic: Long = Constants.Default.LONG,
    @ColumnInfo(name = Constants.Server.LOG_TYPE)
    var logType: String? = Constants.Default.NULL,
    var operator: String? = Constants.Default.NULL,
    var message: String? = Constants.Default.NULL,
    var config: String? = Constants.Default.NULL,

    var quality: Quality? = Constants.Default.NULL,
    var region: String? = Constants.Default.NULL,
    var city: String? = Constants.Default.NULL,
    var latitude: Double = Constants.Default.DOUBLE,
    var longitude: Double = Constants.Default.DOUBLE

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

    override fun toString(): String {
        return "Server $id"
    }
}