package com.dreampany.tools.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.mikepenz.aboutlibraries.LibsBuilder
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by roman on 24/7/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class SettingsFragment
@Inject constructor() : InjectFragment() {

    override val prefLayoutRes: Int = R.xml.settings

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, packageName)
            param.put(Constant.Param.VERSION_CODE, versionCode)
            param.put(Constant.Param.VERSION_NAME, versionName)
            param.put(Constant.Param.SCREEN, Constant.Param.screen(this))

            params.put(Constant.Event.key(this), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == getString(R.string.key_settings_more_apps)) {
            moreApps()
            return true
        } else if (preference.key == getString(R.string.key_settings_rate_us)) {
            rateUs()
            return true
        } else if (preference.key == getString(R.string.key_settings_feedback)) {
            rateUs()
            return true
        } else if (preference.key == getString(R.string.key_settings_about)) {
            about()
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun initUi() {

    }

    private fun moreApps() {
        activity.moreApps(getString(R.string.id_google_play))
    }

    private fun rateUs() {
        activity.rateUs()
    }

    private fun about() {
        LibsBuilder().start(requireContext())
    }
}