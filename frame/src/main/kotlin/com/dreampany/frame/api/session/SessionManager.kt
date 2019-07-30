package com.dreampany.frame.api.session

import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TimeUtil
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-28
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class SessionManager @Inject constructor() {
    var time : Long = 0L

    fun track() {
        time = TimeUtil.currentTime()
    }

    fun isExpired() : Boolean {
        return TimeUtil.isExpired(time, Constants.Session.EXPIRED_TIME)
    }
}