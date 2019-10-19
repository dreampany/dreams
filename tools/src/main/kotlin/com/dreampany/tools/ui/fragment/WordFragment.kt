package com.dreampany.tools.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.misc.extension.resolveText
import com.dreampany.framework.ui.callback.SearchViewCallback
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.*
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.ui.misc.WordRequest
import com.dreampany.tools.data.model.Definition
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.databinding.*
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.model.WordItem
import com.dreampany.tools.ui.vm.WordViewModel
import com.klinker.android.link_builder.Link
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.skydoves.balloon.*
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import cz.kinst.jakub.view.StatefulLayout
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Roman-372 on 8/26/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class WordFragment
@Inject constructor() :
    BaseMenuFragment(),
    MaterialSearchView.OnQueryTextListener,
    MaterialSearchView.SearchViewListener,
    OnMenuItemClickListener<PowerMenuItem>,
    OnBalloonClickListener,
    OnBalloonOutsideTouchListener {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var pref: Pref
    @Inject
    internal lateinit var wordPref: WordPref
    private lateinit var bind: FragmentWordBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindFullWord: ContentFullWordBinding
    private lateinit var bindWord: ContentWordBinding
    private lateinit var bindRelated: ContentRelatedBinding
    private lateinit var bindDef: ContentDefinitionBinding
    private lateinit var bindYandex: ContentYandexTranslationBinding

    private lateinit var searchView: MaterialSearchView

    private lateinit var vm: WordViewModel

    private val langItems = ArrayList<PowerMenuItem>()
    private var langMenu: PowerMenu? = null

    private var balloon: Balloon? = null
    private var clickView: View? = null
    private var clickWord: String? = null

    private var queryText: String? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_word
    }

    override fun getMenuId(): Int {
        return R.menu.menu_word
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val searchItem = getSearchMenuItem()
        val shareItem = menu.findItem(R.id.item_share)
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context!!, R.color.material_white),
            null, searchItem, shareItem
        )

        val activity = getParent()

        if (activity is SearchViewCallback) {
            val searchCallback = activity as SearchViewCallback?
            searchView = searchCallback!!.searchView
            val searchItem = getSearchMenuItem()
            initSearchView(searchView, searchItem)
        }
        initLanguageUi()
    }

    override fun onStartUi(state: Bundle?) {
        buildLangItems()
        initUi()
        adjustTranslationUi()
        val uiTask = getCurrentTask<UiTask<Word>>(true)
        request(id = uiTask?.id, single = true, progress = true)
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_share -> {
                vm.share(this)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toggle_definition -> toggleDefinition()
            R.id.button_favorite -> {
                request(id = bind.item?.item?.id, action = Action.FAVORITE, single = true)
            }

            R.id.image_speak -> speak()

            R.id.button_language -> {
                openOptionsMenu(v)
            }
            R.id.layout_yandex -> openYandexSite()
        }
    }

    override fun onSearchViewShown() {
//toSearchMode()
    }

    override fun onSearchViewClosed() {
//toScanMode()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        Timber.v("onQueryTextSubmit %s", query)
        if (!query.isNotEmpty()) {
            request(
                id = query,
                action = Action.SEARCH,
                history = true,
                single = true,
                progress = true
            )
        }
        return super.onQueryTextSubmit(query)
    }

    override fun onQueryTextChange(newText: String): Boolean {
        Timber.v("onQueryTextChange %s", newText)
        queryText = newText
        ex.postToUi(request, 1000L)
        return super.onQueryTextChange(newText)
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        langMenu?.dismiss()
        val language: Language = item.tag as Language
        Timber.v("Language fired %s", language.toString())
        processOption(language)
    }

    override fun onBalloonClick(view: View) {
        AndroidUtil.speak(clickWord)
    }

    override fun onBalloonOutsideTouch(view: View, event: MotionEvent) {
        balloon?.dismiss()
    }

    private val request: Runnable = object : Runnable {
        override fun run() {
            if (!queryText.isNullOrEmpty()) {
                request(
                    id = queryText,
                    action = Action.SEARCH,
                    history = true,
                    single = true,
                    progress = false
                )
            }
        }
    }

    private fun buildLangItems(fresh: Boolean = false) {
        if (fresh) {
            langItems.clear()
        }
        if (langItems.isNotEmpty()) {
            return
        }
        val current = pref.getLanguage(Language.ENGLISH)
        val langs = Language.getAll()
        for (lang in langs) {
            langItems.add(PowerMenuItem(lang.toString(), lang.equals(current), lang))
        }
    }

    private fun initUi() {
        val uiTask = getCurrentTask<UiTask<Word>>(true)
        setTitle(uiTask!!.id)
        bind = super.binding as FragmentWordBinding
        bindStatus = bind.layoutTopStatus
        bindFullWord = bind.layoutFullWord
        bindWord = bindFullWord.layoutWord
        bindRelated = bindFullWord.layoutRelated
        bindDef = bindFullWord.layoutDefinition
        bindYandex = bindFullWord.layoutYandex

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )
        bind.stateful.setStateView(
            UiState.SEARCH.name,
            LayoutInflater.from(context).inflate(R.layout.item_search, null)
        )
        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null)
        )

        processUiState(UiState.DEFAULT)

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bindDef.toggleDefinition.setOnClickListener(this)
        bindWord.buttonFavorite.setOnClickListener(this)
        bindWord.textWord.setOnClickListener(this)
        bindWord.imageSpeak.setOnClickListener(this)
        bindWord.buttonLanguage.setOnClickListener(this)
        bindYandex.textYandexPowered.setOnClickListener(this)

        vm = ViewModelProviders.of(this, factory).get(WordViewModel::class.java)
        vm.task = uiTask
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initSearchView(searchView: MaterialSearchView, searchItem: MenuItem?) {

        searchView.setMenuItem(searchItem)
        searchView.setSubmitOnClick(true)

        searchView.setOnSearchViewListener(this)
        searchView.setOnQueryTextListener(this)
    }

    private fun initLanguageUi() {
        val language = pref.getLanguage(Language.ENGLISH)
        bindWord.buttonLanguage.text = language.code
    }

    private fun openOptionsMenu(v: View) {
//currentItem = item
        langMenu = PowerMenu.Builder(context)
            .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            .addItemList(langItems)
            .setSelectedMenuColor(ColorUtil.getColor(context!!, R.color.colorPrimary))
            .setSelectedTextColor(Color.WHITE)
            .setOnMenuItemClickListener(this)
            .setLifecycleOwner(this)
            .setDividerHeight(1)
            .setTextSize(14)
            .build()
        langMenu?.showAsAnchorRightTop(v)
    }

    private fun processOption(language: Language) {
        pref.setLanguage(language)
        initLanguageUi()
        adjustTranslationUi()
        buildLangItems(fresh = true)
        if (!language.equals(Language.ENGLISH)) {
            request(id = bind.item?.item?.id, history = true, single = true, progress = true)
        }
    }

    private fun processUiState(state: UiState) {
        when (state) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
//UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.SEARCH -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processSingleResponse(response: Response<WordItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<WordItem>
            processSingleSuccess(result.action, result.data)
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

    private fun processSuccessOfString(action: Action, items: List<String>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)

        if (action === Action.GET) {
            val result = DataUtil.toStringArray(items)
            searchView.setSuggestions(result)
            return
        }
    }

    private fun processSuccess(action: Action, items: List<WordItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)

        if (action === Action.GET) {
            if (!DataUtil.isEmpty(items)) {
                val suggests = arrayOfNulls<String>(items.size)
                for (index in items.indices) {
                    suggests[index] = items[index].item.id
                }
                searchView.setSuggestions(suggests)
            }
            return
        }
        ex.postToUi(kotlinx.coroutines.Runnable { processUiState(UiState.EXTRA) }, 500L)
    }

    private fun processSingleSuccess(action: Action, uiItem: WordItem) {
        Timber.v("Result Single Word[%s]", uiItem.item.id)
        if (action == Action.CLICK) {
            val text = getString(
                R.string.format_word_balloon,
                uiItem.item.id,
                resolveText(uiItem.item.getPartOfSpeech()),
                resolveText(uiItem.translation)
            )
            showBubble(clickView!!, text)
            clickView = null
            return
        }

        if (!uiItem.item.isEmpty()) {
            setTitle(uiItem.item.id)
            bind.setItem(uiItem)
            bindWord.layoutWord.visibility = View.VISIBLE
            if (uiItem.translation.isNullOrEmpty()) {
                bindWord.textTranslation.visibility = View.GONE
            } else {
                bindWord.textTranslation.visibility = View.VISIBLE
            }
            processRelated(uiItem.item.synonyms, uiItem.item.antonyms);
            processDefinitions(uiItem.item.definitions)
            processUiState(UiState.CONTENT)
        }
    }

    private fun processRelated(synonyms: List<String>?, antonyms: List<String>?) {
        val synonym = DataUtil.joinString(synonyms, Constants.Sep.COMMA_SPACE)
        val antonym = DataUtil.joinString(antonyms, Constants.Sep.COMMA_SPACE)

        if (!DataUtil.isEmpty(synonym)) {
            bindRelated.textSynonym.text = getString(R.string.synonyms, synonym)
            setSpan(bindRelated.textSynonym, synonym, getString(R.string.synonyms_bold))
            bindRelated.textSynonym.visibility = View.VISIBLE
        } else {
            bindRelated.textSynonym.visibility = View.GONE
        }

        if (!DataUtil.isEmpty(antonym)) {
            bindRelated.textAntonym.text = getString(R.string.antonyms, antonym)
            setSpan(bindRelated.textAntonym, antonym, getString(R.string.antonyms_bold))
            bindRelated.textAntonym.visibility = View.VISIBLE
        } else {
            bindRelated.textAntonym.visibility = View.GONE
        }
        bindRelated.layoutRelated.visibility =
            if (DataUtil.isEmpty(synonyms) && DataUtil.isEmpty(antonyms)) View.GONE else View.VISIBLE
    }

    private fun processDefinitions(definitions: MutableList<Definition>?) {
        if (definitions == null) {
            bindDef.layoutDefinition.visibility = View.GONE
            return
        }
        bindDef.layoutDefinition.visibility = View.VISIBLE
        val singleBuilder = StringBuilder()
        val multipleBuilder = StringBuilder()

        if (!DataUtil.isEmpty(definitions)) {
            for (index in definitions.indices) {
                val def = definitions[index]
                val text = TextUtil.stripHtml(def.text)
                if (index == 0) {
                    singleBuilder
                        .append(def.getPartOfSpeech())
                        .append(DataUtil.SEMI)
                        .append(DataUtil.SPACE)
                        .append(text)
                    multipleBuilder
                        .append(def.getPartOfSpeech())
                        .append(DataUtil.SEMI)
                        .append(DataUtil.SPACE)
                        .append(text)
                    continue
                }
                multipleBuilder
                    .append(DataUtil.NewLine2)
                    .append(def.getPartOfSpeech())
                    .append(DataUtil.SEMI)
                    .append(DataUtil.SPACE)
                    .append(text)
            }
        }

        if (singleBuilder.length > 0) {
            var text = singleBuilder.toString()
            bindDef.textSingleDefinition.text = text
            setSpan(bindDef.textSingleDefinition, text, null)

            text = multipleBuilder.toString()
            bindDef.textMultipleDefinition.text = text
            setSpan(bindDef.textMultipleDefinition, text, null)
            bindDef.layoutDefinition.visibility = View.VISIBLE

            if (definitions.size > 1) {
                bindDef.toggleDefinition.visibility = View.VISIBLE
            } else {
                bindDef.toggleDefinition.visibility = View.GONE
            }

        } else {
            bindDef.layoutDefinition.visibility = View.GONE
        }
    }

    private fun setSpan(view: TextView, text: String, bold: String?) {
        val items = TextUtil.getWords(text)
        TextUtil.setSpan(
            view,
            items,
            bold,
            object : Link.OnClickListener {
                override fun onClick(clickedText: String) {
                    onClickWord(view, clickedText)
                }
            },
            object : Link.OnLongClickListener {
                override fun onLongClick(clickedText: String) {
                    onLongClickWord(view, clickedText)
                }
            }
        )
    }

    private fun adjustTranslationUi() {
        val language = pref.getLanguage(Language.ENGLISH)
        val english = Language.ENGLISH.equals(language)
        val visible = !english
        bindWord.textTranslation.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun toggleDefinition() {
        if (bindDef.layoutSingleExpandable.isExpanded) {
            bindDef.layoutSingleExpandable.collapse(true)
            bindDef.layoutMultipleExpandable.expand(true)
            bindDef.toggleDefinition.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp)
        } else {
            bindDef.layoutMultipleExpandable.collapse(true)
            bindDef.layoutSingleExpandable.expand(true)
            bindDef.toggleDefinition.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp)
        }
    }

    private fun onClickWord(view: View, word: String) {
        clickView = view
        clickWord = word
        showBubble(view, word)
        request(id = word, history = true, action = Action.CLICK, single = true, progress = true)
        AndroidUtil.speak(word)
    }

    private fun onLongClickWord(view: View, word: String) {
        clickView = view
        clickWord = word
        searchView.clearFocus()
        request(
            id = word,
            history = true,
            action = Action.LONG_CLICK,
            single = true,
            progress = true
        )
        AndroidUtil.speak(word)
    }

    private fun speak() {
        val item = bindWord.getItem()
        item?.let {
            AndroidUtil.speak(it.item.id)
        }
    }

    private fun showBubble(view: View, text: String) {
        balloon?.run {
            if (isShowing) {
                dismiss()
            }
        }
        balloon = createBalloon(context!!) {
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
            setOnBalloonClickListener(this@WordFragment)
            setOnBalloonOutsideTouchListener(this@WordFragment)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(this@WordFragment)
        }
        balloon?.run {
            view.showAlignTop(this)
        }
    }

    private fun openYandexSite() {
        val outTask = UiTask<Word>(
            type = Type.SITE,
            subtype = Subtype.DEFAULT,
            action = Action.OPEN,
            extra = Constants.Translation.YANDEX_URL
        )
        openActivity(ToolsActivity::class.java, outTask)
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