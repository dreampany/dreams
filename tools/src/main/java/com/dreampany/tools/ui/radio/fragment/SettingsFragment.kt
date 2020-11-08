package com.dreampany.tools.ui.radio.fragment

import android.os.Bundle
import androidx.preference.Preference
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.tools.R
import com.dreampany.tools.ui.radio.activity.PagesActivity
import javax.inject.Inject

/**
 * Created by roman on 8/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class SettingsFragment
@Inject constructor() : InjectFragment() {

    override val prefLayoutRes: Int = R.xml.radio_settings

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == getString(R.string.key_radio_settings_pages)) {
            openPagesUi()
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun initUi() {

    }

    private fun openPagesUi() {
        /*val task = UiTask(
            Type.CATEGORY,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.BACK,
            null as Category?
        )*/
        open(PagesActivity::class)
    }
}