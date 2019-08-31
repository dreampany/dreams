package com.dreampany.tools.ui.fragment

import android.os.Bundle
import android.view.View
import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.frame.ui.listener.OnUiItemClickListener
import com.dreampany.tools.ui.model.QuizItem
import javax.inject.Inject

/**
 * Created by roman on 2019-08-31
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class RelatedQuizFragment
@Inject constructor() :
    BaseMenuFragment(),
    OnUiItemClickListener<QuizItem?, Action?> {
    override fun onClick(view: View, item: QuizItem?, action: Action?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLongClick(view: View, item: QuizItem?, action: Action?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartUi(state: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStopUi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}