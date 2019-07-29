package com.dreampany.history.data.source.pref

import android.content.Context
import com.dreampany.frame.data.source.pref.FramePrefKt
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.util.TimeUtil
import com.dreampany.frame.util.TimeUtilKt
import com.dreampany.history.R
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

    private val KEY_NOTIFY_HISTORY_EVENT: String
    private val KEY_NOTIFY_HISTORY_BIRTH: String
    private val KEY_NOTIFY_HISTORY_DEATH: String

    init {
        KEY_NOTIFY_HISTORY_EVENT = TextUtil.getString(context, R.string.key_notify_history_event)!!
        KEY_NOTIFY_HISTORY_BIRTH = TextUtil.getString(context, R.string.key_notify_history_birth)!!
        KEY_NOTIFY_HISTORY_DEATH = TextUtil.getString(context, R.string.key_notify_history_death)!!
    }

    fun setHistoryType(type: HistoryType) {
        setPublicly(Constants.History.TYPE, type)
    }

    fun getHistoryType(): HistoryType {
        return getPublicly(Constants.History.TYPE, HistoryType::class.java, HistoryType.EVENT)
    }

    fun setDay(day: Int) {
        setPublicly(Constants.Date.DAY, day)
    }

    fun setMonth(month: Int) {
        setPublicly(Constants.Date.MONTH, month)
    }

    fun setYear(year: String) {
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

    fun commitNotifyHistoryTime(type: HistoryType) {
        setPrivately(Constants.Pref.NOTIFY_HISTORY.plus(type.name), TimeUtil.currentTime())
    }

    fun getNotifyHistoryTime(type: HistoryType): Long {
        return getPrivately(Constants.Pref.NOTIFY_HISTORY.plus(type.name), 0L)
    }

    fun hasNotification(): Boolean {
        return hasNotifyHistoryEvent() || hasNotifyHistoryBirth() || hasNotifyHistoryDeath()
    }

    fun hasNotifyHistoryEvent(): Boolean {
        return getPublicly(KEY_NOTIFY_HISTORY_EVENT, true)
    }

    fun hasNotifyHistoryBirth(): Boolean {
        return getPublicly(KEY_NOTIFY_HISTORY_BIRTH, true)
    }

    fun hasNotifyHistoryDeath(): Boolean {
        return getPublicly(KEY_NOTIFY_HISTORY_DEATH, true)
    }
}