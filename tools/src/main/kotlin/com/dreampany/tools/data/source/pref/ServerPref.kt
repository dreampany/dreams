package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.util.TimeUtilKt
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.misc.Constants
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 8/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ServerPref
@Inject constructor(
    context: Context
) : FramePref(context) {

    override fun getPrivatePrefName(context: Context): String? {
        return Constants.Pref.SERVER
    }

    fun commitServerTime() {
        setPrivately(Constants.Pref.SERVER_TIME, TimeUtilKt.currentMillis())
    }

    fun getServerTime() : Long {
        return getPrivately(Constants.Pref.SERVER_TIME, 0L)
    }
}