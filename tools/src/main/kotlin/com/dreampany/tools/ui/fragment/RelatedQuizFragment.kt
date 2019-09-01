package com.dreampany.tools.ui.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.tools.R
import com.dreampany.tools.databinding.ContentRelatedQuizBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentRelatedQuizBinding
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

    override fun getLayoutId(): Int {
        return R.layout.fragment_related_quiz
    }

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }
}