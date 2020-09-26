package com.dreampany.tools.api.radio

import com.dreampany.framework.data.model.Base
import com.dreampany.framework.misc.constant.Constant
import kotlinx.android.parcel.Parcelize

/**
 * Created by roman on 2019-10-14
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Parcelize
data class Stream(
    override var time: Long = Constant.Default.LONG,
    override var id: String = Constant.Default.STRING,
    var title: String? = Constant.Default.NULL,
    var artist: String? = Constant.Default.NULL,
    var track: String? = Constant.Default.NULL,
    var meta: Map<String, String>? = Constant.Default.NULL
) : Base() {

    fun hasArtistAndTrack(): Boolean {
        return !(artist.isNullOrEmpty() || track.isNullOrEmpty())
    }
}