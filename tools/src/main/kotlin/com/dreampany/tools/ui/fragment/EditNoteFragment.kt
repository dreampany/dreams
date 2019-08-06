package com.dreampany.tools.ui.fragment

import android.os.Bundle
import com.dreampany.frame.misc.ActivityScope
import com.dreampany.frame.ui.fragment.BaseMenuFragment
import com.dreampany.tools.R
import javax.inject.Inject

/**
 * Created by Roman-372 on 8/6/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class EditNoteFragment @Inject constructor() :
    BaseMenuFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_edit_note
    }

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }
}