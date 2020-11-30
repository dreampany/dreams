package com.dreampany.tools.data.source.history.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.Pref
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.join
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistoryState
import com.dreampany.tools.misc.constants.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Prefs
@Inject constructor(
    context: Context
) : Pref(context) {

    override fun getPrivateName(context: Context): String = Constants.Keys.Pref.History.PREF

    @Synchronized
    fun getExpireTime(
        source: HistorySource,
        state: HistoryState,
        month: Int,
        day: Int
    ): Long {
        val key = join(
            Constants.Keys.Pref.EXPIRE,
            source.value,
            state.value,
            month.toString(),
            day.toString()
        )
        return getPrivately(key, Constant.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(
        source: HistorySource,
        state: HistoryState,
        month: Int,
        day: Int
    ) {
        val key = join(
            Constants.Keys.Pref.EXPIRE,
            source.value,
            state.value,
            month.toString(),
            day.toString()
        )
        setPrivately(key, Util.currentMillis())
    }
}