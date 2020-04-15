package com.dreampany.tools.api.radio

import com.dreampany.common.data.model.Base
import com.dreampany.common.misc.constant.Constants
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Stream(
    override var time: Long = Constants.Default.LONG,
    override var id: String = Constants.Default.STRING,
    var title: String? = Constants.Default.NULL,
    var artist: String? = Constants.Default.NULL,
    var track: String? = Constants.Default.NULL,
    var meta: Map<String, String>? = Constants.Default.NULL
) : Base() {

    fun hasArtistAndTrack(): Boolean {
        return !(artist.isNullOrEmpty() || track.isNullOrEmpty())
    }
}