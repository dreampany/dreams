package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.callback.SearchViewCallback
import com.dreampany.frame.ui.enums.UiState
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.util.*
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.data.misc.WordRequest
import com.dreampany.tools.data.model.Definition
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.data.source.pref.WordPref
import com.dreampany.tools.databinding.*
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.WordItem
import com.dreampany.tools.vm.WordViewModel
import com.klinker.android.link_builder.Link
import com.miguelcatalan.materialsearchview.MaterialSearchView
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
    OnMenuItemClickListener<PowerMenuItem> {

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
    private var recentWord: String? = null

    private val langItems = ArrayList<PowerMenuItem>()
    private var langMenu: PowerMenu? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_word
    }

    override fun getMenuId(): Int {
        return R.menu.menu_word
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

/*    override fun getTitleResId(): Int {
        return R.string.title_word
    }*/

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
        request(id = recentWord, single = true, progress = true)
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
        if (searchView.isSearchOpen()) {
            searchView.closeSearch()
        }
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

    override fun onSearchViewShown() {
        //toSearchMode()
    }

    override fun onSearchViewClosed() {
        //toScanMode()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        Timber.v("onQueryTextSubmit %s", query)
        recentWord = query
        request(
            id = recentWord,
            action = Action.SEARCH,
            history = true,
            single = true,
            progress = true
        )
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
        recentWord = uiTask!!.input!!.id
        setTitle(recentWord)
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
    private fun processOption(language: Language) {
        pref.setLanguage(language)
        initLanguageUi()
        adjustTranslationUi()
        buildLangItems(fresh = true)
        if (!language.equals(Language.ENGLISH)) {
            request(id = recentWord, history = true, single = true, progress = true)
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
            UiState.EMPTY -> bind.stateful.setState(UiState.SEARCH.name)
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
            processSingleSuccess(result.data)
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

    private fun processSingleSuccess(uiItem: WordItem) {
        Timber.v("Result Single Word[%s]", uiItem.item.id)
        recentWord = uiItem.item.id
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
            source = Language.ENGLISH.code,
            target = language.code,
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