package com.dreampany.framework.util

import com.dreampany.framework.data.enums.Quality

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NetworkUtil {
    companion object {
        fun getConnectionQuality(ping: Int, speed: Int, sessions: Int): Quality {
            return if (speed > 10000000 && ping < 30 && sessions != 0 && sessions < 100) {
                Quality.HIGH
            } else if (speed < 1000000 || ping > 100 || sessions == 0 || sessions > 150) {
                Quality.MEDIUM
            } else {
                Quality.LOW
            }
        }
    }
}