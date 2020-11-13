package com.dreampany.tools.data.enums.crypto

import com.dreampany.framework.misc.exts.previousDay
import com.dreampany.framework.misc.exts.previousMonth
import com.dreampany.framework.misc.exts.previousWeek
import com.dreampany.framework.misc.exts.previousYear

/**
 * Created by roman on 9/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
enum class Times {
    DAY, WEEK, MONTH, THREE_MONTH, SIX_MONTH, YEAR, ALL;

    companion object {
        val Times.startTime: Long
            get() {
                when (this) {
                    DAY ->
                        return previousDay
                    WEEK ->
                        return previousWeek
                    MONTH ->
                        return previousMonth
                    THREE_MONTH ->
                        return 3.previousMonth
                    SIX_MONTH ->
                        return 6.previousMonth
                    YEAR ->
                        return previousYear
                    ALL -> return 0L
                }
            }
    }
}