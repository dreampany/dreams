package com.dreampany.tools.api.radio

import com.dreampany.common.misc.constant.Constants.Sep.LEAF_SEPARATOR


/**
 * Created by roman on 2019-10-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RadioDroidBrowser {
    companion object {
        fun getStationIdOfMediaId(mediaId: String?): String {
            if (mediaId.isNullOrEmpty()) return Constants.Default.STRING
            val separatorIdx = mediaId.indexOf(LEAF_SEPARATOR)

            return if (separatorIdx <= 0) mediaId else mediaId.substring(separatorIdx + 1)
        }
    }
}