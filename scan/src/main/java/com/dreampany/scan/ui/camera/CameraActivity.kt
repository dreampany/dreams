package com.dreampany.scan.ui.camera

import android.os.Bundle
import com.dreampany.framework.misc.extension.FLAGS_FULLSCREEN
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.scan.R
import com.dreampany.scan.databinding.CameraActivityBinding

/**
 * Created by roman on 27/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CameraActivity : InjectActivity() {

    private val IMMERSIVE_FLAG_TIMEOUT = 500L

    private lateinit var bind: CameraActivityBinding

    override val layoutRes: Int = R.layout.camera_activity

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    override fun onResume() {
        super.onResume()
        bind.layout.apply {
            postDelayed({ systemUiVisibility = FLAGS_FULLSCREEN }, IMMERSIVE_FLAG_TIMEOUT)
        }
    }

    private fun initUi() {
        bind = getBinding()

    }
}