package com.dreampany.scan.ui.camera

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.dreampany.framework.misc.extension.FLAGS_FULLSCREEN
import com.dreampany.framework.misc.extension.mediaDir
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.scan.R
import com.dreampany.scan.databinding.CameraActivityBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by roman on 27/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CameraActivity : InjectActivity() {

    private val IMMERSIVE_FLAG_TIMEOUT = 500L

    private lateinit var bind: CameraActivityBinding

    private val displayManager by lazy { getSystemService(Context.DISPLAY_SERVICE) as DisplayManager }

    private lateinit var outputDir : File
    private lateinit var cameraExecutor: ExecutorService

    private var displayId: Int = -1

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

        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDir = mediaDir ?: return

        bind.preview.post {
            displayId = bind.preview.display.displayId
            updateCameraUi()
        }
    }

    private fun updateCameraUi() {
        bind.layout.findViewById<ConstraintLayout>(R.id.camera_ui_container)
    }
}