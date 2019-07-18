package com.dreampany.vision.ui.fragment

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.TextView
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.frame.R
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.databinding.FragmentLiveTextOcrBinding
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.util.NotifyUtil
import com.dreampany.frame.util.TextUtil
import com.dreampany.vision.ml.CameraSource
import com.dreampany.vision.ml.CameraSourcePreview
import com.dreampany.vision.ml.GraphicOverlay
import com.dreampany.vision.ml.ocr.TextRecognitionProcessor
import com.google.android.gms.common.annotation.KeepName
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/18/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@KeepName
@ActivityScope
class LiveTextOcrFragment @Inject constructor() : BaseMenuFragment() {

    private lateinit var bind: FragmentLiveTextOcrBinding
    private var source: CameraSource? = null
    private lateinit var preview: CameraSourcePreview
    private lateinit var overlay: GraphicOverlay
    private lateinit var textView: TextView
    private lateinit var check: CheckBox
    private val texts = StringBuilder()

    override fun getLayoutId(): Int {
        return R.layout.fragment_live_text_ocr
    }

    override fun getMenuId(): Int {
        return R.menu.menu_live_text_ocr
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        val checkItem = menu.findItem(R.id.item_auto_collection)
        check = checkItem.actionView as CheckBox
        check.setOnCheckedChangeListener { buttonView, isChecked ->
            val text =
                if (isChecked) "All text collection is enabled" else "All text collection is disabled"
            NotifyUtil.shortToast(context, text)
        }
    }

    override fun onStartUi(state: Bundle?) {
        initView()

        runWithPermissions(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE) {
            createCameraSource()
        }
    }

    override fun onStopUi() {
        source?.release()
    }

    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    override fun onPause() {
        preview.stop()
        super.onPause()
    }

    override fun hasBackPressed(): Boolean {
        done()
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_clear) {
            clear()
            return true
        }
        if (item.itemId == R.id.item_done) {
            done()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {

        setTitle(TextUtil.getString(context, R.string.detected_words, 0))
        bind = super.binding as FragmentLiveTextOcrBinding
        preview = bind.preview
        overlay = bind.overlay
        textView = bind.text
    }

    private fun createCameraSource() {
        if (source == null) {
            source = CameraSource(getParent(), overlay)
        }
        source?.setMachineLearningFrameProcessor(TextRecognitionProcessor(TextRecognitionProcessor.Callback {
            this.updateTitle(
                it
            )
        }))
    }

    private fun startCameraSource() {
        if (source != null) {
            try {
                if (preview == null) {
                    Timber.d("resume: Preview is null")
                }
                if (overlay == null) {
                    Timber.d("resume: graphOverlay is null")
                }
                preview.start(source, overlay)
            } catch (e: IOException) {
                Timber.e(e, "Unable to start camera source.")
                source?.release()
                source = null
            }

        }
    }

    private fun updateTitle(text: String) {
        if (!check.isChecked) {
            texts.setLength(0)
        }
        texts.append(text)
        val result = texts.toString()
        textView.text = text
        val words = TextUtil.getWordsCount(result)
        setTitle(TextUtil.getString(context, R.string.detected_words, words))
    }

    private fun clear() {
        setTitle(TextUtil.getString(context, R.string.detected_words, 0))
        textView.text = null
        texts.setLength(0)
    }

    private fun done() {
        getCurrentTask<Task<*>>(false)!!.comment = texts.toString()
        forResult()
    }
}