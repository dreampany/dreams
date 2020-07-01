package com.dreampany.tube.ui.home.fragment

import android.os.Bundle
import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.framework.ui.fragment.InjectFragment
import javax.inject.Inject
import com.dreampany.tube.R

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class VideosFragment
@Inject constructor() : InjectFragment() {

    override val layoutRes: Int = R.layout.videos_fragment

    override fun onStartUi(state: Bundle?) {
        //initUi()
        //initRecycler(state)
    }

    override fun onStopUi() {
    }
}