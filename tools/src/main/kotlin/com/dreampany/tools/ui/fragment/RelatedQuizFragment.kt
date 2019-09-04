package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.Subtype
import com.dreampany.frame.data.enums.Type
import com.dreampany.frame.data.model.Response
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.misc.exception.EmptyException
import com.dreampany.frame.misc.exception.ExtraException
import com.dreampany.frame.misc.exception.MultiException
import com.dreampany.frame.ui.enums.UiState
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnVerticalScrollListener
import com.dreampany.frame.ui.model.UiTask
import com.dreampany.frame.util.TextUtil
import com.dreampany.frame.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.misc.RelatedQuizRequest
import com.dreampany.tools.data.model.Quiz
import com.dreampany.tools.databinding.*
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.adapter.QuizOptionAdapter
import com.dreampany.tools.ui.adapter.WordAdapter
import com.dreampany.tools.ui.model.RelatedQuizItem
import com.dreampany.tools.ui.vm.RelatedQuizViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
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
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: RelatedQuizViewModel
    private lateinit var adapter: QuizOptionAdapter
    private lateinit var scroller: OnVerticalScrollListener

    private var quizItem: RelatedQuizItem? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_related_quiz
    }

    override fun onStartUi(state: Bundle?) {
        val uiTask = getCurrentTask<UiTask<Quiz>>() ?: return
        initUi()
        initRecycler()
        request(subtype = uiTask.subtype, single = true, progress = true)
    }

    override fun onStopUi() {
        processUiState(UiState.HIDE_PROGRESS)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.layout_parent -> {
                val tag = v.tag as Int
                val answer = quizItem!!.item.options!!.get(tag)
                when (tag) {
                    0 -> {
                        if (answer.equals(quizItem!!.item.answer)) {

                        } else {

                        }
                    }
                    1 -> {
                        if (answer.equals(quizItem!!.item.answer)) {

                        }
                    }
                    2 -> {
                        if (answer.equals(quizItem!!.item.answer)) {

                        }
                    }
                    3 -> {
                        if (answer.equals(quizItem!!.item.answer)) {

                        }
                    }
                }
            }
        }
    }

    private fun initUi() {
        //setTitle(R.string.home)
        bind = super.binding as FragmentRelatedQuizBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        ViewUtil.setSwipe(bind.layoutRefresh, this)

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )
        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null)
        )

        vm = ViewModelProviders.of(this, factory).get(RelatedQuizViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = QuizOptionAdapter(this)
        adapter.setStickyHeaders(true)
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_quiz_option, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
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
            UiState.EXTRA -> processUiState(if (quizItem == null) UiState.EMPTY else UiState.CONTENT)
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
       // bind.setItem(item)
        quizItem = item
        showQuiz()
        processUiState(UiState.CONTENT)
    }


    private fun showQuiz() {
        val quiz = quizItem!!.item!!
        val title = TextUtil.getString(
            context,
            R.string.title_quiz_header,
            TextUtil.toTitleCase(quiz.subtype.name),
            quiz.id
        )
      //  bindQuizHeader.textTitle.text = title


/*        quizItem!!.drawLetter(bindQuizOptionOne.imageIcon, "A")
        quizItem!!.drawLetter(bindQuizOptionTwo.imageIcon, "B")
        quizItem!!.drawLetter(bindQuizOptionThree.imageIcon, "C")
        quizItem!!.drawLetter(bindQuizOptionFour.imageIcon, "D")

        if (quiz.options!!.size >= 1) {
            bindQuizOptionOne.textTitle.text = quiz.options!!.first()
        }
        if (quiz.options!!.size >= 2) {
            bindQuizOptionTwo.textTitle.text = quiz.options!!.get(1)
        }
        if (quiz.options!!.size >= 3) {
            bindQuizOptionThree.textTitle.text = quiz.options!!.get(2)
        }
        if (quiz.options!!.size >= 4) {
            bindQuizOptionFour.textTitle.text = quiz.options!!.get(3)
        }*/
    }

    private fun request(
        subtype: Subtype = Subtype.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {

        val request = RelatedQuizRequest(
            type = Type.QUIZ,
            subtype = subtype,
            action = action,
            single = single,
            progress = progress
        )
        vm.request(request)
    }
}