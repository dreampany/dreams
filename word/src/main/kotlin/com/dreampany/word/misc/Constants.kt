package com.dreampany.word.misc

import android.content.Context
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TextUtil
import com.dreampany.word.R
import java.util.concurrent.TimeUnit


/**
 * Created by Hawladar Roman on 29/5/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    object Event {
        const val ERROR = Constants.Event.ERROR
        const val APPLICATION = Constants.Event.APPLICATION
        const val ACTIVITY = Constants.Event.ACTIVITY
        const val FRAGMENT = Constants.Event.FRAGMENT
        const val NOTIFICATION = Constants.Event.NOTIFICATION
    }

    companion object Screen {
        fun lastAppId(context: Context): String = Constants.lastAppId(context)
        fun more(context: Context): String = Constants.more(context)
        fun about(context: Context): String = Constants.about(context)
        fun settings(context: Context): String = Constants.settings(context)
        fun license(context: Context): String = Constants.license(context)

        fun app(context: Context): String = lastAppId(context) + Sep.HYPHEN + TextUtil.getString(context, R.string.app_name)

        fun launch(context: Context): String = Constants.launch(context)
        fun navigation(context: Context): String = Constants.navigation(context)
        fun tools(context: Context): String = Constants.tools(context)
    }

    object Assets {
        const val WORDS_COMMON = "common.txt"
        const val WORDS_ALPHA = "alpha.txt"
    }

    object Tag {
        const val NOTIFY_SERVICE = Constants.Tag.NOTIFY_SERVICE
        const val LANGUAGE_PICKER = "language_picker"
    }

    object Count {
        const val WORD_COMMON = 1000
        const val WORD_ALPHA = 370099
        const val WORD_RECENT = 100
        const val WORD_RECENT_LETTER = 4
        const val WORD_PAGE = 100
    }

    object Limit {
        const val WORD_RESOLVE = 10
        const val WORD_RECENT = 100
        const val WORD_SEARCH = 1000
        const val WORD_SUGGESTION = 10
        const val WORD_OCR = 1000
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
        val WordPeriod = TimeUnit.SECONDS.toMillis(10)
    }

    object FirebaseKey {
        const val WORDS = "words"
    }

    object Word {
        const val ID = Constants.Key.ID
        const val WORD = "word"
        const val PART_OF_SPEECH = "part_of_speech"
        const val LAST_SEARCH = "last_search_word"
        const val LOADED = "loaded"
    }

    object Definition {
        const val PART_OF_SPEECH = Word.PART_OF_SPEECH
    }

    object Synonym {
        const val LEFTER = "lefter"
        const val RIGHTER = "righter"
    }

    object Antonym {
        const val LEFTER = Synonym.LEFTER
        const val RIGHTER = Synonym.RIGHTER
    }

    object Language {
        const val LANGUAGE = "language"
    }

    object Period {
        val Notify = TimeUnit.MINUTES.toSeconds(3)
    }

    object Delay {
        val Notify = TimeUnit.MINUTES.toSeconds(1)
    }

    object Sep {
        const val DOT = Constants.Sep.DOT
        const val COMMA = Constants.Sep.COMMA
        const val COMMA_SPACE = Constants.Sep.COMMA_SPACE
        const val SPACE = Constants.Sep.SPACE
        const val HYPHEN = Constants.Sep.HYPHEN
    }
}