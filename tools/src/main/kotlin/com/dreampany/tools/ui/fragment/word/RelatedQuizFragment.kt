package com.dreampany.tools.ui.fragment.word

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.ui.misc.RelatedQuizRequest
import com.dreampany.tools.data.model.Quiz
import com.dreampany.tools.data.model.RelatedQuiz
import com.dreampany.tools.data.model.Word
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.databinding.*
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.adapter.QuizOptionAdapter
import com.dreampany.tools.ui.model.QuizOptionItem
import com.dreampany.tools.ui.model.RelatedQuizItem
import com.dreampany.tools.ui.vm.word.RelatedQuizViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import nl.dionsegijn.konfetti.ParticleSystem
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import timber.log.Timber
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

) : BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<QuizOptionItem?, Action?> {

    @Inject
    internal lateinit var pref: Pref
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    private lateinit var bind: FragmentRelatedQuizBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRelated: ContentRelatedQuizBinding
    private lateinit var bindRelatedHeader: ContentRelatedQuizHeaderBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: RelatedQuizViewModel
    private lateinit var adapter: QuizOptionAdapter
    private lateinit var scroller: OnVerticalScrollListener

    private lateinit var subtype: Subtype
    private var quizItem: RelatedQuizItem? = null
    private var particle: ParticleSystem? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_related_quiz
    }

    override fun getMenuId(): Int {
        return R.menu.menu_related_quiz
    }

    override fun getTitleResId(): Int {
        val uiTask = getCurrentTask<UiTask<Quiz>>() ?: return R.string.quiz
        when (uiTask.subtype) {
            Subtype.SYNONYM -> return R.string.synonym_quiz
            Subtype.ANTONYM -> return R.string.antonym_quiz
            else -> return R.string.quiz
        }
    }

    override fun getScreen(): String {
        return Constants.relatedQuiz(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        val level = pref.getLevel(Level.A1)
        findMenuItemById(R.id.item_level)?.apply {
            title = getString(R.string.format_level, level.name)
        }
    }

    override fun onStartUi(state: Bundle?) {
        val uiTask = getCurrentTask<UiTask<Quiz>>() ?: return
        subtype = uiTask.subtype
        initUi()
        initRecycler()
        request(
            state = State.DEFAULT,
            resolve = State.PLAYED,
            action = Action.GET,
            single = true,
            progress = true
        )
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.layout_parent -> {

            }
            R.id.button_view -> {
                quizItem?.run {
                    openWordUi(item.id)
                }
            }
            R.id.button_next -> {
                particle?.run {
                    bind.konfetti.stop(this)
                }
                vm.updateUiState(uiState = UiState.DEFAULT)
                adapter.clear()
                request(
                    state = State.DEFAULT,
                    resolve = State.PLAYED,
                    action = Action.NEXT,
                    single = true,
                    progress = true
                )
            }
        }
    }

    override fun onUiItemClick(view: View, item: QuizOptionItem?, action: Action?) {
        item?.run {
            performAnswer(this)
        }
    }

    override fun onUiItemLongClick(view: View, item: QuizOptionItem?, action: Action?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initUi() {
        bind = super.binding as FragmentRelatedQuizBinding
        bindStatus = bind.layoutTopStatus
        bindRelated = bind.layoutRelatedQuiz
        bindRelatedHeader = bindRelated.layoutHeader
        bindRecycler = bindRelated.layoutRecycler


        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bindRelated.buttonView.setOnClickListener(this)
        bindRelated.buttonNext.setOnClickListener(this)


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

        vm.updateUiState(uiState = UiState.DEFAULT)
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        scroller = object : OnVerticalScrollListener() {}
        adapter = QuizOptionAdapter(this)
        adapter.setStickyHeaders(true)
        /*adapter.setAnimationInterpolator( DecelerateInterpolator())
            .setAnimationDuration(300L)*/
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollLinearLayoutManager(context!!),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_quiz_option_header, adapter.getItemOffset())
                .addItemViewType(R.layout.item_quiz_option, adapter.getItemOffset())
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
            UiState.SEARCH -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
            }
        }
    }

    private fun processSingleResponse(response: Response<RelatedQuizItem>) {
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
            val result = response as Response.Result<RelatedQuizItem>
            processSingleSuccess(result.action, result.data)
        }
    }

    private fun processSingleSuccess(action: Action, item: RelatedQuizItem) {
        Timber.v("Result Related Quiz[%s]", item.item.id)
        if (action == Action.GET) {
            bindRelated.buttonView.isEnabled = false
        }
        if (action == Action.NEXT) {
            adapter.clear()
            bindRelated.buttonView.isEnabled = false
        }
        if (action == Action.SOLVE) {
            bindRelated.buttonView.isEnabled = true
        }
        quizItem = item
        val headerItem = item.getHeaderOptionItem(context!!)
        val result = item.getOptionItems(context!!)

        bindRelatedHeader.textPoint.text =
            getString(R.string.format_quiz_point, headerItem.credit, headerItem.totalCredit)

        bindRelatedHeader.textCount.text =
            getString(R.string.format_quiz_count, headerItem.count, headerItem.totalCount)


        bindRelatedHeader.textTitle.text = headerItem.item.id
        adapter.addItems(result)
        vm.updateUiState(uiState = UiState.CONTENT)

        quizItem?.run {
            if (played()) {
                if (isWinner()) {
                    rightAnswer()
                } else {
                    wrongAnswer()
                }
            }
        }
    }

    private fun performAnswer(item: QuizOptionItem) {
        Timber.v("Select %s", item.item.id)
        if (quizItem!!.played()) {
            return
        }
        val quiz = quizItem!!.item
        val given = item.item.id
        request(
            state = State.DEFAULT,
            resolve = State.PLAYED,
            action = Action.SOLVE,
            input = quiz,
            single = true,
            progress = false,
            given = given
        )
    }

    private fun rightAnswer() {
        particle = bind.konfetti.build()
        particle?.apply {
            addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(10))
                .setPosition(-50f, bind.konfetti.width + 50f, -50f, -50f)
                .streamFor(300, 3000L)
        }
        bind.konfetti.start(particle!!)
    }

    private fun wrongAnswer() {

    }

    private fun openWordUi(id: String) {
        val task = UiTask<Word>(
            type = Type.WORD,
            action = Action.OPEN,
            id = id
        )
        openActivity(ToolsActivity::class.java, task)
    }

    private fun request(
        state: State = State.DEFAULT,
        resolve: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        input: RelatedQuiz? = Constants.Default.NULL,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        given: String? = Constants.Default.NULL
    ) {

        val request = RelatedQuizRequest(
            type = Type.QUIZ,
            subtype = subtype,
            state = state,
            resolve = resolve,
            action = action,
            input = input,
            single = single,
            progress = progress,
            given = given
        )
        vm.request(request)
    }
}