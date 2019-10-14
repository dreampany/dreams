package com.dreampany.tools.api.radio

import androidx.room.Ignore
import com.dreampany.framework.data.model.Base
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.misc.Constants
import com.google.common.base.Objects
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class ShoutCast(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var metadataOffset: Int = Constants.Default.INT,
    var bitrate: Int = Constants.Default.INT,

    var audioInfo: String? = Constants.Default.NULL,  // e.g.: ice-audio-info: ice-samplerate=44100;ice-bitrate=128;ice-channels=2
    var audioDesc: String? = Constants.Default.NULL,
    var audioGenre: String? = Constants.Default.NULL, // e.g.: icy-genre:Pop / Rock
    var audioName: String? = Constants.Default.NULL,
    var audioHomePage: String? = Constants.Default.NULL,

    var serverName: String? = Constants.Default.NULL,  // e.g.: Server: Icecast 2.3.2
    var publicServer: Boolean = Constants.Default.BOOLEAN,

    var channels: Int = Constants.Default.INT,
    var sampleRate: Int = Constants.Default.INT

) : Base() {

    @Ignore
    constructor() : this(time = TimeUtilKt.currentMillis()) {

    }

    constructor(id: String) : this(time = TimeUtilKt.currentMillis(), id = id) {

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as Station
        return Objects.equal(this.id, item.id)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun toString(): String {
        return "ShoutCast [$id] [$audioInfo] [$audioName]"
    }
}