package com.dreampany.demo.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import com.dreampany.demo.R
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.moreApps
import com.dreampany.framework.misc.exts.rateUs
import com.dreampany.framework.ui.fragment.InjectFragment
import com.mikepenz.aboutlibraries.LibsBuilder
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