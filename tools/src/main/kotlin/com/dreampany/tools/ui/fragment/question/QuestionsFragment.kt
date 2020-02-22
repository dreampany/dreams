package com.dreampany.tools.ui.fragment.question

import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnHorizontalScrollListener
import com.dreampany.framework.ui.listener.OnSnapScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.databinding.ContentQuestionsBinding
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentQuestionsBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.question.QuestionAdapter
import com.dreampany.tools.ui.misc.QuestionRequest
import com.dreampany.tools.ui.model.question.QuestionItem
import com.dreampany.tools.ui.model.question.QuestionReq
import com.dreampany.tools.ui.vm.question.QuestionViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2020-02-20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class QuestionsFragment
@Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager

    private lateinit var bind: FragmentQuestionsBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindQuestions: ContentQuestionsBinding

    private lateinit var vm: QuestionViewModel
    private lateinit var adapter: QuestionAdapter
    private lateinit var scroller: OnSnapScrollListener

    override fun getLayoutId(): Int {
        return R.layout.fragment_questions
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()

        val task = getCurrentTask<UiTask<Question>>() ?: return
        val req = QuestionReq.parse(task.extra) ?: return

        request(
            limit = req.limit,
            category = req.category,
            type = req.type,
            difficult = req.difficult,
            progress = true
        )
    }

    override fun onStopUi() {

    }

    override fun onRefresh() {
        super.onRefresh()
        if (adapter.isEmpty) {
            request(progress = true)
        } else {
            vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        }
    }

    private fun initUi() {
        bind = super.binding as FragmentQuestionsBinding
        bindStatus = bind.layoutTopStatus
        bindQuestions = bind.layoutQuestions
        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProvider(this, factory).get(QuestionViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        val snapHelper = PagerSnapHelper()
        scroller = object : OnSnapScrollListener(snapHelper) {
            override fun onSnapChange(position: Int) {
                //adapter.invalidateItemDecorations(1000L)
            }
        }
        adapter = QuestionAdapter(listener = this)
        adapter.setStickyHeaders(false)
        ViewUtil.setRecycler(
            adapter,
            bindQuestions.recycler,
            SmoothScrollLinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false),
            FlexibleItemDecoration(context!!)
                .withOffset(adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
        snapHelper.attachToRecyclerView(bindQuestions.recycler)
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
            }
        }
    }

    fun processMultipleResponse(response: Response<List<QuestionItem>>) {
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
            val result = response as Response.Result<List<QuestionItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    fun processSingleResponse(response: Response<QuestionItem>) {
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
            val result = response as Response.Result<QuestionItem>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<QuestionItem>) {
        Timber.v("Result Action[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }

    private fun processSuccess(state: State, action: Action, item: QuestionItem) {
        /*if (action == Action.DELETE) {
            adapter.removeItem(item)
        } else {
            adapter.addItem(item)
        }*/

        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        limit: Long = Constants.Default.LONG,
        id: String? = Constants.Default.NULL,
        input: Question? = Constants.Default.NULL,
        category: Question.Category? = Constants.Default.NULL,
        type: Question.Type? = Constants.Default.NULL,
        difficult: Difficult? = Constants.Default.NULL,
        answer: String? = Constants.Default.NULL
    ) {
        val request = QuestionRequest(
            type = Type.QUESTION,
            subtype = Subtype.DEFAULT,
            state = state,
            action = action,
            single = single,
            progress = progress,
            limit = limit,
            id = id,
            input = input,
            category = category,
            questionType = type,
            difficult = difficult,
            answer = answer
        )
        vm.request(request)
    }
}