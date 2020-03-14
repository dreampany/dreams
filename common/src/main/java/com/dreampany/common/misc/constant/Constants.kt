package com.dreampany.common.misc.constant

import java.util.*

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constants {
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

    object Count {
        const val THREAD_NETWORK = 5
    }

    object Pref {
        const val DEFAULT = "default"
        const val SERVICE = "service"

        object Service {
            const val STATE = "service_state"
        }
    }

    object Keys {
        const val ID = "id"
    }
}