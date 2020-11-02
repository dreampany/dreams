package com.dreampany.news.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.news.R
import com.dreampany.news.data.enums.Action
import com.dreampany.news.data.enums.State
import com.dreampany.news.data.enums.Subtype
import com.dreampany.news.data.enums.Type
import com.dreampany.news.data.model.Article
import com.dreampany.news.data.model.Page
import com.dreampany.news.databinding.ArticlesFragmentBinding
import com.dreampany.news.misc.Constants
import com.dreampany.news.ui.adapter.FastArticleAdapter
import com.dreampany.news.ui.model.ArticleItem
import com.dreampany.news.ui.vm.ArticleViewModel
import com.dreampany.news.ui.vm.SearchViewModel
import com.dreampany.news.ui.web.WebActivity
import com.dreampany.stateful.StatefulLayout
import com.google.android.gms.location.LocationRequest
import com.patloew.colocation.CoLocation
import kotlinx.android.synthetic.main.content_recycler_ad.view.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 29/10/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class ArticlesFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: ArticlesFragmentBinding
    private lateinit var searchVm: SearchViewModel
    private lateinit var vm: ArticleViewModel
    private lateinit var adapter: FastArticleAdapter
    private lateinit var input: Page
    private lateinit var query: String

    override val layoutRes: Int = R.layout.articles_fragment
    override val menuRes: Int = R.menu.articles_menu
    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        val task = (task ?: return) as UiTask<Type, Subtype, State, Action, Page>
        input = task.input ?: return
        initUi()
        initRecycler(state)
        /*       if (adapter.isEmpty.not()) {
                   adapter.notifyUi()
               }*/
        // onRefresh()
    }

    override fun onStopUi() {
        Timber.v("")
        //reset("bind")
        //reset("adapter")
    }

    override fun onStart() {
        super.onStart()
        onRefresh()
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

    override fun onViewStateRestored(inState: Bundle?) {
        super.onViewStateRestored(inState)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (::adapter.isInitialized) {
            adapter.filter(newText)
            val value = newText.trimValue
            if (value.isNotEmpty()) {
                this.query = value
                ex.getUiHandler().removeCallbacks(runner)
                ex.getUiHandler().postDelayed(runner, 3000L)
            }
        }
        return false
    }

    override fun onRefresh() {
        loadArticles()
    }

    private fun onItemPressed(view: View, input: ArticleItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                openWeb(input.input.url)
            }
            else -> {

            }
        }
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        searchVm = createVm(SearchViewModel::class)
        vm = createVm(ArticleViewModel::class)

        vm.subscribe(this, { this.processResponse(it) })
        vm.subscribes(this, { this.processResponses(it) })

        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_articles)
        bind.stateful.setStateView(StatefulLayout.State.OFFLINE, R.layout.content_offline_articles)
    }

    private fun initRecycler(state: Bundle?) {
        if (::adapter.isInitialized) return
        adapter = FastArticleAdapter(
            { currentPage ->
                Timber.v("CurrentPage: %d", currentPage)
                onRefresh()
            }, this::onItemPressed
        )
        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler
        )
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

    private fun processResult(result: ArticleItem?) {
        if (result != null) {
            adapter.updateItem(result)
        }
    }

    private fun processResults(result: List<ArticleItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun loadArticles() {
        if (adapter.isEmpty) {
            if (input.type.isLocal) {
                readLocalArticles()
            } else if (input.type.isCategory) {
                vm.loadArticles(input)
            } else if (input.type.isCustom) {
                vm.loadSearch(input.id)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun readLocalArticles() {
        if (context.hasLocationPermission) {
            readLocalArticlesSafe()
        } else {
            if (isFinishing) return
            runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
                readLocalArticlesSafe()
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun readLocalArticlesSafe() {
        val location = CoLocation.from(requireContext())
        val request = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        vm.viewModelScope.launch {
            /* val result = location.checkLocationSettings(request)
             when (request) {

             }*/
            val data = location.getLastLocation()
            if (data == null) {
                vm.loadArticles(input)
            } else {
                vm.loadLocalArticles(input.id)
            }
        }
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

    private val runner = Runnable {
        writeSearch()
    }

    private fun writeSearch() {
        if (isFinishing) return
        searchVm.write(query, Constants.Values.ARTICLES)
    }
}