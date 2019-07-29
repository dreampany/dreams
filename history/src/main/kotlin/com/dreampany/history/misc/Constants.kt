package com.dreampany.history.misc

import android.content.Context
import com.dreampany.history.R
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TextUtil
import com.google.common.base.Splitter
import com.google.common.collect.Iterables
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    companion object {
        fun database(name: String): String {
            return Iterables.getLast(Splitter.on(Constants.Sep.DOT).trimResults().split(name)) + Constants.Database.POST_FIX
        }

        fun database(name: String, type: String): String {
            return Iterables.getLast(Splitter.on(Constants.Sep.DOT).trimResults().split(name)) + type + Constants.Database.POST_FIX
        }

        fun lastAppId(context: Context): String = Constants.lastAppId(context)
        fun more(context: Context): String = Constants.more(context)
        fun about(context: Context): String = Constants.about(context)
        fun settings(context: Context): String = Constants.settings(context)
        fun license(context: Context): String = Constants.license(context)

        fun app(context: Context): String =
            lastAppId(context) + Sep.HYPHEN + TextUtil.getString(context, R.string.app_name)

        fun launch(context: Context): String = Constants.launch(context)
        fun navigation(context: Context): String = Constants.navigation(context)
        fun tools(context: Context): String = Constants.tools(context)

        fun notifyHistory(context: Context): String =
            lastAppId(context) + Sep.HYPHEN + "notify_history"
    }

    object Event {
        const val ERROR = Constants.Event.ERROR
        const val APPLICATION = Constants.Event.APPLICATION
        const val ACTIVITY = Constants.Event.ACTIVITY
        const val FRAGMENT = Constants.Event.FRAGMENT
        const val NOTIFICATION = Constants.Event.NOTIFICATION
    }

    object Sep {
        const val DOT = Constants.Sep.DOT
        const val COMMA = Constants.Sep.COMMA
        const val COMMA_SPACE = Constants.Sep.COMMA_SPACE
        const val SPACE = Constants.Sep.SPACE
        const val HYPHEN = Constants.Sep.HYPHEN
    }

    object Tag {
        const val NOTIFY_SERVICE = Constants.Tag.NOTIFY_SERVICE
        const val LANGUAGE_PICKER = "language_picker"
    }

    object Time {
        val NotifyPeriod = TimeUnit.MINUTES.toSeconds(10)
        val NotifyNextHistory = TimeUnit.MINUTES.toSeconds(30)
    }

    object Date {
        const val MONTH_DAY = "MMM dd"
        const val DAY = "day"
        const val MONTH = "month"
        const val YEAR = "year"
    }

    object History {
        const val ID = Constants.Key.ID
        const val EVENTS = "Events"
        const val BIRTHS = "Births"
        const val DEATHS = "Deaths"
        const val DAY = "day"
        const val MONTH = "month"
        const val TYPE = "history_type"
    }

    object Retrofit {
        const val CONNECTION_CLOSE = Constants.Retrofit.CONNECTION_CLOSE
    }

    object Api {
        const val HISTORY_MUFFIN_LABS = "https://history.muffinlabs.com"
        const val HISTORY_MUFFIN_LABS_DAY_MONTH = "/date/{month}/{day}"
    }

    object Database {
        const val TYPE_HISTORY = "history"
        const val POST_FIX = Constants.Sep.HYPHEN + "db"
    }

    object Pref {
        const val NOTIFY_HISTORY = "notify_history"
    }
}