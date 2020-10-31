package com.dreampany.radio.api.radiobrowser

import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.constant.Constant.Sep.LEAF_SEPARATOR


/**
 * Created by roman on 2019-10-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RadioDroidBrowser {
    companion object {
        fun getStationIdOfMediaId(mediaId: String?): String {
            if (mediaId.isNullOrEmpty()) return Constant.Default.STRING
            val separatorIdx = mediaId.indexOf(LEAF_SEPARATOR)

            return if (separatorIdx <= 0) mediaId else mediaId.substring(separatorIdx + 1)
        }
    }
}