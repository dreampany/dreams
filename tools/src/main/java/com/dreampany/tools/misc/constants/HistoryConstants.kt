package com.dreampany.tools.misc.constants

import java.util.concurrent.TimeUnit

/**
 * Created by roman on 2019-11-11
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HistoryConstants {

    object Keys {

        object PrefKeys {
            const val HISTORY = "history"

            object History {
                const val EXPIRE = "history-expire"
            }
        }

        object Room {
            const val TYPE_HISTORY = "history"
        }
    }

    object Date {
        const val FORMAT_MONTH_DAY = "MMM dd"
        const val DAY = "day"
        const val MONTH = "month"
        const val YEAR = "year"
    }

    object Limits {
        const val HISTORIES = 100L
    }

    object Times {
        val HISTORIES = TimeUnit.DAYS.toMillis(1)
    }
}