package com.dreampany.tools.ui.fragment.question

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.extension.resToStringArray
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.language.Language
import com.dreampany.tools.R
import com.dreampany.tools.data.model.question.Question
import com.dreampany.tools.databinding.ContentQuestionHomeBinding
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentQuestionHomeBinding
import com.dreampany.tools.misc.Constants
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

    override fun onStartUi(state: Bundle?) {
        initUi()
        initCategory()
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        bind = super.binding as FragmentQuestionHomeBinding
        bindStatus = bind.layoutTopStatus
        bindQuestion = bind.layoutQuestionHome

    }

    private fun initCategory(fresh: Boolean = false) {
        if (fresh) categoryItems.clear()
        if (categoryItems.isNotEmpty()) {
            return
        }
        val categories = Question.Category.values()
        categories.forEach {category->
            //categoryItems.add(PowerMenuItem(category, lang.equals(current), lang))
        }
    }
}