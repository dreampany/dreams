package com.dreampany.hello.ui.auth

import android.os.Bundle
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.hello.R

/**
 * Created by roman on 24/9/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoginActivity : InjectActivity() {

    override val layoutRes: Int = R.layout.auth_activity
    override val toolbarId: Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }
}