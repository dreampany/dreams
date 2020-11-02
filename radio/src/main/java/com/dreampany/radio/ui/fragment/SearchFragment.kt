package com.dreampany.radio.ui.fragment

import android.os.Bundle
import android.view.View
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.radio.data.source.pref.Prefs
import com.dreampany.stateful.StatefulLayout
import kotlinx.android.synthetic.main.content_recycler.view.*
import timber.log.Timber
import javax.inject.Inject
import com.dreampany.radio.R
import com.dreampany.radio.data.enums.Action
import com.dreampany.radio.data.enums.State
import com.dreampany.radio.data.enums.Subtype
import com.dreampany.radio.data.enums.Type
import com.dreampany.radio.data.model.Article
import com.dreampany.radio.databinding.SearchFragmentBinding
import com.dreampany.radio.misc.Constants
import com.dreampany.radio.ui.adapter.FastArticleAdapter
import com.dreampany.radio.ui.model.ArticleItem
import com.dreampany.radio.ui.vm.ArticleViewModel
import com.dreampany.radio.ui.vm.PageViewModel
import com.dreampany.radio.ui.vm.SearchViewModel
import com.dreampany.radio.ui.web.WebActivity

/**
 * Created by roman on 30/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class SearchFragment
@Inject constructor() : InjectFragment() {

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: SearchFragmentBinding
    private lateinit var searchVm: SearchViewModel
    private lateinit var pageVm: PageViewModel
    private lateinit var vm: ArticleViewModel
    private lateinit var adapter: FastArticleAdapter
    private lateinit var query: String

    override val layoutRes: Int = R.layout.search_fragment
    override val menuRes: Int = R.menu.search_menu
    override val searchMenuItemId: Int = R.id.item_search

    override val params: Map<String, Map<String, Any>?>?
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, parentRef.packageName)
            param.put(Constant.Param.VERSION_CODE, parentRef.versionCode)
            param.put(Constant.Param.VERSION_NAME, parentRef.versionName)
            param.put(Constant.Param.SCREEN, "SearchFragment")

            params.put(Constant.Event.fragment(context), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        bind.stateful.setState(StatefulLayout.State.DEFAULT)
    }

    override fun onStopUi() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::adapter.isInitialized) {
            var outState = outState
            outState = adapter.saveInstanceState(outState)
            super.onSaveInstanceState(outState)
            return
        }
        super.onSaveInstanceState(outState)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //adapter.filter(newText)
        bind.layoutMake.hide()
        /*if (newText.isNullOrEmpty().not()) {
            searchVideos(newText.value)
        }*/
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val value = query.trimValue
        if (value.isNotEmpty()) {
            this.query = value
            searchArticles(value)
            writeSearch()
        }
        return false
    }

    private fun onItemPressed(view: View, input: ArticleItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openWeb(input.input.url)
            }
            /*R.id.favorite -> {
                onFavoriteClicked(item)
            }*/
            else -> {

            }
        }
    }

    override val backPressed: Boolean
        get() {

            return false
        }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        searchVm = createVm(SearchViewModel::class)
        pageVm = createVm(PageViewModel::class)
        vm = createVm(ArticleViewModel::class)

        vm.subscribe(this, { this.processResponse(it) })
        vm.subscribes(this, { this.processResponses(it) })

        bind.swipe.init(this)
        bind.stateful.setStateView(
            StatefulLayout.State.DEFAULT,
            R.layout.content_default_search_articles
        )
        bind.stateful.setStateView(
            StatefulLayout.State.EMPTY,
            R.layout.content_empty_search_articles
        )
        bind.stateful.setStateView(StatefulLayout.State.OFFLINE, R.layout.content_offline_articles)

        bind.layoutMake.setOnSafeClickListener {
            writePage()
            bind.layoutMake.hide()
        }
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastArticleAdapter(
            { currentPage: Int ->
                Timber.v("CurrentPage: %d", currentPage)
                onRefresh()
            }, this::onItemPressed
        )
        adapter.initRecycler(state, bind.layoutRecycler.recycler)
    }

    fun openWeb(url: String?) {
        if (url.isNullOrEmpty()) {
            //TODO
            return
        }
        val task = UiTask(
            Type.SITE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.DEFAULT,
            null as Article?,
            url = url
        )
        open(WebActivity::class, task)
    }

/*    private fun onFavoriteClicked(item: VideoItem) {
        vm.toggleFavorite(item.input)
    }*/

    private fun searchArticles(query: String) {
        vm.loadSearch(query)
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<ArticleItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<ArticleItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, ArticleItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, ArticleItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processError(error: SmartError) {
        if (error.hostError) {
            if (adapter.isEmpty) {
                bind.stateful.setState(StatefulLayout.State.OFFLINE)
            } else {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
            }
        }

        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
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
        )
    }

    private fun processResults(result: List<ArticleItem>?) {
        adapter.clearAll()
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
            bind.layoutMake.hide()
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
            bind.layoutMake.show()
        }
    }

    private fun processResult(result: ArticleItem?) {
        if (result != null) {
            adapter.addItem(result)
        }
    }

    private fun writePage() {
        pageVm.write(query)
    }

    private fun writeSearch() {
        searchVm.write(query, Constants.Values.SEARCH)
    }
}