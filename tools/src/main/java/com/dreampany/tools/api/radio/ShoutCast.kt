package com.dreampany.tools.api.radio

import androidx.room.Ignore
import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constants
import com.dreampany.common.misc.util.Util
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
    var desc: String? = Constants.Default.NULL,
    var genre: String? = Constants.Default.NULL, // e.g.: icy-genre:Pop / Rock
    var name: String? = Constants.Default.NULL,
    var url: String? = Constants.Default.NULL,

    var server: String? = Constants.Default.NULL,  // e.g.: Server: Icecast 2.3.2
    var public: Boolean = Constants.Default.BOOLEAN,

    var channels: Int = Constants.Default.INT,
    var sampleRate: Int = Constants.Default.INT

) : Base() {

    @Ignore
    constructor() : this(time = Util.currentMillis()) {

    }

    constructor(id: String) : this(time = Util.currentMillis(), id = id) {

    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val item = other as ShoutCast
        return Objects.equal(this.id, item.id)
    }

    override fun toString(): String {
        return "ShoutCast [$id] [$audioInfo] [$name]"
    }
}