package com.dreampany.lockui.ui.activity

import android.os.Bundle
import com.dreampany.common.ui.activity.BaseActivity
import com.dreampany.lockui.R

/**
 * Created by roman on 3/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PinActivity : BaseActivity() {

    val EXTRA_SET_PIN = "set_pin"
    private val PIN_LENGTH = 4

    private var setPin = false

    override fun isFullScreen(): Boolean = true

    override fun hasBinding(): Boolean = true

    override fun getLayoutId(): Int = R.layout.pin_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        setPin = intent.getBooleanExtra(EXTRA_SET_PIN, false)

        if (setPin) {

        } else {

        }
    }
}