package com.dreampany.tube.ui.home.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.misc.exts.visible
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.stateful.StatefulLayout
import com.dreampany.tube.R
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.databinding.HomeFragmentBinding
import com.dreampany.tube.databinding.RecyclerFragmentBinding
import com.dreampany.tube.ui.home.adapter.CategoryPagerAdapter
import com.dreampany.tube.ui.home.model.CategoryItem
import com.dreampany.tube.ui.home.vm.CategoryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 20/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class HomeFragment
@Inject constructor() : InjectFragment() {

    private lateinit var bind: HomeFragmentBinding
    private lateinit var vm: CategoryViewModel
    private lateinit var adapter: CategoryPagerAdapter

    override val layoutRes: Int = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initPager()
        //initRecycler(state)
        vm.loadCategories()
        /* if (adapter.isEmpty)
             vm.loadFeatures()*/
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (!::bind.isInitialized) {
            bind = getBinding()
            //bind.fab.setImageResource(R.drawable.ic_photo_camera_black_48dp)
            /*bind.fab.visible()
            bind.fab.setOnSafeClickListener { openScanUi() }*/
            vm = createVm(CategoryViewModel::class)
            vm.subscribes(this, Observer { this.processResponses(it) })
        }
    }

    private fun initPager() {
        if (!::adapter.isInitialized) {
            adapter = CategoryPagerAdapter(this)
        }
        bind.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<CategoryItem>>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<CategoryItem>>) {
            Timber.v("Result [%s]", response.result)
            processResults(response.result)
        }
    }

    private fun processError(error: SmartError) {
        val titleRes = if (error.hostError) R.string.title_no_internet else R.string.title_error
        val message =
            if (error.hostError) getString(R.string.message_no_internet) else error.message
        showDialogue(
            titleRes,
            messageRes = R.string.message_unknown,
            message = message,
            onPositiveClick = {

            },
            onNegativeClick = {

            }
        )
    }

    private fun processResults(result: List<CategoryItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }
    }

    private fun openScanUi() {
        /* val task = UiTask(
             Type.CAMERA,
             Subtype.DEFAULT,
             State.DEFAULT,
             Action.SCAN,
             null
         )
         open(CameraActivity::class, task, REQUEST_CAMERA)*/
    }
}