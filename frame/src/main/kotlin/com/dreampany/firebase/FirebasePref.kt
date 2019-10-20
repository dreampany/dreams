package com.dreampany.firebase

import android.content.Context
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.framework.util.TimeUtilKt
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-20
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Singleton
class FirebasePref @Inject constructor(context: Context) : FramePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.FIREBASE
    }

    fun commitExceptionTime() {
        setPrivately(Constants.Pref.EXCEPTION_TIME, TimeUtilKt.currentMillis())
    }

    fun getExceptionTime(): Long {
        return getPrivately(Constants.Pref.EXCEPTION_TIME, 0L)
    }
}