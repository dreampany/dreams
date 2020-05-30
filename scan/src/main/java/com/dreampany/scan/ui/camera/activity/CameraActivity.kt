package com.dreampany.scan.ui.camera.activity

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.extension.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.activity.InjectActivity
import com.dreampany.scan.R
import com.dreampany.scan.data.enums.Action
import com.dreampany.scan.data.enums.State
import com.dreampany.scan.data.enums.Subtype
import com.dreampany.scan.data.enums.Type
import com.dreampany.scan.data.model.scan.Scan
import com.dreampany.scan.databinding.CameraActivityBinding
import com.dreampany.scan.databinding.CameraUiContainerBinding
import com.dreampany.scan.misc.Constants
import com.dreampany.scan.misc.Constants.Keys.RATIO_16_9_VALUE
import com.dreampany.scan.misc.Constants.Keys.RATIO_4_3_VALUE
import com.dreampany.scan.ui.camera.vm.ScanViewModel
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
    private lateinit var vm: ScanViewModel

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

    private var type: Type = Type.QR

    override val layoutRes: Int = R.layout.camera_activity

    private val hasBackCamera: Boolean
        get() = cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false

    private val hasFrontCamera: Boolean
        get() = cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false

    override fun onStartUi(state: Bundle?) {
        initUi()
        askForPermissions(Permission.CAMERA) { result ->
            if (result.isAllGranted()) {
                startCamera()
            } else {
                onBackPressed()
            }
        }
    }

    override fun onStopUi() {
        cameraExecutor.shutdown()
    }

    override fun onResume() {
        super.onResume()
        bind.layout.apply {
            postDelayed({ systemUiVisibility = FLAGS_FULLSCREEN }, IMMERSIVE_FLAG_TIMEOUT)
        }
    }

    private fun initUi() {
        bind = getBinding()
        vm = createVm(ScanViewModel::class)

        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDir = mediaDir ?: return
    }

    private fun startCamera() {
        bind.preview.post {
            displayId = bind.preview.display.displayId
            updateCameraUi()
            initCamera()
        }
    }

    private fun updateBindCamera(type: Type) {
        if (type == this.type) return
        this.type = type
        bindCamera()
    }

    private fun updateCameraUi() {
        bind.layout.findViewById<ConstraintLayout>(R.id.camera_ui_container)
            ?.let { bind.layout.removeView(it) }

        val controls = CameraUiContainerBinding.inflate(layoutInflater, bind.layout, true)
        controls.cameraCaptureButton.setOnSafeClickListener { capture() }
        controls.qr.setOnSafeClickListener { updateBindCamera(Type.QR)  }
        controls.doc.setOnSafeClickListener { updateBindCamera(Type.DOC) }
        controls.face.setOnSafeClickListener { updateBindCamera(Type.FACE) }

        when (type) {
            Type.QR -> {
                controls.cameraCaptureButton.disable()
            }
            Type.DOC -> {
                controls.cameraCaptureButton.enable()
            }
            Type.FACE -> {
                controls.cameraCaptureButton.disable()
            }
        }
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
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, QrCodeAnalyzer(vm))
            }

        cameraProvider.unbindAll()
        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            preview?.setSurfaceProvider(bind.preview.createSurfaceProvider())
        } catch (error: Throwable) {
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

    private class QrCodeAnalyzer(val vm: ScanViewModel) : ImageAnalysis.Analyzer {
        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(proxy: ImageProxy) {
            val cameraImage = proxy.image ?: return
            val rotationDegrees = proxy.imageInfo.rotationDegrees
            vm.analyzeQr(cameraImage, rotationDegrees)
        }

    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, Scan>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, Scan>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processError(error: SmartError) {
        /*val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )*/
    }

    private fun processResult(result: Scan?) {

    }
}