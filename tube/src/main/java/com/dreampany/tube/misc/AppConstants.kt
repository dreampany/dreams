package com.dreampany.tube.misc

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 29/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppConstants {

    object Keys {
        object Pref {
            const val PREF = "pref"
            const val EXPIRE = "expire"
        }

        object Room {
            const val TYPE_VIDEO = "video"
        }
    }

    object Limits {
        const val VIDEOS = 100L
    }

    object Times {
        val VIDEOS = TimeUnit.MINUTES.toMillis(30)
    }
}