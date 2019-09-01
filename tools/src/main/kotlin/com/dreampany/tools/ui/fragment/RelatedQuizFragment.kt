package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.enums.UiState
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.util.ViewUtil
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.data.misc.RelatedQuizRequest
import com.dreampany.tools.databinding.ContentRelatedQuizBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentRelatedQuizBinding
import com.dreampany.tools.databinding.FragmentWordHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.RelatedQuizItem
import com.dreampany.tools.ui.model.WordItem
import com.dreampany.tools.ui.vm.LoaderViewModel
import com.dreampany.tools.ui.vm.RelatedQuizViewModel
import com.dreampany.tools.ui.vm.WordViewModel
import cz.kinst.jakub.view.StatefulLayout
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by roman on 2019-08-31
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class RelatedQuizFragment
@Inject constructor(

) : BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private lateinit var bind: FragmentRelatedQuizBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRelated: ContentRelatedQuizBinding

    private lateinit var vm: RelatedQuizViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_related_quiz
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
    }

    private fun initUi() {
        //setTitle(R.string.home)
        bind = super.binding as FragmentRelatedQuizBinding
        bindStatus = bind.layoutTopStatus
        bindRelated = bind.layoutRelatedQuiz

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )
        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null)
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)

        vm = ViewModelProviders.of(this, factory).get(RelatedQuizViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
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

    private fun processSingleResponse(response: Response<RelatedQuizItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<RelatedQuizItem>
            processSingleSuccess(result.action, result.data)
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

    private fun processSingleSuccess(action: Action, item: RelatedQuizItem) {
        Timber.v("Result Related Quiz[%s]", item.item.id)
        bind.setItem(item)

        processUiState(UiState.CONTENT)
    }

    private fun request(
        type: Type = Type.QUIZ,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {

        val request = RelatedQuizRequest(
            type = type,
            action = action,
            single = single,
            progress = progress
        )
        //vm.request(request)
    }
}