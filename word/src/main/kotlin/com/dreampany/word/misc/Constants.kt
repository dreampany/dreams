package com.dreampany.word.misc

import com.dreampany.frame.misc.Constants
import com.dreampany.word.R
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    object Event  {
        const val ERROR = Constants.Event.ERROR
        const val APPLICATION = Constants.Event.APPLICATION
        const val ACTIVITY = Constants.Event.ACTIVITY
        const val FRAGMENT = Constants.Event.FRAGMENT
        const val NOTIFICATION = Constants.Event.NOTIFICATION
    }

    companion object Screen {
        fun lastAppId(context: android.content.Context): String =
            com.dreampany.frame.misc.Constants.lastAppId(context)

        fun more(context: android.content.Context): String =
            com.dreampany.frame.misc.Constants.more(context)

        fun about(context: android.content.Context): String =
            com.dreampany.frame.misc.Constants.about(context)

        fun settings(context: android.content.Context): String =
            com.dreampany.frame.misc.Constants.settings(context)

        fun license(context: android.content.Context): String =
            com.dreampany.frame.misc.Constants.license(context)

        fun app(context: android.content.Context): String =
            lastAppId(context) + Constants.Sep.HYPHEN + com.dreampany.frame.util.TextUtil.getString(
                context,
                R.string.app_name
            )

        fun navigation(context: android.content.Context): String =
            lastAppId(context) + Constants.Sep.HYPHEN + "navigation"

        fun tools(context: android.content.Context): String =
            lastAppId(context) + Constants.Sep.HYPHEN + "tools"
    }

    object Assets {
        const val WORDS_COMMON = "common.txt"
        const val WORDS_ALPHA = "alpha.txt"
    }

    object Count {
        const val WORD_COMMON = 1000
        const val WORD_ALPHA = 370099
        const val WORD_RECENT = 100
        const val WORD_RECENT_LETTER = 4
    }

    object Limit {
        const val WORD_RESOLVE = 10
        const val WORD_RECENT = 100
        const val WORD_SEARCH = 1000
        const val WORD_SUGGESTION = 2
        const val WORD_OCR = 1000
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
        val WordPeriod = TimeUnit.SECONDS.toMillis(10)
    }

    object KEY {
        const val WORDS = "words"
    }

    object Word {
        const val WORD = "word"
        const val LEFTER = "lefter"
        const val RIGHTER = "righter"
    }
}