package com.dreampany.frame.misc

import android.content.Context
import com.dreampany.frame.util.AndroidUtil
import com.google.common.base.Splitter
import com.google.common.collect.Iterables

/**
 * Created by Hawladar Roman on 24/2/19.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class Constants {

    object Tag {
        const val MORE = "more"
        const val ABOUT = "about"
        const val SETTINGS = "settings"
        const val LICENSE = "license"
        const val LAUNCH = "launch"
        const val NAVIGATION = "navigation"
        const val TOOLS = "tools"


        const val NOTIFY_SERVICE = "notify_service"
        const val MORE_APPS = "more_apps"
        const val RATE_US = "rate_us"
    }

    companion object {
        fun database(name: String): String {
            return Iterables.getLast(Splitter.on(Sep.DOT).trimResults().split(name)) + Database.POST_FIX
        }

        fun database(name: String, type: String): String {
            return Iterables.getLast(Splitter.on(Sep.DOT).trimResults().split(name)) + type + Database.POST_FIX
        }

        fun lastAppId(context: Context): String = AndroidUtil.getLastApplicationId(context)!!
        fun more(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.MORE
        fun about(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.ABOUT
        fun settings(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.SETTINGS
        fun license(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.LICENSE
        fun launch(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.LAUNCH
        fun navigation(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.NAVIGATION
        fun tools(context: Context): String = lastAppId(context) + Sep.HYPHEN + Tag.TOOLS
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

    object Ad {
        const val BANNER = "banner"
        const val INTERSTITIAL = "interstitial"
        const val REWARDED = "rewarded"
    }

    object Sep {
        const val DOT = "."
        const val COMMA = ","
        const val COMMA_SPACE = ", "
        const val SPACE = " "
        const val HYPHEN = "-"
    }

    object Database {
        const val TYPE_FRAME = "frame"
        const val TYPE_TRANSLATION = "translation"
        const val POST_FIX = Sep.HYPHEN + "db"
    }

    object Key {
        const val ID = "id"
        const val TIME = "time"
        const val TYPE = "type"
        const val SUBTYPE = "subtype"
        const val STATE = "state"
    }

    object Notify {
        const val DEFAULT_ID = 101
        const val DEFAULT_CHANNEL_ID = "default_channel_id"
    }

    object Task {
        const val TASK = "task"
    }

    object Retrofit {
        const val CONNECTION_CLOSE = "Connection:close"
    }
}