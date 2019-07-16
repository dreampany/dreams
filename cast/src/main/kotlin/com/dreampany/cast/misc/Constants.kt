package com.dreampany.cast.misc

import android.content.Context
import com.dreampany.cast.R
import com.dreampany.frame.misc.Constants
import com.dreampany.frame.util.TextUtil
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

        fun app(context: Context): String = lastAppId(context) + Constants.Sep.HYPHEN + TextUtil.getString(context, R.string.app_name)

        fun launch(context: Context): String = Constants.launch(context)
        fun navigation(context: Context): String = Constants.navigation(context)
        fun tools(context: Context): String = Constants.tools(context)
    }

    object Ad {
        const val BANNER = Constants.Ad.BANNER
        const val INTERSTITIAL = Constants.Ad.INTERSTITIAL
        const val REWARDED = Constants.Ad.REWARDED
    }

    object Time {
        val NotifyPeriod = TimeUnit.HOURS.toSeconds(1)
    }

    object Key {
        const val ID = Constants.Key.ID
    }

    object User {
        const val ID = Key.ID
    }

    object Permission {
         //val PERMISSIONS: Array<String> = Arrays.
    }
}