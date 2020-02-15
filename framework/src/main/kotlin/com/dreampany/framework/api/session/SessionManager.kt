package com.dreampany.framework.api.session

import com.dreampany.framework.misc.Constants
import com.dreampany.framework.util.TimeUtil
import com.dreampany.framework.util.TimeUtilKt
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-07-28
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class SessionManager
@Inject constructor() {

    var time : Long = 0L

    fun track() {
        time = System.currentTimeMillis()
    }

    fun isExpired() : Boolean {
        return TimeUtilKt.isExpired(time, Constants.Session.EXPIRED_TIME)
    }
}