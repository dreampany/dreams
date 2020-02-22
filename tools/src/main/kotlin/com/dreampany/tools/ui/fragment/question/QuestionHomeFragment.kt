package com.dreampany.tools.ui.fragment.question

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.extension.parseLong
import com.dreampany.framework.misc.extension.resToStringArray
import com.dreampany.framework.misc.extension.setOnSafeClickListener
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.databinding.ContentQuestionHomeBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentQuestionHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.model.question.QuestionReq
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import javax.inject.Inject

/**
 * Created by roman on 2020-02-16
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class QuestionHomeFragment
@Inject constructor() : BaseMenuFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var session: SessionManager

    private lateinit var bind: FragmentQuestionHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindQuestion: ContentQuestionHomeBinding

    private val categoryItems = ArrayList<PowerMenuItem>()
    private val typeItems = ArrayList<PowerMenuItem>()
    private val difficultItems = ArrayList<PowerMenuItem>()
    private var categoryMenu: PowerMenu? = null
    private var typeMenu: PowerMenu? = null
    private var difficultMenu: PowerMenu? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_question_home
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_question
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = super.binding as FragmentQuestionHomeBinding
        bindStatus = bind.layoutTopStatus
        bindQuestion = bind.layoutQuestionHome

        bindQuestion.spinnerCategory.apply {
            lifecycleOwner = this@QuestionHomeFragment
        }

        bindQuestion.spinnerType.apply {
            lifecycleOwner = this@QuestionHomeFragment
        }

        bindQuestion.spinnerDifficult.apply {
            lifecycleOwner = this@QuestionHomeFragment
        }

        bindQuestion.spinnerLimit.apply {
            lifecycleOwner = this@QuestionHomeFragment
        }

        bind.fab.setOnSafeClickListener {
            openQuestionsUi()
        }
    }

    private fun openQuestionsUi() {
        val selectedCategory = R.array.question_categories.resToStringArray(context)?.get(bindQuestion.spinnerCategory.selectedIndex)
        val selectedType = R.array.question_types.resToStringArray(context)?.get(bindQuestion.spinnerType.selectedIndex)
        val selectedDifficult = R.array.question_difficulties.resToStringArray(context)?.get(bindQuestion.spinnerDifficult.selectedIndex)
        val selectedLimit = R.array.question_limits.resToStringArray(context)?.get(bindQuestion.spinnerLimit.selectedIndex)

        val category = Constants.Values.QuestionValues.getCategory(selectedCategory)
        val type = Constants.Values.QuestionValues.getTypeOfUi(selectedType)
        val difficult = Constants.Values.QuestionValues.getDifficult(selectedDifficult)
        val limit = selectedLimit.parseLong()

        val req = QuestionReq(category = category, type = type, difficult = difficult, limit = limit)
        val task = UiTask<Question>(
            type = Type.QUESTION,
            subtype = Subtype.DEFAULT,
            action = Action.SOLVE,
            extra = req.toJson()
        )
        openActivity(ToolsActivity::class.java, task)
    }
}