package com.dreampany.tools.ui.dashboard

import android.os.Bundle
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.common.ui.fragment.InjectFragment
import com.dreampany.tools.R
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class DashboardFragment
@Inject constructor() : BaseFragment() {

    override fun layoutId(): Int  = R.layout.dashboard_fragment

    override fun onStartUi(state: Bundle?) {
        Timber.v("")
    }

    override fun onStopUi() {
    }
}