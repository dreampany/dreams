package com.dreampany.tools.ui.fragment.word

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BasicGridItem
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.misc.extensions.inflate
import com.dreampany.framework.misc.extensions.resolveText
import com.dreampany.framework.misc.extensions.toColor
import com.dreampany.framework.misc.extensions.toTint
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.callback.SearchViewCallback
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.*
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.data.model.word.Definition
import com.dreampany.tools.data.model.Note
import com.dreampany.tools.data.model.word.Quiz
import com.dreampany.tools.data.model.word.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.databinding.*
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.WordAdapter
import com.dreampany.tools.ui.request.LoadRequest
import com.dreampany.tools.ui.request.WordRequest
import com.dreampany.tools.ui.model.word.WordItem
import com.dreampany.tools.ui.vm.LoaderViewModel
import com.dreampany.tools.ui.vm.word.WordViewModel
import com.ferfalk.simplesearchview.SimpleSearchView
import com.gaurav.gesto.OnGestureListener
import com.klinker.android.link_builder.Link
import com.skydoves.balloon.*
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class WordHomeFragment
@Inject constructor() : BaseMenuFragment(),
    SmartAdapter.Callback<WordItem>,
    SmartAdapter.OnUiItemClickListener<WordItem, Action>,
    SimpleSearchView.OnQueryTextListener,
    SimpleSearchView.SearchViewListener,
    OnMenuItemClickListener<PowerMenuItem>,
    OnBalloonClickListener,
    OnBalloonOutsideTouchListener {

    @Inject
    internal lateinit var session: SessionManager
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var pref: Pref
    @Inject
    internal lateinit var wordPref: WordPref

    private lateinit var bind: FragmentWordHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding
    private lateinit var bindFullWord: ContentFullWordBinding
    private lateinit var bindWord: ContentWordBinding
    private lateinit var bindRelated: ContentRelatedBinding
    private lateinit var bindDef: ContentDefinitionBinding
    private lateinit var bindYandex: ContentYandexTranslationBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var searchView: SimpleSearchView

    private lateinit var vm: WordViewModel
    private lateinit var loaderVm: LoaderViewModel
    private lateinit var adapter: WordAdapter

    private val langItems = ArrayList<PowerMenuItem>()
    private var langMenu: PowerMenu? = null
    private val sheetItems = arrayListOf<BasicGridItem>()

    private var balloon: Balloon? = null
    private var clickedView: View? = null
    private var clickedWord: String? = null

    private var queryText: String? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_word_home
    }

    override fun getMenuId(): Int {
        return R.menu.menu_word_home
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_word
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val searchItem = getSearchMenuItem()
        val favoriteItem = findMenuItemById(R.id.item_favorite)
        val settingsItem = findMenuItemById(R.id.item_settings)
        searchItem.toTint(context, R.color.material_white)
        favoriteItem.toTint(context, R.color.material_white)
        settingsItem.toTint(context, R.color.material_white)

        val activity = getParent()

        if (activity is SearchViewCallback) {
            (activity as SearchViewCallback).let {
                searchView = it.searchView
            }
            searchItem?.run {
                initSearchView(searchView, this)
            }
        }
        initLanguageUi()
    }

    override fun getScreen(): String {
        return Constants.wordHome(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        buildLangItems()
        buildSheetItems()
        initUi()
        initRecycler()
        toScanMode()
        adjustTranslationUi()
        onRefresh()
        loadRequest()
        session.track()
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
        }
    }

    override fun onResume() {
        super.onResume()
        initLanguageUi()
        AndroidUtil.initTts(context)
    }

    override fun onPause() {
        AndroidUtil.stopTts()
        super.onPause()
    }

    override fun hasBackPressed(): Boolean {
        if (searchView.onBackPressed()) {
            searchView.closeSearch()
            return true
        }
        return super.hasBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.v("onActivityResult");
        if (searchView.onActivityResult(requestCode, resultCode, data)) {
            return
        }

        when (requestCode) {
            Constants.RequestCode.OCR -> {
                if (isOkay(resultCode)) {
                    request(
                        state = State.HISTORY,
                        action = Action.GET,
                        progress = true,
                        limit = Constants.Limit.Word.HISTORY
                    )
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorite -> {
                openFavoriteUi()
                return true
            }
            R.id.item_settings -> {

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        if (Constants.Cache.Word.HISTORY || adapter.isEmpty) {
            request(
                state = State.HISTORY,
                action = Action.GET,
                single = false,
                progress = true,
                limit = Constants.Limit.Word.HISTORY
            )
            Constants.Cache.Word.HISTORY = false
        } else {
            vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toggle_definition -> toggleDefinition()
            R.id.button_favorite -> {
                request(id = bind.item?.item?.id, action = Action.FAVORITE, single = true)
            }
            R.id.fab -> processFabAction()
            R.id.image_speak -> speak()
            R.id.text_word -> {
                bindWord.getItem()?.run {
                    openWordUi(this.item)
                }
            }
            R.id.button_language -> {
                openOptionsMenu(v)
            }
            R.id.layout_yandex -> openYandexSite()
            R.id.layout_bottom_slide -> {
                openOptionSheet()
            }
        }
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        if (position != RecyclerView.NO_POSITION) {
            val item = adapter.getItem(position)
            // openWordUi(item.getItem());
            return true
        }
        return false
    }

    override fun onSearchViewShown() {
        toSearchMode()
    }

    override fun onSearchViewClosed() {
        toScanMode()
    }

    override fun onSearchViewShownAnimation() {

    }

    override fun onSearchViewClosedAnimation() {

    }

    override fun onQueryTextSubmit(query: String): Boolean {
        Timber.v("onQueryTextSubmit %s", query)
        if (!query.isNotEmpty()) {
            request(
                id = query,
                state = State.HISTORY,
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

    override fun onQueryTextCleared(): Boolean {
        return false
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        langMenu?.dismiss()
        val language: Language = item.tag as Language
        Timber.v("Language fired %s", language.toString())
        processOption(language)
    }

    override fun onBalloonClick(view: View) {
        AndroidUtil.speak(clickedWord)
    }

    override fun onBalloonOutsideTouch(view: View, event: MotionEvent) {
        balloon?.dismiss()
    }

    override val empty: Boolean
        get() = false
    override val items: List<WordItem>?
        get() = adapter.currentItems
    override val visibleItems: List<WordItem>?
        get() = adapter.getVisibleItems()
    override val visibleItem: WordItem?
        get() = adapter.getVisibleItem()


    override fun onUiItemClick(view: View, item: WordItem, action: Action) {
        openWordUi(item.item)
    }

    override fun onUiItemLongClick(view: View, item: WordItem, action: Action) {
    }

    private val request: Runnable = object : Runnable {
        override fun run() {
            if (!queryText.isNullOrEmpty()) {
                request(
                    id = queryText,
                    action = Action.SEARCH,
                    history = true,
                    single = true,
                    important = true,
                    progress = true
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

    private fun buildSheetItems() {
        if (sheetItems.isNotEmpty()) {
            return
        }
        sheetItems.add(
            BasicGridItem(
                R.drawable.ic_play_circle_filled_black_24dp,
                getString(R.string.synonym)
            )
        )
        sheetItems.add(
            BasicGridItem(
                R.drawable.ic_play_circle_filled_black_24dp,
                getString(R.string.antonym)
            )
        )
    }

    private fun initUi() {
        bind = super.binding as FragmentWordHomeBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler
        bindFullWord = bind.layoutFullWord
        bindWord = bindFullWord.layoutWord
        bindRelated = bindFullWord.layoutRelated
        bindDef = bindFullWord.layoutDefinition
        bindYandex = bindFullWord.layoutYandex

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            context.inflate(R.layout.item_default)
        )
        bind.stateful.setStateView(
            UiState.SEARCH.name,
            context.inflate(R.layout.item_search)
        )
        bind.stateful.setStateView(
            UiState.EMPTY.name,
            context.inflate(R.layout.content_empty_words)?.apply {
                setOnClickListener(this@WordHomeFragment)
            }
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bindDef.toggleDefinition.setOnClickListener(this)
        bindWord.buttonFavorite.setOnClickListener(this)
        bindWord.textWord.setOnClickListener(this)
        bindWord.imageSpeak.setOnClickListener(this)
        bindWord.buttonLanguage.setOnClickListener(this)
        bind.fab.setOnClickListener(this)
        bindYandex.textYandexPowered.setOnClickListener(this)
        //bind.layoutBottomSlide.setOnClickListener(this)

        bind.layoutBottomSlide.setOnTouchListener(object : OnGestureListener(context!!) {
            override fun onClick() {
                openOptionSheet()
            }

            override fun onSwipeTop() {
                openOptionSheet()
            }
        })

        vm = ViewModelProvider(this, factory).get(WordViewModel::class.java)
        loaderVm = ViewModelProvider(this, factory).get(LoaderViewModel::class.java)
        vm.setUiCallback(this)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputsOfString(this, Observer { this.processResponseOfString(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = WordAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {
            override fun onScrollingAtEnd() {

            }
        }
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_word, vm.itemOffset)
                .withEdge(true),
            null,
            scroller, null
        )
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

    private fun initSearchView(searchView: SimpleSearchView, searchItem: MenuItem) {
        searchView.setMenuItem(searchItem)
        searchView.setOnQueryTextListener(this)
        searchView.setOnSearchViewListener(this)
        /*   searchView.setSubmitOnClick(true)
          searchView.setVoiceSearch(true)
          //searchView.
          searchView.setOnSearchViewListener(this)
          searchView.setOnQueryTextListener(this)*/
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
            .setSelectedMenuColor(R.color.colorPrimary.toColor(context))
            .setSelectedTextColor(R.color.material_white.toColor(context))
            .setOnMenuItemClickListener(this)
            .setLifecycleOwner(this)
            .setDividerHeight(1)
            .setTextSize(14)
            .build()
        langMenu?.showAsAnchorRightTop(v)
    }

    private fun processUiState(response: Response.UiResponse) {
        when (response.uiState) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> {
                response.uiState = if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT
                processUiState(response)
            }
            UiState.SEARCH -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processResponseOfString(response: Response<List<String>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<String>>
            processSuccessOfString(result.action, result.data)
        }
    }

    private fun processMultipleResponse(response: Response<List<WordItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<WordItem>>
            processMultipleSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSingleResponse(response: Response<WordItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<WordItem>
            processSingleSuccess(result.state, result.action, result.data)
        }
    }

    private fun toScanMode() {
        bind.fab.setImageResource(R.drawable.ic_filter_center_focus_black_24dp)
    }

    private fun toSearchMode() {
        bind.fab.setImageResource(R.drawable.ic_search_black_24dp)
    }

    private fun processFabAction() {
        if (searchView.isSearchOpen()) {
            searchView.clearFocus()
            bind.item?.item?.id?.run {
                request(
                    action = Action.SEARCH,
                    single = true,
                    progress = true,
                    id = this,
                    history = true
                )
            }
            return
        }
        openOcr()
    }

    private fun processSuccessOfString(action: Action, items: List<String>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)

        if (action === Action.GET) {
            val result = DataUtil.toStringArray(items)
            //searchView.setSuggestions(result)
            return
        }
    }

    private fun processMultipleSuccess(state: State, action: Action, items: List<WordItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)

        if (state == State.HISTORY) {
            adapter.addItemsOfHistory(items)
            ex.postToUi(Runnable {
                vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
            }, 500L)
            return
        }

        if (action === Action.GET) {
            if (!DataUtil.isEmpty(items)) {
                val suggests = arrayOfNulls<String>(items.size)
                for (index in items.indices) {
                    suggests[index] = items[index].item.id
                }
                //searchView.setSuggestions(suggests)
            }
            return
        }
        adapter.clear()
        adapter.addItems(items)
        ex.postToUi(
            kotlinx.coroutines.Runnable {
                vm.updateUiState(
                    state = state,
                    action = action,
                    uiState = UiState.EXTRA
                )
            },
            500L
        )
    }

    private fun processSingleSuccess(state: State, action: Action, item: WordItem) {
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
            return
        }

        if (item.item.hasWeight()) {
            adapter.addItemOfHistory(item)
            bindRecycler.recycler.smoothScrollToPosition(0)
            vm.updateUiState(state = state, action = action, uiState = UiState.CONTENT)
/*            bind.setItem(item)
            bindWord.layoutWord.visibility = View.VISIBLE
            if (item.translation.isNullOrEmpty()) {
                bindWord.textTranslation.visibility = View.GONE
            } else {
                bindWord.textTranslation.visibility = View.VISIBLE
            }
            //processRelated(item.getItem().getSynonyms(), item.getItem().getAntonyms());
            processDefinitions(item.item.definitions)
            vm.updateUiState(state, action, UiState.CONTENT)*/
        }
    }

    private fun processRelated(synonyms: List<String>, antonyms: List<String>) {
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
        clickedView = view
        clickedWord = word
        showBubble(view, word)
        request(id = word, history = true, action = Action.CLICK, single = true, progress = true)
        AndroidUtil.speak(word)
    }

    private fun onLongClickWord(view: View, word: String) {
        clickedView = view
        clickedWord = word
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

    private fun openOptionSheet() {
        MaterialDialog(context!!, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            cornerRadius(res = R.dimen._10sdp)
            gridItems(items = sheetItems) { _, index, item ->
                processSheetOption(index, item)
            }

        }
    }

    private fun processSheetOption(index: Int, item: BasicGridItem) {
        when (index) {
            0 -> {
                openQuizUi(Subtype.SYNONYM)
            }
            1 -> {
                openQuizUi(Subtype.ANTONYM)
            }
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
            setOnBalloonClickListener(this@WordHomeFragment)
            setOnBalloonOutsideTouchListener(this@WordHomeFragment)
            setArrowOrientation(ArrowOrientation.BOTTOM)
            setBalloonAnimation(BalloonAnimation.FADE)
            setLifecycleOwner(this@WordHomeFragment)
        }
        balloon?.run {
            view.showAlignTop(this)
        }
    }

    private fun loadRequest() {
        val request = LoadRequest(type = Type.WORD, action = Action.LOAD)
        loaderVm.request(request)
    }

    private fun openWordUi(item: Word) {
        val task = UiTask<Word>(
            type = Type.WORD,
            action = Action.OPEN,
            id = item.id,
            input = item
        )
        openActivity(ToolsActivity::class.java, task)
        getParent()?.run {
            AndroidUtil.hideSoftInput(this)
        }
    }

    private fun openOcr() {
        val task = UiTask<Word>(type = Type.OCR, subtype = Subtype.DEFAULT, action = Action.OPEN)
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.OCR)
    }

    private fun openFavoriteUi() {
        val task = UiTask<Word>(
            type = Type.WORD,
            state = State.FAVORITE,
            action = Action.OPEN
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.FAVORITE)
    }

    private fun openSettingsUi() {
        val task = UiTask<Note>(
            type = Type.NOTE,
            state = State.SETTINGS,
            action = Action.OPEN
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.SETTINGS)
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

    private fun openPlayUi(subtype: Subtype) {
        val task = UiTask<Word>(
            type = Type.QUIZ,
            subtype = subtype,
            state = State.HOME
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.QUIZ)
    }

    private fun openQuizUi(subtype: Subtype) {
        var task: UiTask<Quiz> = UiTask<Quiz>(
            type = Type.QUIZ,
            subtype = subtype
        )
        openActivity(ToolsActivity::class.java, task)
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        important: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        limit: Long = Constants.Default.LONG,
        id: String? = Constants.Default.NULL,
        input: Word? = Constants.Default.NULL,
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
            important = important,
            progress = progress,
            limit = limit,
            id = id,
            input = input,
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