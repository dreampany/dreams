package com.dreampany.frame.misc

import android.content.Context
import com.dreampany.frame.util.AndroidUtil

/**
 * Created by Hawladar Roman on 24/2/19.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    companion object Screen {
        fun lastAppId(context: Context): String = AndroidUtil.getLastApplicationId(context)
        fun more(context: Context): String = lastAppId(context) + Sep.HYPHEN + "more"
        fun about(context: Context): String = lastAppId(context) + Sep.HYPHEN + "about"
        fun settings(context: Context): String = lastAppId(context) + Sep.HYPHEN + "settings"
        fun license(context: Context): String = lastAppId(context) + Sep.HYPHEN + "license"
    }

    object Event {
        const val ERROR = "error"
        const val APPLICATION = "application"
        const val ACTIVITY = "activity"
        const val FRAGMENT = "fragment"
        const val NOTIFICATION = "notification"
    }

    object Param {
        const val PACKAGE_NAME = "package_name"
        const val VERSION_CODE = "version_code"
        const val VERSION_NAME = "version_name"
        const val SCREEN = "screen"
        const val ERROR_MESSAGE = "error_message"
        const val ERROR_DETAILS = "error_details"
    }

    object Tag {
        const val NOTIFY_SERVICE = "notify_service"
        const val MORE_APPS = "more_apps"
        const val RATE_US = "rate_us"
    }

    object Sep {
        const val DOT = "."
        const val SPACE = " "
        const val HYPHEN = "-"
    }

    object Key {
        const val ID = "id"
        const val TIME = "time"
        const val TYPE = "type"
        const val SUBTYPE = "subtype"
        const val STATE = "state"
    }

    object LanguageCode {
        const val ARABIC = "ar"
    }

    object LanguageCountry {
        const val ARABIC = "Arabic"
    }
}