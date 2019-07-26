package com.dreampany.history.data.source.pref

import android.content.Context
import com.dreampany.frame.data.source.pref.FramePrefKt
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 7/24/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class Pref @Inject constructor(context: Context) : FramePrefKt(context) {

    fun setHistoryType(type:HistoryType) {
        setPublicly(Constants.History.TYPE, type)
    }

    fun getHistoryType() : HistoryType {
        return getPublicly(Constants.History.TYPE, HistoryType::class.java, HistoryType.EVENT)
    }

    fun setDay(day: Int) {
        setPublicly(Constants.Date.DAY, day)
    }

    fun setMonth(month: Int) {
        setPublicly(Constants.Date.MONTH, month)
    }

    fun setYear(year: Int) {
        setPublicly(Constants.Date.YEAR, year)
    }

    fun getDay(): Int {
        return getPublicly(Constants.Date.DAY, TimeUtilKt.getDay())
    }

    fun getMonth(): Int {
        return getPublicly(Constants.Date.MONTH, TimeUtilKt.getMonth())
    }

    fun getYear(): Int {
        return getPublicly(Constants.Date.YEAR, TimeUtilKt.getYear())
    }
}