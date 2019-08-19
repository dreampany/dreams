package com.dreampany.tools.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.ui.callback.SearchViewCallback
import com.dreampany.frame.ui.enums.UiState
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.util.*
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.data.misc.WordRequest
import com.dreampany.tools.data.model.Definition
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.databinding.*
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.WordAdapter
import com.dreampany.tools.ui.model.WordItem
import com.dreampany.tools.vm.WordViewModel
import com.klinker.android.link_builder.Link
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class WordHomeFragment
@Inject constructor() :
    BaseMenuFragment(),
    SmartAdapter.Callback<WordItem>,
    MaterialSearchView.OnQueryTextListener,
    MaterialSearchView.SearchViewListener,
    OnMenuItemClickListener<PowerMenuItem> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var pref: Pref
    private lateinit var bind: FragmentWordHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding
    private lateinit var bindFullWord: ContentFullWordBinding
    private lateinit var bindWord: ContentWordBinding
    private lateinit var bindRelated: ContentRelatedBinding
    private lateinit var bindDef: ContentDefinitionBinding
    private lateinit var bindYandex: ContentYandexTranslationBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var searchView: MaterialSearchView

    private lateinit var vm: WordViewModel
    private lateinit var adapter: WordAdapter
    private var recentWord: String? = null

    private val langItems = ArrayList<PowerMenuItem>()
    private var langMenu: PowerMenu? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_word_home
    }

    override fun getMenuId(): Int {
        return R.menu.menu_word_home
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun onStartUi(state: Bundle?) {
        buildLangItems()
        initView()
        initRecycler()
        toScanMode()
        processUiState(UiState.SEARCH)
        adjustTranslationUi()
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
        }
    }

    override fun onResume() {
        super.onResume()
        initLanguageMenuItem()
        request(id = recentWord, recent = true, translate = true, progress = true)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        MenuTint.colorMenuItem(
            getSearchMenuItem(),
            ColorUtil.getColor(context!!, R.color.material_white),
            null
        )

        val activity = getParent()

        if (activity is SearchViewCallback) {
            val searchCallback = activity as SearchViewCallback?
            searchView = searchCallback!!.searchView
            val searchItem = getSearchMenuItem()
            initSearchView(searchView, searchItem)
        }
        initLanguageMenuItem()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_search ->
                //searchView.open(item);
                return true
            R.id.item_language -> {
                openOptionsMenu()
                //openLanguagePicker()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        super.onRefresh()
        //processUiState(UiState.HIDE_PROGRESS);
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toggle_definition -> toggleDefinition()
            R.id.button_favorite -> {
/*                bind.getItem()?.let {
                    vm.toggleFavorite(it.item)
                }*/
            }
            R.id.fab -> processFabAction()
            R.id.image_speak -> speak()
            R.id.text_word -> {
/*                bind.getItem()?.let {
                    openUi(it.item)
                }*/
            }
            R.id.layout_yandex -> openYandexSite()
        }
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        if (position != RecyclerView.NO_POSITION) {
            val item = adapter.getItem(position)
            // openUi(item.getItem());
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

    override fun onQueryTextSubmit(query: String): Boolean {
        Timber.v("onQueryTextSubmit %s", query)
        recentWord = query
        // request(recentWord, false, true, true, true)
        return super.onQueryTextSubmit(query)
    }

    override fun onQueryTextChange(newText: String): Boolean {
        Timber.v("onQueryTextChange %s", newText)
        recentWord = newText
        return super.onQueryTextChange(newText)
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        langMenu?.dismiss()
        val language: Language = item.tag as Language
        Timber.v("Language fired %s", language.toString())
        processOption(language)
    }

    override val empty: Boolean
        get() = false
    override val items: List<WordItem>?
        get() = adapter.currentItems
    override val visibleItems: List<WordItem>?
        get() = adapter.getVisibleItems()
    override val visibleItem: WordItem?
        get() = adapter.getVisibleItem()

    override fun hasBackPressed(): Boolean {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
            return true
        }
        return super.hasBackPressed()
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

    private fun initView() {
        setTitle(R.string.home)
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

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bindDef.toggleDefinition.setOnClickListener(this)
        bindWord.buttonFavorite.setOnClickListener(this)
        bindWord.textWord.setOnClickListener(this)
        bindWord.imageSpeak.setOnClickListener(this)
        bind.fab.setOnClickListener(this)
        bindYandex.textYandexPowered.setOnClickListener(this)

        vm = ViewModelProviders.of(this, factory).get(WordViewModel::class.java)
        vm.setUiCallback(this)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputsOfString(this, Observer { this.processResponseOfString(it) })
        vm.observeOutputs(this, Observer { this.processResponse(it) })
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
        initLanguageMenuItem()
        adjustTranslationUi()
        buildLangItems(fresh = true)
        if (!language.equals(Language.ENGLISH)) {
            request(id = recentWord, translate = true, progress = true)
        }
    }

    private fun initSearchView(searchView: MaterialSearchView, searchItem: MenuItem?) {

        searchView.setMenuItem(searchItem)
        searchView.setSubmitOnClick(true)

        searchView.setOnSearchViewListener(this)
        searchView.setOnQueryTextListener(this)

        //vm.suggests(false)
    }

    private fun initLanguageMenuItem() {
        val language = pref.getLanguage(Language.ENGLISH)
        val item = findMenuItemById(R.id.item_language)
        if (item != null) {
            item.title = language.code
        }
    }

    private fun openOptionsMenu() {
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
        langMenu?.showAsAnchorRightTop(view)
    }

    private fun openLanguagePicker() {
        val languages = Language.getAll()

        /*val picker = LanguagePicker.newInstance(getString(R.string.select_language), languages)
        picker.setCallback { language ->
            pref.setLanguage(language)
            initLanguageMenuItem()
            adjustTranslationUi()
            val language = pref.getLanguage(Language.ENGLISH)
            val english = Language.ENGLISH.equals(language)
            if (!english) {
                request(id = recentWord, recent = true, progress = false)
            }
            picker.dismissAllowingStateLoss()
            //Unit
        }
        picker.show(fragmentManager!!, Constants.Tag.LANGUAGE_PICKER)*/
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
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.SEARCH -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    fun processResponseOfString(response: Response<List<String>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<String>>
            processSuccessOfString(result.action, result.data)
        }
    }

    fun processResponse(response: Response<List<WordItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<WordItem>>
            processSuccess(result.action, result.data)
        }
    }

    private fun processSingleResponse(response: Response<WordItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<WordItem>
            processSingleSuccess(result.data)
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
            request(recentWord, recent = true, progress = true)
            return
        }
        openOcr()
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

    private fun processSuccessOfString(action: Action, items: List<String>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)

        if (action === Action.SUGGESTS) {
            val result = DataUtil.toStringArray(items)
            searchView.setSuggestions(result)
            return
        }
    }

    private fun processSuccess(action: Action, items: List<WordItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)

        if (action === Action.SUGGESTS) {
            if (!DataUtil.isEmpty(items)) {
                val suggests = arrayOfNulls<String>(items.size)
                for (index in items.indices) {
                    suggests[index] = items[index].item.id
                }
                searchView.setSuggestions(suggests)
            }
            return
        }
        adapter.clear()
        adapter.addItems(items)
        ex.postToUi({ processUiState(UiState.EXTRA) }, 500L)
    }

    private fun processSingleSuccess(item: WordItem) {
        Timber.v("Result Single Word[%s]", item.item.id)
        recentWord = item.item.id
        bind.setItem(item)
        bindWord.layoutWord.visibility = View.VISIBLE
        //processRelated(item.getItem().getSynonyms(), item.getItem().getAntonyms());
        processDefinitions(item.item.definitions)
        processUiState(UiState.CONTENT)
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
            return
        }
        val singleBuilder = StringBuilder()
        val multipleBuilder = StringBuilder()

        if (!DataUtil.isEmpty(definitions)) {
            for (index in definitions.indices) {
                val def = definitions[index]
                if (index == 0) {
                    singleBuilder
                        .append(def.getPartOfSpeech())
                        .append(DataUtil.SEMI)
                        .append(DataUtil.SPACE)
                        .append(def.text)
                    multipleBuilder
                        .append(def.getPartOfSpeech())
                        .append(DataUtil.SEMI)
                        .append(DataUtil.SPACE)
                        .append(def.text)
                    continue
                }
                multipleBuilder
                    .append(DataUtil.NewLine2)
                    .append(def.getPartOfSpeech())
                    .append(DataUtil.SEMI)
                    .append(DataUtil.SPACE)
                    .append(def.text)
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
                    searchWord(clickedText)
                }
            },
            object : Link.OnLongClickListener {
                override fun onLongClick(clickedText: String) {
                    searchWord(clickedText)
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

    private fun searchWord(word: String) {
        recentWord = word
        searchView.clearFocus()
        //request(recent = true)
        AndroidUtil.speak(recentWord)
    }

    private fun speak() {
        val item = bindWord.getItem()
        item?.let {
            AndroidUtil.speak(it.item.id)
        }
    }

    private fun request(
        id: String? = Constants.Default.NULL,
        recent: Boolean = Constants.Default.BOOLEAN,
        translate: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val language = pref.getLanguage(Language.ENGLISH)
/*        val translate = !Language.ENGLISH.equals(language)*/

        val request = WordRequest(
            id = id,
            source = Language.ENGLISH.code,
            target = language.code,
            recent = recent,
            translate = translate,
            progress = progress
        )
        vm.request(request)
    }

    private fun openUi(item: Word) {
        val task = UiTask<Word>(
            type = Type.WORD,
            subtype = Subtype.DEFAULT,
            action = Action.OPEN,
            input = item
        )
        openActivity(ToolsActivity::class.java, task)
    }

    private fun openOcr() {
        val task = UiTask<Word>(type = Type.OCR, subtype = Subtype.DEFAULT, action = Action.OPEN)
        openActivity(ToolsActivity::class.java, task)
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
}