package com.dreampany.crypto.ui.news

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.dreampany.crypto.R
import com.dreampany.crypto.data.enums.Action
import com.dreampany.crypto.data.enums.State
import com.dreampany.crypto.data.enums.Subtype
import com.dreampany.crypto.data.enums.Type
import com.dreampany.crypto.data.model.Article
import com.dreampany.crypto.databinding.NewsFragmentBinding
import com.dreampany.crypto.ui.web.WebActivity
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.init
import com.dreampany.framework.misc.exts.open
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.toTint
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.stateful.StatefulLayout
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.content_recycler_ad.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class NewsFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: NewsFragmentBinding
    private lateinit var vm: ArticleViewModel
    private lateinit var galleryAdapter: SliderAdapter
    private lateinit var adapter: FastArticleAdapter

    override val layoutRes: Int = R.layout.news_fragment
    override val menuRes: Int = R.menu.menu_search
    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initSlider()
        initRecycler(state)
        if (galleryAdapter.isEmpty)
            onRefresh()
    }

    override fun onStopUi() {
        //adapter.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(context, R.color.material_white)
        //findMenuItemById(R.id.item_favorites).toTint(context, R.color.material_white)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_favorites -> {
                openFavoritesUi()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter(newText)
        return false
    }

    override fun onRefresh() {
        loadArticles()
    }

    private fun onItemPressed(view: View, item: ArticleItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                //openArticleUi(item)
                openWeb(item.input.url)
            }
            R.id.button_favorite -> {
                onFavoriteClicked(item)
            }
            else -> {

            }
        }
    }

    private fun initUi() {
        bind = getBinding()
        bind.swipe.init(this)
        bind.stateful.setStateView(StatefulLayout.State.EMPTY, R.layout.content_empty_coins)
        vm = createVm(ArticleViewModel::class)
        vm.subscribe(this, Observer { this.processResponse(it) })
        vm.subscribes(this, Observer { this.processResponses(it) })
    }

    private fun initSlider() {
        if (!::galleryAdapter.isInitialized) {
            galleryAdapter = SliderAdapter(requireContext())
        }
        bind.slider.apply {
            setSliderAdapter(galleryAdapter)
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        }
    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
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



        //ViewCompat.setNestedScrollingEnabled(bind.layoutRecycler.recycler, false)
    }

    private fun loadArticles() {
        vm.loadArticles()
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
            //adapter.updateItem(result)
        }
    }

    private fun processResults(result: List<ArticleItem>?) {
        if (result != null) {
            val heads = result.subList(0, 5)
            galleryAdapter.addItems(heads)
            adapter.addItems(result)
        }

        if (galleryAdapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun onFavoriteClicked(item: ArticleItem) {
        //vm.toggleFavorite(item.input, CoinItem.ItemType.ITEM)
    }


    private fun openArticleUi(item: ArticleItem) {
        val task = UiTask(
            Type.ARTICLE,
            Subtype.DEFAULT,
            State.DEFAULT,
            Action.VIEW,
            item.input
        )
        //open(CoinActivity::class, task)
    }

    private fun openFavoritesUi() {
        //open(FavoriteCoinsActivity::class)
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
}