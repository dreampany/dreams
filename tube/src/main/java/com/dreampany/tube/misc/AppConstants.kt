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
            const val CATEGORY = "category"
            const val CATEGORIES = "categories"
            const val SEARCH = "search"
            const val VIDEO = "video"
            const val VIDEOS = "videos"
        }

        object Room {
            const val TYPE_TUBE = "tube"
        }

        object Related {
            const val LEFTER = "lefter"
            const val RIGHTER = "righter"
        }
    }

    object Limits {
        const val VIDEOS = 50L
    }

    object Times {
        val CATEGORIES = TimeUnit.DAYS.toMillis(7)
        val VIDEOS = TimeUnit.DAYS.toMillis(1)
        val VIDEO = TimeUnit.HOURS.toMillis(1)
    }

    object Count {
        const val MIN_CATEGORIES = 3
    }
}