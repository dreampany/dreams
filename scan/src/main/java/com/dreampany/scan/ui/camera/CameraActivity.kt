package com.dreampany.scan.ui.camera

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.constraintlayout.widget.ConstraintLayout
import com.dreampany.framework.misc.extension.FLAGS_FULLSCREEN
import com.dreampany.framework.misc.extension.createFile
import com.dreampany.framework.misc.extension.mediaDir
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.scan.R
import com.dreampany.scan.databinding.CameraActivityBinding
import com.dreampany.scan.databinding.CameraUiContainerBinding
import com.dreampany.scan.misc.Constants
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

    private lateinit var outputDir: File
    private lateinit var cameraExecutor: ExecutorService

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var imageCapture: ImageCapture? = null


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
            ?.let { bind.layout.removeView(it) }

        val controls = CameraUiContainerBinding.inflate(layoutInflater, bind.layout, true)
        controls.cameraCaptureButton.setOnSafeClickListener { capture() }
    }

    private fun capture() {
        imageCapture?.let { capture ->
            val photoFile =
                outputDir.createFile(Constants.Keys.FILENAME, Constants.Keys.PHOTO_EXTENSION)
            val meta = ImageCapture.Metadata().apply {
                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            }
            val options = ImageCapture.OutputFileOptions.Builder(photoFile)
                .setMetadata(meta)
                .build()
        }
    }
}