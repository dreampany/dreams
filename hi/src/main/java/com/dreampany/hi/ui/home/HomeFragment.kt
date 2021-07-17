package com.dreampany.hi.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.common.ui.fragment.BaseFragment
import com.dreampany.hi.R
import com.dreampany.hi.databinding.HomeFragmentBinding
import com.dreampany.hi.ui.vm.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by roman on 7/12/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    private val vm: UserViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {
        vm.registerNearby()
        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            vm.nearbyUsers()
        }
    }

    override fun onStopUi() {
        vm.unregisterNearby()
    }
}