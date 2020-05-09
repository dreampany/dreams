package com.dreampany.tools.data.source.history.pref

import android.content.Context
import com.dreampany.framework.data.source.pref.BasePref
import com.dreampany.framework.misc.constant.Constants
import com.dreampany.framework.misc.extension.append
import com.dreampany.framework.misc.util.Util
import com.dreampany.tools.data.enums.history.HistorySource
import com.dreampany.tools.data.enums.history.HistorySubtype
import com.dreampany.tools.data.enums.history.HistoryType
import com.dreampany.tools.misc.constant.HistoryConstants
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
        type: HistoryType,
        subtype: HistorySubtype,
        month: Int,
        day: Int
    ): Long {
        val key = HistoryConstants.Keys.PrefKeys.History.EXPIRE.append(
            source.value, type.value, subtype.value, month.toString(), day.toString()
        )
        return getPrivately(key, Constants.Default.LONG)
    }

    @Synchronized
    fun commitExpireTime(source: HistorySource,
                         type: HistoryType,
                         subtype: HistorySubtype,
                         month: Int,
                         day: Int) {
        val key = HistoryConstants.Keys.PrefKeys.History.EXPIRE.append(
            source.value, type.value, subtype.value, month.toString(), day.toString()
        )
        setPrivately(key, Util.currentMillis())
    }
}