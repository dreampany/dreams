package com.dreampany.framework.api.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.dreampany.framework.data.source.pref.ConfigPref
import com.dreampany.framework.misc.Constants
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by roman on 2019-10-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ThemeManager @Inject constructor(
    private val context: Context,
    private val pref: ConfigPref
) {

    fun switchMode(activity: Activity) {
        // SWITCH: undefined -> night / night -> day / day - undefined
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                // currently: day mode -> switch to: follow system
                displayDefaultStatusBar(activity) // necessary hack :-/
                activateFollowSystemMode(true)
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                // currently: night mode -> switch to: day mode
                displayLightStatusBar(activity) // necessary hack :-/
                activateDayMode(activity, true)
            }
            else -> {
                // currently: follow system / undefined -> switch to: day mode
                displayLightStatusBar(activity) // necessary hack :-/
                activateNightMode(activity, true)
            }
        }
    }

    fun restoreSavedState(context: Context) {
        val savedNightModeState = loadNightModeState(context)
        val currentNightModeState = AppCompatDelegate.getDefaultNightMode()
        if (savedNightModeState != currentNightModeState) {
            when (savedNightModeState) {
                AppCompatDelegate.MODE_NIGHT_NO ->
                    // turn on day mode
                    activateDayMode(context, false)
                AppCompatDelegate.MODE_NIGHT_YES ->
                    // turn on night mode
                    activateNightMode(context, false)
                else ->
                    // turn on mode "follow system"
                    activateFollowSystemMode(false)
            }
        }
    }

    private fun getCurrentNightModeState(): Int {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    }

    private fun activateNightMode(context: Context, notifyUser: Boolean) {
        saveNightModeState(AppCompatDelegate.MODE_NIGHT_YES)

        // switch to Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // notify user
        if (notifyUser) {
/*            Toast.makeText(
                context,
                context.getText(R.string.toastmessage_theme_night),
                Toast.LENGTH_LONG
            ).show()*/
        }
    }


    private fun activateDayMode(context: Context, notifyUser: Boolean) {
        // save the new state
        saveNightModeState(AppCompatDelegate.MODE_NIGHT_NO)

        // switch to Day Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // notify user
        if (notifyUser) {
/*            Toast.makeText(
                context,
                context.getText(R.string.toastmessage_theme_day),
                Toast.LENGTH_LONG
            ).show()*/
        }
    }

    private fun activateFollowSystemMode(notifyUser: Boolean) {
        // save the new state
        saveNightModeState(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // switch to Undefined Mode / Follow System
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // notify user
        if (notifyUser) {
/*            Toast.makeText(
                context,
                context.getText(R.string.toastmessage_theme_follow_system),
                Toast.LENGTH_LONG
            ).show()*/
        }
    }


    private fun displayDefaultStatusBar(activity: Activity) {
        val decorView = activity.window.decorView
        decorView.systemUiVisibility = 0
    }

    private fun displayLightStatusBar(activity: Activity) {
        val decorView = activity.window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = 0
        }
    }

    private fun saveNightModeState(currentState: Int) {
        pref.getPublicly(Constants.Pref.NIGHT_MODE, currentState)
    }

    private fun loadNightModeState(context: Context): Int {
        return pref.getPublicly(
            Constants.Pref.NIGHT_MODE,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
    }
}