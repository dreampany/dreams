package com.dreampany.tools.ui.more

import android.os.Bundle
import com.dreampany.common.inject.annote.ActivityScope
import com.dreampany.common.ui.fragment.InjectFragment
import com.dreampany.tools.R
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class MoreFragment
@Inject constructor() : InjectFragment() {

    override fun hasBinding(): Boolean = true

    override fun layoutRes(): Int  = R.layout.recycler_fragment

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }
}