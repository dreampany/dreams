package com.dreampany.nearby.misc

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
            const val TYPE_USER = "user"
        }
    }

    object Limits {
        const val USERS = 100L
    }

    object Times {
        val USERS = TimeUnit.MINUTES.toMillis(30)
    }
}