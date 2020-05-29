package com.dreampany.scan.ui.camera

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.dreampany.framework.misc.extension.FLAGS_FULLSCREEN
import com.dreampany.framework.misc.extension.createFile
import com.dreampany.framework.misc.extension.mediaDir
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.scan.R
import com.dreampany.scan.databinding.CameraActivityBinding
import com.dreampany.scan.databinding.CameraUiContainerBinding
import com.dreampany.scan.misc.Constants
import com.dreampany.scan.misc.Constants.Keys.RATIO_16_9_VALUE
import com.dreampany.scan.misc.Constants.Keys.RATIO_4_3_VALUE
import timber.log.Timber
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null

    override val layoutRes: Int = R.layout.camera_activity

    private val hasBackCamera: Boolean
        get() = cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false

    private val hasFrontCamera: Boolean
        get() = cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false

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
            initCamera()
        }
    }

    private fun updateCameraUi() {
        bind.layout.findViewById<ConstraintLayout>(R.id.camera_ui_container)
            ?.let { bind.layout.removeView(it) }

        val controls = CameraUiContainerBinding.inflate(layoutInflater, bind.layout, true)
        controls.cameraCaptureButton.setOnSafeClickListener { capture() }
    }

    private fun initCamera() {
        val provider = ProcessCameraProvider.getInstance(this)
        provider.addListener(Runnable {
            cameraProvider = provider.get()
            lensFacing = when {
                hasBackCamera -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }
            bindCamera()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCamera() {
        val metrics = DisplayMetrics().also { bind.preview.display.getRealMetrics(it) }
        Timber.d("Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Timber.d("Preview aspect ratio: $screenAspectRatio")

        val rotation = bind.preview.display.rotation
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer())
            }

        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            preview?.setSurfaceProvider(bind.preview.createSurfaceProvider())
        } catch (error : Throwable) {
            Timber.e(error)
        }
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

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private class BarcodeAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(image: ImageProxy) {
             Timber.v("image")
        }

    }
}