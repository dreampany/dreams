package com.dreampany.tools.ui.home

import android.os.Bundle
import com.dreampany.common.ui.fragment.BaseFragment
import javax.inject.Inject
import com.dreampany.tools.R

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class HomeFragment
@Inject constructor() : BaseFragment() {

    override fun layoutId(): Int  = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }
}