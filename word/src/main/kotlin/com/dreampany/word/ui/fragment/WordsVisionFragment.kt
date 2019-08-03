package com.dreampany.word.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.data.model.Task
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.util.*
import com.dreampany.language.Language
import com.dreampany.vision.ml.CameraSource
import com.dreampany.vision.ml.CameraSourcePreview
import com.dreampany.vision.ml.GraphicOverlay
import com.dreampany.vision.ml.ocr.TextRecognitionProcessor
import com.dreampany.word.R
import com.dreampany.word.data.model.WordRequest
import com.dreampany.word.databinding.FragmentWordsVisionBinding
import com.dreampany.word.ui.model.WordItem
import com.dreampany.word.vm.WordViewModel
import com.google.android.gms.common.annotation.KeepName
import com.klinker.android.link_builder.Link
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
class WordsVisionFragment @Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private lateinit var bind: FragmentWordsVisionBinding
    private var source: CameraSource? = null
    private lateinit var preview: CameraSourcePreview
    private lateinit var overlay: GraphicOverlay
    private lateinit var viewText: AppCompatTextView
    private lateinit var viewCheck: AppCompatCheckBox
    private val texts = StringBuilder()
    private val words = mutableListOf<String>()

    private lateinit var vm: WordViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_words_vision
    }

    override fun getMenuId(): Int {
        return R.menu.menu_words_vision
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        //val checkItem = menu.findItem(R.id.item_auto_collection)
        val clearItem = menu.findItem(R.id.item_clear)
        val doneItem = menu.findItem(R.id.item_done)
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context, R.color.material_white),
            null,
            clearItem, doneItem
        )

/*        viewCheck = checkItem.actionView as AppCompatCheckBox
        viewCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            val text =
                if (isChecked) "All text collection is enabled" else "All text collection is disabled"
            NotifyUtil.shortToast(context, text)
        }*/
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
        processUiState(UiState.HIDE_PROGRESS)
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

    override fun onRefresh() {
        super.onRefresh()
        processUiState(UiState.HIDE_PROGRESS)
    }

    private fun initView() {
        setTitle(TextUtil.getString(context, R.string.detected_words, 0))
        bind = super.binding as FragmentWordsVisionBinding
        preview = bind.preview
        overlay = bind.overlay
        viewText = bind.viewText

        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProviders.of(this, factory).get(WordViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processResponse(it) })
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
/*        if (!viewCheck.isChecked) {
            this.words.clear()
        }*/
        val words = TextUtil.getWords(text)
        for (word in words) {
            val lowerWord = word.toLowerCase()
            if (!vm.isValid(lowerWord)) {
                continue
            }
            if (!this.words.contains(lowerWord)) {
                this.words.add(lowerWord)
                viewText.append(lowerWord + DataUtil.SPACE)
            }
        }
        // val result = DataUtil.joinString(this.words, DataUtil.SPACE)

        setSpan(viewText, this.words)
        setTitle(TextUtil.getString(context, R.string.detected_words, this.words.size))
    }

    private fun setSpan(view: TextView, words: List<String>) {
        if (words.isNullOrEmpty()) {
            return
        }
        TextUtil.setSpan(
            view,
            words,
            R.color.material_white,
            R.color.material_white,
            object : Link.OnClickListener {
                override fun onClick(clickedText: String) {
                    onClickOnText(clickedText)
                }
            },
            object : Link.OnLongClickListener {
                override fun onLongClick(clickedText: String) {
                    onLongClickOnText(clickedText)
                }
            }
        )
    }

    private fun onClickOnText(text: String) {
        Timber.v("Clicked Word %s", text)
        request(text.toLowerCase(), true, true, true)
    }

    private fun onLongClickOnText(text: String) {
        Timber.v("Clicked Word %s", text)
        request(text.toLowerCase(), true, true, true)
    }

    private fun clear() {
        setTitle(TextUtil.getString(context, R.string.detected_words, 0))
        viewText.text = null
        words.clear()
    }

    private fun done() {
        getCurrentTask<Task<*>>(false)!!.comment = texts.toString()
        forResult()
    }

    private fun processUiState(state: UiState) {
        when (state) {
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
        }
    }

    private fun processResponse(response: Response<WordItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<WordItem>
            processSuccess(result.data)
        }
    }

    private fun processProgress(loading: Boolean) {
        if (loading) {
            vm.updateUiState(UiState.SHOW_PROGRESS)
        } else {
            vm.updateUiState(UiState.HIDE_PROGRESS)
        }
    }

    private fun processFailure(error: Throwable) {
        if (error is IOException || error.cause is IOException) {
            vm.updateUiState(UiState.OFFLINE)
        } else if (error is EmptyException) {
            vm.updateUiState(UiState.EMPTY)
        } else if (error is ExtraException) {
            vm.updateUiState(UiState.EXTRA)
        } else if (error is MultiException) {
            for (e in error.errors) {
                processFailure(e)
            }
        }
    }

    private fun processSuccess(item: WordItem) {
        val result = TextUtil.getString(
            context!!,
            R.string.word_vision,
            item.item.id,
            item.item.getPartOfSpeech(),
            item.translation
        )
        val activity = getParent() as Activity?
        if (activity != null && result != null) {
            NotifyUtil.showInfo(activity, result)
        }
    }

    private fun request(word: String, important: Boolean, progress: Boolean, history: Boolean) {
        Timber.v("Request Word %s", word)
        val translate = vm.needToTranslate()
        val language = vm.getCurrentLanguage()

        val request = WordRequest()
        request.inputWord = word
        request.source = Language.ENGLISH.code
        request.target = language.code
        request.translate = translate
        request.important = important
        request.progress = progress
        request.history = history
        vm.load(request)
    }
}