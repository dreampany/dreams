package com.dreampany.framework.misc.constant

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import androidx.fragment.app.Fragment
import com.dreampany.framework.misc.exts.applicationId
import com.dreampany.framework.misc.exts.lastApplicationId
import com.dreampany.framework.misc.exts.lastPart
import java.util.*

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class Constant {
    companion object {
        fun database(context: Context?): String {
            return lastAppId(context).plus(Sep.DOT).plus(Room.POST_FIX_DB)
        }

        fun database(context: Context?, type: String): String {
            return lastAppId(context).lastPart(Sep.DOT).plus(Sep.DOT).plus(type).plus(Sep.DOT)
                .plus(Room.POST_FIX_DB)
        }

        fun database(name: String): String {
            return name.lastPart(Sep.DOT).plus(Sep.DOT).plus(Room.POST_FIX_DB)
        }

        fun database(name: String, type: String): String {
            return name.lastPart(Sep.DOT).plus(Sep.DOT).plus(type).plus(Sep.DOT)
                .plus(Room.POST_FIX_DB)
        }

        fun appId(context: Context?): String =
            context.applicationId ?: Default.STRING

        fun lastAppId(context: Context?): String =
            context.lastApplicationId ?: Default.STRING

        fun more(context: Context?): String = lastAppId(context) + Sep.HYPHEN + Tag.MORE
        fun about(context: Context?): String = lastAppId(context) + Sep.HYPHEN + Tag.ABOUT
        fun settings(context: Context?): String = lastAppId(context) + Sep.HYPHEN + Tag.SETTINGS
        fun license(context: Context?): String = lastAppId(context) + Sep.HYPHEN + Tag.LICENSE
        fun launch(context: Context?): String = lastAppId(context) + Sep.HYPHEN + Tag.LAUNCH
        fun navigation(context: Context?): String = lastAppId(context) + Sep.HYPHEN + Tag.NAVIGATION
        fun tools(context: Context?): String = lastAppId(context) + Sep.HYPHEN + Tag.TOOLS
        fun web(context: Context?): String = lastAppId(context) + Sep.HYPHEN + Tag.WEB
    }

    object Event {
        const val EMPTY = "empty"
        const val ERROR = "error"
        const val APPLICATION = "application"
        const val ACTIVITY = "activity"
        const val FRAGMENT = "fragment"
        const val SERVICE = "service"
        const val NOTIFICATION = "notification"

        fun key(instance: Any?) : String {
            if (instance is Application) return APPLICATION
            if (instance is Activity) return ACTIVITY
            if (instance is Fragment) return FRAGMENT
            if (instance is Service) return SERVICE
            return EMPTY
        }
        /*fun application(instance: Application?): String = APPLICATION
        fun activity(instance: Activity?): String = ACTIVITY
        fun fragment(instance: Fragment?): String = FRAGMENT
        fun service(instance: Service?): String = SERVICE*/
    }

    object Param {
        const val PACKAGE_NAME = "package_name"
        const val VERSION_CODE = "version_code"
        const val VERSION_NAME = "version_name"
        const val SCREEN = "screen"
        const val ERROR_MESSAGE = "error_message"
        const val ERROR_DETAILS = "error_details"

        fun screen(instance: Application?) : String = lastAppId(instance).plus(Sep.DOT).plus(instance?.javaClass?.simpleName)
        fun screen(instance: Activity?) : String = lastAppId(instance).plus(Sep.DOT).plus(instance?.javaClass?.simpleName)
        fun screen(instance: Fragment?) : String = lastAppId(instance?.context).plus(Sep.DOT).plus(instance?.javaClass?.simpleName)
        fun screen(instance: Service?) : String = lastAppId(instance?.baseContext).plus(Sep.DOT).plus(instance?.javaClass?.simpleName)
    }

    object Tag {
        const val MORE = "more"
        const val ABOUT = "about"
        const val SETTINGS = "settings"
        const val LICENSE = "license"
        const val LAUNCH = "launch"
        const val NAVIGATION = "navigation"
        const val TOOLS = "tools"
        const val WEB = "web"

        const val NOTIFY_SERVICE = "notify_service"
        const val MORE_APPS = "more_apps"
        const val RATE_US = "rate_us"
    }

    object Notify {
        const val DEFAULT_ID = 101
        const val FOREGROUND_ID = 102
        const val GENERAL_ID = 103
        const val DEFAULT_CHANNEL_ID = "default_channel_id"
        const val FOREGROUND_CHANNEL_ID = "foreground_channel_id"
    }

    object Default {
        val NULL = null
        const val BOOLEAN = false
        const val CHARACTER = 0.toChar()
        const val INT = 0
        const val LONG = 0L
        const val FLOAT = 0f
        const val DOUBLE = 0.0
        const val STRING = ""
        val LIST = Collections.emptyList<Any>()
    }

    object Sep {
        const val DOT = '.'
        const val COMMA = ','
        const val COMMA_SPACE = ", "
        const val SPACE = ' '
        const val HYPHEN = '-'
        const val UNDERSCORE = '_'
        const val SEMI_COLON = ';'
        const val EQUAL = '='
        const val SPACE_HYPHEN_SPACE = " - "
        const val LEAF_SEPARATOR = '|'
        const val PLUS = '+'
        const val SLASH = "/"
        const val SHARP = "#"
    }

    object Room {
        const val TYPE_FRAMEWORK = "framework"
        const val TYPE_TRANSLATION = "translation"
        const val TYPE_DEFAULT = "default"
        const val POST_FIX_DB = "db"
    }

    object Count {
        const val THREAD_NETWORK = 5
    }

    object Http {
        const val READ_TIMEOUT = 20L
        const val WRITE_TIMEOUT = 10L
    }

    object Keys {
        const val TASK = "task"
        const val ID = "id"
        const val TYPE = "type"
        const val SUBTYPE = "subtype"
        const val STATE = "state"

        object Pref {
            const val DEFAULT = "default"
            const val SERVICE = "service"
            const val ADS = "ads"
            const val SERVICE_STATE = "service_state"
        }

        object Firestore {
            const val PACKAGES = "packages"
        }

        object Ads {
            const val BANNER = "banner"
            const val INTERSTITIAL = "interstitial"
            const val REWARDED = "rewarded"
            const val HOUSE = "house"
        }
    }

    object Values {
        object Order {
            const val ASCENDING = "asc"
            const val DESCENDING = "desc"
        }
    }

    object Regex {
        const val NAME = "^[a-zA-Zぁ-ゔゞ゛゜ー ]*\$"
        const val PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{10,}$"
        val WHITE_SPACE = "\\s+".toRegex()
    }
}