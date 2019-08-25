package com.dreampany.word.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.adapter.SmartAdapter
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.frame.util.ColorUtil
import com.dreampany.frame.util.MenuTint
import com.dreampany.frame.util.ViewUtil
import com.dreampany.language.Language
import com.dreampany.word.R
import com.dreampany.word.data.model.Word
import com.dreampany.word.data.model.WordRequest
import com.dreampany.word.databinding.ContentRecyclerBinding
import com.dreampany.word.databinding.ContentTopStatusBinding
import com.dreampany.word.databinding.FragmentFavoritesBinding
import com.dreampany.word.ui.activity.ToolsActivity
import com.dreampany.word.ui.adapter.WordAdapter
import com.dreampany.word.ui.enums.UiSubtype
import com.dreampany.word.ui.enums.UiType
import com.dreampany.word.ui.model.UiTask
import com.dreampany.word.ui.model.WordItem
import com.dreampany.word.vm.WordViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/19/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class FavoriteWordsFragment
@Inject constructor() : BaseMenuFragment(),
    SmartAdapter.Callback<WordItem> {

    private val NONE = "none"
    private val EMPTY = "empty"

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private lateinit var bind: FragmentFavoritesBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var scroller: OnVerticalScrollListener
    private lateinit var vm: WordViewModel
    private lateinit var adapter: WordAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_favorites
    }

    override fun getMenuId(): Int {
        return R.menu.menu_search
    }

    override fun getSearchMenuItemId(): Int {
        return R.id.item_search
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        initRecycler()

        processUiState(UiState.NONE)
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onResume() {
        super.onResume()
        request(true, true)
    }

    override fun onRefresh() {
        super.onRefresh()
        request(true, true)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        val searchItem = findMenuItemById(R.id.item_search)
        MenuTint.colorMenuItem(searchItem, ColorUtil.getColor(context, R.color.material_white), null)
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (adapter.hasNewFilter(newText)) {
            adapter.setFilter(newText)
            adapter.filterItems()
        }
        return false
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.text_word -> {
                val text = ViewUtil.getText(v)
                AndroidUtil.speak(text)
            }
        }
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        if (position != RecyclerView.NO_POSITION) {
            val item = adapter.getItem(position)
            openUi(item!!.item)
            return true
        }
        return false
    }

    override val empty: Boolean
        get() = adapter.isEmpty()
    override val items: List<WordItem>?
        get() = adapter.getCurrentItems()
    override val visibleItems: List<WordItem>?
        get() = adapter.getVisibleItems()
    override val visibleItem: WordItem?
        get() = adapter.getVisibleItem()

    private fun initView() {
        setTitle(R.string.favourite_words)

        bind = super.binding as FragmentFavoritesBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            NONE,
            LayoutInflater.from(context).inflate(R.layout.item_none, null)
        )
        bind.stateful.setStateView(
            EMPTY,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null)
        )

        ViewUtil.setText(this, R.id.text_empty, R.string.empty_favourite)
        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProviders.of(this, factory).get(WordViewModel::class.java)
        vm.setUiCallback(this)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = WordAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = OnVerticalScrollListener()
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

    private fun processUiState(state: UiState) {
        when (state) {
            UiState.NONE -> bind.stateful.setState(NONE)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.EMPTY -> bind.stateful.setState(EMPTY)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
            }
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
            processSuccess(result.type, result.data)
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

    private fun processSuccess(type: Response.Type, items: List<WordItem>) {
        adapter.addFavoriteItems(items)
        ex.postToUi({ processUiState(UiState.EXTRA) }, 500L)
    }

    private fun processSingleSuccess(item: WordItem) {
        adapter.updateSilently(item)
    }

    private fun openUi(item: Word) {
        val task = UiTask<Word>(false, UiType.WORD, UiSubtype.VIEW, item, null)
        openActivity(ToolsActivity::class.java, task)
    }

    private fun request(important: Boolean, progress: Boolean) {
        val translate = vm.needToTranslate()
        val language = vm.getCurrentLanguage()

        val request = WordRequest()
        request.source = Language.ENGLISH.code
        request.target = language.code
        request.translate = translate
        request.important = important
        request.progress = progress
        request.favorite = true
        vm.loads(request)
    }
}