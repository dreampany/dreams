package com.dreampany.tools.ui.fragment.word

import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.data.model.Task
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.misc.extension.resolveText
import com.dreampany.framework.misc.extension.toTint
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.util.*
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.databinding.FragmentWordVisionBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.request.WordRequest
import com.dreampany.tools.ui.model.word.WordItem
import com.dreampany.tools.ui.vm.word.WordViewModel
import com.dreampany.vision.ml.CameraSource
import com.dreampany.vision.ml.CameraSourcePreview
import com.dreampany.vision.ml.GraphicOverlay
import com.dreampany.vision.ml.ocr.TextRecognitionProcessor
import com.google.android.gms.common.annotation.KeepName
import com.klinker.android.link_builder.Link
import com.skydoves.balloon.*
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
class WordVisionFragment
@Inject constructor() : BaseMenuFragment(), OnBalloonClickListener,
    OnBalloonOutsideTouchListener {

    @Inject
    internal lateinit var session: SessionManager
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var pref: Pref
    private lateinit var bind: FragmentWordVisionBinding
    private var source: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var overlay: GraphicOverlay? = null
    private lateinit var viewText: AppCompatTextView
    private lateinit var viewCheck: AppCompatCheckBox
    private val texts = StringBuilder()
    private val words = mutableListOf<String>()

    private lateinit var vm: WordViewModel

    private var balloon: Balloon? = null
    private var clickedView: View? = null
    private var clickedWord: String? = null
    private var updated: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_word_vision
    }

    override fun getMenuId(): Int {
        return R.menu.menu_word_vision
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        findMenuItemById(R.id.item_clear).toTint(context, R.color.material_white)
        //findMenuItemById(R.id.item_done).toTint(context, R.color.material_white)
    }

    override fun getScreen(): String {
        return Constants.wordVision(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        runWithPermissions(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE) {
            createCameraSource()
        }
        session.track()
    }

    override fun onStopUi() {
        source?.release()
    }

    override fun onResume() {
        super.onResume()
        startCameraSource()
        AndroidUtil.initTts(context)
    }

    override fun onPause() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        preview?.stop()
        AndroidUtil.stopTts()
        super.onPause()
    }

    override fun hasBackPressed(): Boolean {
        forResult(updated)
        return true
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

    override fun onBalloonClick(view: View) {
        AndroidUtil.speak(clickedWord)
    }

    override fun onBalloonOutsideTouch(view: View, event: MotionEvent) {
        balloon?.dismiss()
    }

    private fun initUi() {
        setTitle(TextUtil.getString(context, R.string.detected_words, 0))
        bind = super.binding as FragmentWordVisionBinding
        preview = bind.preview
        overlay = bind.overlay
        viewText = bind.viewText

        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProvider(this, factory).get(WordViewModel::class.java)
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
                preview?.start(source, overlay)
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
                    onClickWord(view,clickedText)
                }
            },
            object : Link.OnLongClickListener {
                override fun onLongClick(clickedText: String) {
                    onLongClickWord(view, clickedText)
                }
            }
        )
    }

    private fun showBubble(view: View, text: String) {
        balloon?.run {
            if (isShowing) {
                dismiss()
            }
        }
        context?.run {
            balloon = createBalloon(this) {
                setArrowSize(10)
                setWidthRatio(0.6f)
                setHeight(70)
                setArrowPosition(0.5f)
                setCornerRadius(4f)
                setAlpha(0.9f)
                setTextTypeface(Typeface.BOLD)
                setText(text)
                setTextColorResource(R.color.material_white)
                setTextSize(14.0f)
                // setIconDrawable(ContextCompat.getDrawable(baseContext, R.drawable.ic_profile))
                setBackgroundColorResource(R.color.colorPrimary)
                setOnBalloonClickListener(this@WordVisionFragment)
                setOnBalloonOutsideTouchListener(this@WordVisionFragment)
                setArrowOrientation(ArrowOrientation.BOTTOM)
                setBalloonAnimation(BalloonAnimation.FADE)
                setLifecycleOwner(this@WordVisionFragment)
            }
        }

        balloon?.run {
            view.showAlignTop(this)
        }
    }

    private fun onClickWord(view: View, word: String) {
        clickedView = view
        clickedWord = word
        showBubble(view, word)
        request(id = word, history = true, action = Action.CLICK, single = true, progress = true)
        AndroidUtil.speak(word)
    }

    private fun onLongClickWord(view: View, word: String) {
        clickedView = view
        clickedWord = word
        //searchView.clearFocus()
        request(
            id = word,
            history = true,
            action = Action.LONG_CLICK,
            single = true,
            progress = true
        )
        AndroidUtil.speak(word)
    }

    private fun clear() {
        setTitle(TextUtil.getString(context, R.string.detected_words, 0))
        viewText.text = null
        words.clear()
    }

    private fun done() {
        getCurrentTask<Task<*>>(false)?.extra = texts.toString()
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
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, item: WordItem) {
        Timber.v("Result Single Word[%s]", item.item.id)
        if (action == Action.CLICK) {
            val text = getString(
                R.string.format_word_balloon,
                item.item.id,
                resolveText(item.item.getPartOfSpeech()),
                resolveText(item.translation)
            )
            showBubble(clickedView!!, text)
            clickedView = null
            updated = true
            return
        }
/*        val result = TextUtil.getString(
            context!!,
            R.string.word_vision,
            item.item.id,
            item.item.getPartOfSpeech(),
            item.translation
        )
        val activity = getParent() as Activity?
        if (activity != null && result != null) {
            NotifyUtil.showInfo(activity, result)
        }*/
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        limit: Long = Constants.Default.LONG,
        id: String? = Constants.Default.NULL,
        recent: Boolean = Constants.Default.BOOLEAN,
        history: Boolean = Constants.Default.BOOLEAN,
        suggests: Boolean = Constants.Default.BOOLEAN
    ) {
        val language = pref.getLanguage(Language.ENGLISH)
        val translate = !Language.ENGLISH.equals(language)
        val id = id?.toLowerCase()
        val request = WordRequest(
            type = Type.WORD,
            subtype = Subtype.DEFAULT,
            state = state,
            action = action,
            single = single,
            progress = progress,
            limit = limit,
            id = id,
            sourceLang = Language.ENGLISH.code,
            targetLang = language.code,
            recent = recent,
            history = history,
            translate = translate,
            suggests = suggests
        )
        vm.request(request)
    }
}