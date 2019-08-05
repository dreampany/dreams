package com.dreampany.history.misc

import android.content.Context
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TextUtil
import com.dreampany.history.R
import com.dreampany.history.data.enums.HistoryType
import com.dreampany.history.data.enums.LinkSource
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    companion object {
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

        fun notifyHistory(context: Context): String =
            lastAppId(context).plus(Sep.HYPHEN).plus(Pref.NOTIFY_HISTORY)

        fun notifyHistory(context: Context, type: HistoryType): String =
            lastAppId(context).plus(Sep.HYPHEN).plus(Pref.NOTIFY_HISTORY).plus(type.name)

        fun toUrl(source: LinkSource, relUrl: String): String {
            return toBaseUrl(source).plus(relUrl)
        }

        fun toBaseUrl(source: LinkSource): String {
            when (source) {
                LinkSource.WIKIPEDIA -> {
                    return ImageLink.WIKIPEDIA_BASE_URL
                }
            }
        }
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
        const val INT = Constants.Default.INT
        const val LONG = Constants.Default.LONG
        const val STRING = Constants.Default.STRING
    }

    object Tag {
        const val NOTIFY_SERVICE = Constants.Tag.NOTIFY_SERVICE
        const val LANGUAGE_PICKER = "language_picker"
    }

    object Time {
        val NotifyPeriod = TimeUnit.MINUTES.toSeconds(3)
        val NotifyNextHistory = TimeUnit.MINUTES.toSeconds(10)
        val JSOUP = TimeUnit.SECONDS.toMillis(20)
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

    object ImageLink {
        const val WIKIPEDIA_BASE_URL = "https://en.wikipedia.org"
        const val REF = Constants.Link.REF
        const val URL = Constants.Link.URL
    }

    object Retrofit {
        const val CONNECTION_CLOSE = Constants.Retrofit.CONNECTION_CLOSE
    }

    object Api {
        const val HISTORY_MUFFIN_LABS = "https://history.muffinlabs.com"
        const val HISTORY_MUFFIN_LABS_DAY_MONTH = "/date/{month}/{day}"
    }

    object Pref {
        const val NOTIFY_HISTORY = "notify_history"
    }

    object ImageParser {
        const val PATTERN_IMAGE_TAG = Constants.Parser.PATTERN_IMAGE_TAG
        const val BASE_URL = Constants.Parser.BASE_URL
        const val HREF = Constants.Parser.HREF
        const val SOURCE = Constants.Parser.SOURCE
        const val ALTERNATE = Constants.Parser.ALTERNATE
        const val WIDTH = Constants.Parser.WIDTH
        const val HEIGHT = Constants.Parser.HEIGHT
    }

    object Network {
        const val HTTP = Constants.Network.HTTP
        const val HTTPS = Constants.Network.HTTPS
    }

    object Threshold {
        const val IMAGE_MIN_WIDTH = 100
        const val IMAGE_MIN_HEIGHT = 100
    }

    object UiState {
        enum class State {
            NONE, EMPTY, SEARCH
        }
    }

    object Pattern {
        const val PATTERN_IMAGE = Constants.Pattern.PATTERN_IMAGE
    }

    object Period {
        val Notify = TimeUnit.MINUTES.toSeconds(1)
    }

    object Delay {
        val Notify = TimeUnit.MINUTES.toSeconds(1)
    }
}