package com.dreampany.tools.data.source.history.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.exts.join
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistoryState
import com.dreampany.tools.misc.constants.HistoryConstants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class HistoryPref
@Inject constructor(
    context: Context
) : BasePref(context) {

    override fun getPrivateName(context: Context): String {
        return HistoryConstants.Keys.PrefKeys.HISTORY
    }

    @Synchronized
    fun getExpireTime(
        source: HistorySource,
        state: HistoryState,
        month: Int,
        day: Int
    ): Long {
        val key = join(
            HistoryConstants.Keys.PrefKeys.History.EXPIRE,
            source.value,
            state.value,
            month.toString(),
            day.toString()
        )
        return getPrivately(key, Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(
        source: HistorySource,
        state: HistoryState,
        month: Int,
        day: Int
    ) {
        val key = join(
            HistoryConstants.Keys.PrefKeys.History.EXPIRE,
            source.value,
            state.value,
            month.toString(),
            day.toString()
        )
        setPrivately(key, Util.currentMillis())
    }
}