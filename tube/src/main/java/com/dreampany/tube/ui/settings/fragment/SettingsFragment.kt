package com.dreampany.tube.ui.settings.fragment

import android.os.Bundle
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.ui.fragment.BaseFragment
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.tube.R
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

    private fun initUi() {

    }
}