package com.dreampany.history.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.api.session.SessionManager
import com.dreampany.frame.data.enums.UiState
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.view.TextViewClickMovement
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.util.ViewUtil
import com.dreampany.history.R
import com.dreampany.history.data.model.History
import com.dreampany.history.data.model.HistoryRequest
import com.dreampany.history.databinding.ContentHistoryBinding
import com.dreampany.history.databinding.ContentTopStatusBinding
import com.dreampany.history.databinding.FragmentHistoryBinding
import com.dreampany.history.ui.activity.ToolsActivity
import com.dreampany.history.ui.enums.UiSubtype
import com.dreampany.history.ui.enums.UiType
import com.dreampany.history.ui.model.HistoryItem
import com.dreampany.history.ui.model.UiTask
import com.dreampany.history.vm.HistoryViewModel
import cz.kinst.jakub.view.StatefulLayout
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Roman-372 on 7/29/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HistoryFragment
@Inject constructor() :
    BaseMenuFragment(),
    TextViewClickMovement.OnTextViewClickMovementListener {

    private val NONE = "none"

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentHistoryBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindHistory: ContentHistoryBinding

    private lateinit var vm: HistoryViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_history
    }

    override fun onStartUi(state: Bundle?) {
        initView()
        session.track()
        initTitleSubtitle()
        val task = vm.task as UiTask<History>
        val history = task.input!!
        request(history, true, true, false)
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onRefresh() {
        super.onRefresh()
        processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_favorite -> {
                val task = vm.task as UiTask<History>
                val history = task.input!!
                vm.toggleFavorite(history)
            }
        }
    }

    override fun onLinkClicked(linkText: String, linkType: TextViewClickMovement.LinkType) {
        if (linkText.isNotEmpty()) {
            val task = vm.task as UiTask<History>
            val history = task.input!!
            val link = history?.getLinkByTitle(linkText)
            link?.run {
                openSite(this.link)
            }
        }
    }

    override fun onLongClick(text: String) {
    }

    private fun initTitleSubtitle() {
        setTitle(R.string.history)
        val subtitle = vm.getHistoryType().toTitle()
        setSubtitle(subtitle)
    }

    private fun initView() {
        bind = super.binding as FragmentHistoryBinding
        bindStatus = bind.layoutTopStatus
        bindHistory = bind.layoutHistory

        bind.stateful.setStateView(
            NONE,
            LayoutInflater.from(context).inflate(R.layout.item_none, null)
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bindHistory.buttonFavorite.setOnClickListener(this)
        bindHistory.textHtml.movementMethod = TextViewClickMovement(this, getContext())

        vm = ViewModelProviders.of(this, factory).get(HistoryViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processResponse(it) })

        vm.task = getCurrentTask(true)
    }

    private fun processResponse(response: Response<HistoryItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<HistoryItem>
            processSuccess(result.data)
        }
    }

    private fun processUiState(state: UiState) {
        when (state) {
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> {
            }
            UiState.SEARCH -> {
            }
            UiState.EMPTY -> {
            }
            UiState.ERROR -> {
            }
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processSuccess(uiItem: HistoryItem) {
        val history = uiItem.item
        bindHistory.textHtml.text =
            HtmlCompat.fromHtml(history.html!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        bindHistory.textYear.text =
            TextUtil.getString(getContext(), R.string.year_format, history.year)

        bindHistory.buttonFavorite.isLiked = uiItem.favorite
    }

    private fun request(
        history: History,
        important: Boolean,
        progress: Boolean,
        favorite: Boolean
    ) {
        val type = vm.getHistoryType()
        Timber.v("Request type %s", type)
        val day = vm.getDay()
        val month = vm.getMonth()

        val request = HistoryRequest(type, day, month, important, progress, favorite)
        request.input = history
        vm.load(request)
    }

    fun openSite(url: String) {
        var task = UiTask<History>(true, UiType.SITE, UiSubtype.VIEW, null, url)
        openActivity(ToolsActivity::class.java, task)
    }
}