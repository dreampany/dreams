package com.dreampany.scan.ui.camera.vm

import android.annotation.SuppressLint
import android.app.Application
import android.media.Image
import androidx.camera.core.ImageProxy
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.ui.vm.BaseViewModel
import com.dreampany.scan.data.enums.Action
import com.dreampany.scan.data.enums.State
import com.dreampany.scan.data.enums.Subtype
import com.dreampany.scan.data.enums.Type
import com.dreampany.scan.data.model.scan.Scan
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 30/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ScanViewModel
@Inject constructor(
    application: Application,
    rm: ResponseMapper
) : BaseViewModel<Type, Subtype, State, Action, Scan, Scan, UiTask<Type, Subtype, State, Action, Scan>>(
    application,
    rm
) {

    @SuppressLint("UnsafeExperimentalUsageError")
    fun analyzeQr(image: Image, degrees : Int) {
        uiScope.launch {
            Timber.v("New Image found")
            val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build()
            val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
            val visionImage = FirebaseVisionImage.fromMediaImage(
                image,
                getRotationConstant(degrees)
            )

            detector.detectInImage(visionImage)
                .addOnSuccessListener { barcodes ->
                    Timber.v(barcodes.toString())
                }
                .addOnFailureListener { error ->
                    Timber.e(error)
                }
        }
    }

    private fun getRotationConstant(rotationDegrees: Int): Int {
        return when (rotationDegrees) {
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_0
        }
    }
}