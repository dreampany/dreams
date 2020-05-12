package com.dreampany.tools.ui.web

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.tools.R

/**
 * Created by roman on 12/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WebActivity: InjectActivity() {

    override fun hasBinding(): Boolean = true

    override fun homeUp(): Boolean = true

    override fun layoutRes(): Int = R.layout.web_activity

    override fun onStartUi(state: Bundle?) {
    }

    override fun onStopUi() {
    }
}