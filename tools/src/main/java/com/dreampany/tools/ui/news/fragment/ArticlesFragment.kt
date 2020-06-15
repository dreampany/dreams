package com.dreampany.tools.ui.news.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.init
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.toTint
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tools.databinding.RecyclerChildFragmentBinding
import com.dreampany.tools.ui.news.adapter.FastArticleAdapter
import com.dreampany.tools.ui.news.vm.ArticleViewModel
import javax.inject.Inject
import com.dreampany.tools.R
import com.dreampany.tools.data.enums.news.NewsAction
import com.dreampany.tools.data.enums.news.NewsState
import com.dreampany.tools.data.enums.news.NewsSubtype
import com.dreampany.tools.data.enums.news.NewsType
import com.dreampany.tools.ui.news.model.ArticleItem
import kotlinx.android.synthetic.main.content_recycler_ad.view.*
import timber.log.Timber

/**
 * Created by roman on 14/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class ArticlesFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: RecyclerChildFragmentBinding
    private lateinit var vm: ArticleViewModel

    private lateinit var adapter: FastArticleAdapter

    override val layoutRes: Int = R.layout.recycler_child_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        onRefresh()
    }

    override fun onStopUi() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRefresh() {
        loadArticles()
    }

    private fun onItemPressed(view: View, item: ArticleItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                //openCoinUi(item)
            }
            R.id.button_favorite -> {
                //onFavoriteClicked(item)
            }
            else -> {

            }
        }
    }

    private fun initUi() {
        bind = getBinding()
        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_articles)
        vm = createVm(ArticleViewModel::class)
        vm.subscribe(this, Observer { this.processResponse(it) })
        vm.subscribes(this, Observer { this.processResponses(it) })
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastArticleAdapter(
                { currentPage ->
                    Timber.v("CurrentPage: %d", currentPage)
                    onRefresh()
                }, this::onItemPressed
            )
        }

        adapter.initRecycler(
            state,
            bind.layoutRecycler.recycler
        )
    }

    private fun processResponse(response: Response<NewsType, NewsSubtype, NewsState, NewsAction, ArticleItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<NewsType, NewsSubtype, NewsState, NewsAction, ArticleItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
        }
    }

    private fun processResponses(response: Response<NewsType, NewsSubtype, NewsState, NewsAction, List<ArticleItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<NewsType, NewsSubtype, NewsState, NewsAction, List<ArticleItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
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
        /*val task = task ?: return
        if (task.state is RadioState) {
            when (task.state) {
                RadioState.LOCAL -> {
                    vm.loadStations(
                        task.state as RadioState,
                        context.countryCode,
                        adapter.itemCount.toLong()
                    )
                }
                RadioState.TRENDS,
                RadioState.POPULAR -> {
                    vm.loadStations(task.state as RadioState, adapter.itemCount.toLong())
                }
            }
        }*/
    }
}