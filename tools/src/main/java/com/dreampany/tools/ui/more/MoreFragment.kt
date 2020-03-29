package com.dreampany.tools.ui.dashboard

import android.os.Bundle
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.tools.R
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MoreFragment
@Inject constructor() : BaseFragment() {


    override fun layoutRes(): Int  = R.layout.more_fragment

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }
}