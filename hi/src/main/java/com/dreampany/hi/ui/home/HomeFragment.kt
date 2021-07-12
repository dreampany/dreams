package com.dreampany.hi.ui.home

import android.os.Bundle
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.hi.R
import com.dreampany.hi.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by roman on 7/12/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    override val layoutRes: Int
        get() = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {

    }

    override fun onStopUi() {
    }
}