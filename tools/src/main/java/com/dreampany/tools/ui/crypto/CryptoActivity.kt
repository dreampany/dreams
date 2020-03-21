package com.dreampany.tools.ui.crypto

import android.os.Bundle
import com.dreampany.common.ui.activity.InjectActivity
import com.dreampany.tools.R
import com.dreampany.tools.databinding.CryptoActivityBinding

/**
 * Created by roman on 21/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CryptoActivity : InjectActivity() {

    private lateinit var bind: CryptoActivityBinding

    override fun hasBinding(): Boolean = true

    override fun layoutId(): Int = R.layout.crypto_activity

    override fun toolbarId(): Int = R.id.toolbar

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = getBinding()
    }
}