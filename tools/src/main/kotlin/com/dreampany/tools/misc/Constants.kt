package com.dreampany.tools.misc

import android.content.Context
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TextUtil
import com.dreampany.tools.R
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    companion object Screen {
        fun database(name: String): String = Constants.database(name)
        fun database(name: String, type: String): String = Constants.database(name, type)

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

    object Default {
        val NULL = Constants.Default.NULL
        const val BOOLEAN = Constants.Default.BOOLEAN
        const val INT = Constants.Default.INT
        const val LONG = Constants.Default.LONG
        const val FLOAT = Constants.Default.FLOAT
        const val DOUBLE = Constants.Default.DOUBLE
        const val STRING = Constants.Default.STRING
    }

    object Tag {
        const val NOTIFY_SERVICE = Constants.Tag.NOTIFY_SERVICE
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
    }

    object Period {
        val Notify = TimeUnit.MINUTES.toSeconds(1)
    }

    object Delay {
        val Notify = TimeUnit.MINUTES.toSeconds(1)
    }

    object Date {
        const val FORMAT_MONTH_DAY = "dd MMMM"
    }

/*    object Database {
        const val NOTE = "note"
    }*/

    object Demo {
        const val ID = Constants.Key.ID
    }

    object Feature {
        const val ID = Constants.Key.ID
    }

    object Apk {
        const val ID = Constants.Key.ID
    }

    object Barcode {
        const val ID = Constants.Key.ID
    }

    object Note {
        const val ID = Constants.Key.ID
    }

    object RequestCode {
        const val ADD_NOTE = 1
        const val EDIT_NOTE = 2
    }
}