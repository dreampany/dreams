package com.dreampany.hi.misc

import java.util.*

/**
 * Created by roman on 5/8/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Constant {

    object Default {
        val NULL = null
        const val BOOLEAN = false
        const val CHARACTER = 0.toChar()
        const val INT = 0
        const val LONG = 0L
        const val FLOAT = 0f
        const val DOUBLE = 0.0
        const val STRING = ""
        val LIST = Collections.emptyList<Any>()
    }

    object Apis {
        const val GOOGLE_CLIENT_ID_DREAMPANY_MAIL =
            "387180098728-3ugp904a274k90p0a0vrb823t1v3ufqi.apps.googleusercontent.com"
    }


    object Keys {
        const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0

        object Pref {
            const val DEFAULT = "default"
            const val MISC = "misc"
            const val PREF = "pref"
            const val EXPIRE = "expire"
            const val STARTED = "started"
            const val LOGGED = "logged"
            const val REGISTERED = "registered"
            const val AUTH = "auth"
            const val USER = "user"
        }
    }

    object Count {
        const val THREAD_NETWORK = 5
    }


}