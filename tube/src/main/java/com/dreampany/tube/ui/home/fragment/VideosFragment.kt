package com.dreampany.tube.ui.home.fragment

import android.os.Bundle
import com.dreampany.framework.inject.annote.FragmentScope
import com.dreampany.framework.ui.fragment.InjectFragment
import javax.inject.Inject
import com.dreampany.tube.R
import com.dreampany.tube.data.model.Category
import com.dreampany.tube.databinding.VideosFragmentBinding
import com.dreampany.tube.ui.home.vm.VideoViewModel

/**
 * Created by roman on 30/6/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@FragmentScope
class VideosFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: VideosFragmentBinding
    private lateinit var vm: VideoViewModel
    private lateinit var input: Category

    override val layoutRes: Int = R.layout.videos_fragment

    override fun onStartUi(state: Bundle?) {
        //initUi()
        //initRecycler(state)
    }

    override fun onStopUi() {
    }
}