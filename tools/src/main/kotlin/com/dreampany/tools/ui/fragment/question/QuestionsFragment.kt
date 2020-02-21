package com.dreampany.tools.ui.fragment.question

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.tools.R
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.databinding.*
import com.dreampany.tools.ui.model.question.QuestionReq
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

    override fun getLayoutId(): Int {
        return R.layout.fragment_questions
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
    }

    override fun onStopUi() {

    }

    private fun initUi() {
        bind = super.binding as FragmentQuestionsBinding
        bindStatus = bind.layoutTopStatus
        bindQuestions = bind.layoutQuestions

        val task = getCurrentTask<UiTask<Question>>() ?: return
        val req = QuestionReq.parse(task.extra) ?: return


    }
}