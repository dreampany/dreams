package com.dreampany.tools.ui.news.fragment

import android.os.Bundle
import androidx.preference.Preference
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.ui.fragment.InjectFragment
import javax.inject.Inject
import com.dreampany.tools.R
import com.dreampany.tools.ui.news.activity.PagesActivity

/**
 * Created by roman on 17/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class SettingsFragment
@Inject constructor() : InjectFragment() {

    override val prefLayoutRes: Int = R.xml.news_settings

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == getString(R.string.key_news_settings_pages)) {
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