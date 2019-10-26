package com.dreampany.tools.ui.fragment.word

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.ui.misc.QuizRequest
import com.dreampany.tools.data.model.Quiz
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentWordQuizBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.QuizAdapter
import com.dreampany.tools.ui.model.QuizItem
import com.dreampany.tools.ui.vm.QuizViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-08-29
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class WordQuizFragment
@Inject constructor() :
    BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<QuizItem?, Action?> {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager
    private lateinit var bind: FragmentWordQuizBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var adapter: QuizAdapter
    private lateinit var vm: QuizViewModel
    private lateinit var scroller: OnVerticalScrollListener

    override fun getLayoutId(): Int {
        return R.layout.fragment_word_quiz
    }

    override fun getTitleResId(): Int {
        return R.string.quiz
    }

    override fun getScreen(): String {
        return Constants.wordQuiz(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        initTitleSubtitle()
        initUi()
        initRecycler()
        session.track()
        request(type = Type.QUIZ, subtype = Subtype.RELATED, action = Action.GET, progress = true)
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onUiItemClick(view: View, item: QuizItem?, action: Action?) {
        openQuizUi(item)
    }

    override fun onUiItemLongClick(view: View, item: QuizItem?, action: Action?) {

    }

    private fun initTitleSubtitle() {
        //val subtitle = getString(R.string.subtitle_favorite_words, adapter.itemCount)
        // setSubtitle(subtitle)
    }

    private fun initUi() {
        bind = super.binding as FragmentWordQuizBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null).apply {
                //setOnClickListener(this@WordQui)
            }
        )


        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProviders.of(this, factory).get(QuizViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.updateUiState(uiState = UiState.DEFAULT)
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = QuizAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {}
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_quiz, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        when (response.uiState) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
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
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
                //initTitleSubtitle()
            }
        }
    }

    fun processMultipleResponse(response: Response<List<QuizItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<QuizItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<QuizItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)

        ex.postToUi(Runnable {
            vm.updateUiState(state, action, UiState.EXTRA)
        }, 500L)
    }

    private fun request(
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val request = QuizRequest(
            type = type,
            subtype = subtype,
            single = single,
            progress = progress
        )
        vm.request(request)
    }

    private fun openQuizUi(uiItem: QuizItem?) {
        var task: UiTask<Quiz>? = null
        uiItem?.run {
            when (item.subtype) {
                Subtype.SYNONYM,
                Subtype.ANTONYM -> {
                    task = UiTask<Quiz>(
                        type = Type.QUIZ,
                        subtype = item.subtype,
                        input = item
                    )
                }
            }
        }
        task?.run {
            openActivity(ToolsActivity::class.java, this)
        }
    }
}