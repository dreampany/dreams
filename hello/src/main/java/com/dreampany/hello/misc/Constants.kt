package com.dreampany.hello.misc

/**
 * Created by roman on 29/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {
    object Keys {
        const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0

        object Pref {
            const val PREF = "pref"
            const val STARTED = "started"
            const val LOGGED = "logged"
            const val SiGN_IN = "sign_in"
            const val USER = "user"
        }

        object Room {
            const val TYPE_USER = "user"
        }
    }

    object Pattern {
        const val YY_MM_DD: String = "yy/MM/dd"
    }

    object Gender {
        const val MALE = "male"
        const val FEMALE = "female"
        const val OTHER = "other"
    }
}