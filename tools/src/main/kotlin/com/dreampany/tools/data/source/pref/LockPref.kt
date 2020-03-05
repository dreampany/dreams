package com.dreampany.tools.data.source.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.FramePref
import com.dreampany.tools.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 1/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class LockPref
@Inject constructor(
    context: Context
) : FramePref(context) {

    override fun getPrivateName(context: Context): String {
        return Constants.Pref.NAME.LOCK
    }

    fun commitPasscode() {
        setPrivately(Constants.Pref.Lock.PASSCODE, true)
    }

    fun hasPasscode(): Boolean  {
        return getPrivately(Constants.Pref.Lock.PASSCODE, false)
    }
}