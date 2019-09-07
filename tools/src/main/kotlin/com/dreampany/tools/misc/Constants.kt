package com.dreampany.tools.misc

import android.content.Context
import com.dreampany.framework.misc.Constants
import com.dreampany.framework.util.TextUtil
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
        const val CHARACTER = Constants.Default.CHARACTER
        const val INT = Constants.Default.INT
        const val LONG = Constants.Default.LONG
        const val FLOAT = Constants.Default.FLOAT
        const val DOUBLE = Constants.Default.DOUBLE
        const val STRING = Constants.Default.STRING
    }

    object Tag {
        const val NOTIFY_SERVICE = Constants.Tag.NOTIFY_SERVICE
        const val LANGUAGE_PICKER = "language-picker"
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
    }

    object Period {
        val Notify = TimeUnit.SECONDS.toMillis(100)
    }

    object Delay {
        val Notify = TimeUnit.MINUTES.toSeconds(1)
        val WordSyncTimeMS = TimeUnit.SECONDS.toMillis(60)
    }

    object Date {
        const val FORMAT_MONTH_DAY = "dd MMMM"
    }

    object Database {
        const val POINT = "point"
        const val NOTE = "note"
        const val WORD = "word"
    }

    object Pref {
        const val LOAD = "load"
        const val WORD = "word"
        const val WORD_COMMON_LOADED = "word_common_loaded"
        const val WORD_ALPHA_LOADED = "word_alpha_loaded"
        const val WORD_LAST = "word_last"
        const val WORD_LAST_SYNC_TIME = "word_last_sync_time"
    }

    object Assets {
        const val WORDS_COMMON = "common.txt"
        const val WORDS_ALPHA = "alpha.txt"
    }

    object Firebase {
        const val EXTRA = "extra"
        const val WORDS = "words"
        const val TRACK = "track"
    }

    object Count {
        const val WORD_COMMON = 1000
        const val WORD_ALPHA = 370099
        const val WORD_PAGE = 1000
    }

    object Limit {
        const val WORD_RESOLVE = 10
        const val WORD_RECENT = 100
        const val WORD_SEARCH = 1000
        const val WORD_SUGGESTION = 10
        const val WORD_OCR = 1000
    }

    object Demo {
        const val ID = Constants.Key.ID
    }

    object Point {
        const val ID = Constants.Key.ID
        const val TYPE = Constants.Key.TYPE
        const val SUBTYPE = Constants.Key.SUBTYPE
    }

    object Level {
        const val ID = Constants.Key.ID
        const val LEVEL = "level"
    }

    object Quiz {
        const val ID = Constants.Key.ID
        const val TYPE = Constants.Key.TYPE
        const val SUBTYPE = Constants.Key.SUBTYPE

        val OptionCharArray: CharArray = CharArray(4).apply {
            set(0, 'A')
            set(1, 'B')
            set(2, 'C')
            set(3, 'D')
        }
    }

    object Feature {
        const val ID = Constants.Key.ID
    }

    object App {
        const val ID = Constants.Key.ID
    }

    object Barcode {
        const val ID = Constants.Key.ID
    }

    object Note {
        const val ID = Constants.Key.ID
    }

    object Word {
        const val ID = Constants.Key.ID
        const val LEFTER = "lefter"
        const val RIGHTER = "righter"
        const val RECENT_WORD = "recent_word"
    }

    object Contact {
        const val ID = Constants.Key.ID
    }

    object Language {
        const val LANGUAGE = "language"
    }

    object RequestCode {
        const val ADD_NOTE = 1
        const val EDIT_NOTE = 2
        const val SETTINGS = 3
        const val FAVORITE = 4
        const val QUIZ = 5
    }

    object Service {
        const val VPN_ADDRESS = "10.0.0.2"
        const val VPN_ROUTE = "0.0.0.0"
    }

    object Translation {
        const val YANDEX_URL = com.dreampany.translation.misc.Constants.Yandex.URL
    }
}