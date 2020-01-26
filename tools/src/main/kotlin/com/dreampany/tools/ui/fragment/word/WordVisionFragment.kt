package com.dreampany.tools.ui.fragment.word

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
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.util.*
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.ui.misc.WordRequest
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.databinding.FragmentWordVisionBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.WordItem
import com.dreampany.tools.ui.vm.word.WordViewModel
import com.dreampany.vision.ml.CameraSource
import com.dreampany.vision.ml.CameraSourcePreview
import com.dreampany.vision.ml.GraphicOverlay
import com.dreampany.vision.ml.ocr.TextRecognitionProcessor
import com.google.android.gms.common.annotation.KeepName
import com.klinker.android.link_builder.Link
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by roman on 2019-08-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@KeepName
@ActivityScope
class WordVisionFragment @Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var pref: Pref
    private lateinit var bind: FragmentWordVisionBinding
    private var source: CameraSource? = null
    private lateinit var preview: CameraSourcePreview
    private lateinit var overlay: GraphicOverlay
    private lateinit var viewText: AppCompatTextView
    private lateinit var viewCheck: AppCompatCheckBox
    private val texts = StringBuilder()
    private val words = mutableListOf<String>()

    private lateinit var vm: WordViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_word_vision
    }

    override fun getMenuId(): Int {
        return R.menu.menu_word_vision
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        val clearItem = menu.findItem(R.id.item_clear)
        val doneItem = menu.findItem(R.id.item_done)
        MenuTint.colorMenuItem(ColorUtil.getColor(context!!, R.color.material_white), null, clearItem, doneItem)
    }

    override fun getScreen(): String {
        return Constants.wordVision(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
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
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
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
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    private fun initUi() {
        setTitle(TextUtil.getString(context, R.string.detected_words, 0))
        bind = super.binding as FragmentWordVisionBinding
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
            this.updateTitle(it)
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
        getCurrentTask<Task<*>>(false)!!.extra = texts.toString()
        forResult()
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        when (response.uiState) {
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
            vm.processProgress(state = result.state, action =  result.action, loading =  result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state =  result.state,  action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<WordItem>
            processSuccess(result.data)
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

    private fun request(
        id: String? = Constants.Default.NULL,
        recent: Boolean = Constants.Default.BOOLEAN,
        history: Boolean = Constants.Default.BOOLEAN,
        suggests: Boolean = Constants.Default.BOOLEAN,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val language = pref.getLanguage(Language.ENGLISH)
        val translate = !Language.ENGLISH.equals(language)
        val id = id?.toLowerCase()
        val request = WordRequest(
            id = id,
            sourceLang = Language.ENGLISH.code,
            targetLang = language.code,
            recent = recent,
            history = history,
            translate = translate,
            suggests = suggests,
            action = action,
            single = single,
            progress = progress
        )
        vm.request(request)
    }
}