package com.dreampany.tube.ui.home.fragment

import android.os.Bundle
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.constant.Constant
import com.dreampany.framework.misc.exts.packageName
import com.dreampany.framework.misc.exts.versionCode
import com.dreampany.framework.misc.exts.versionName
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.tube.R
import com.dreampany.tube.data.enums.Action
import com.dreampany.tube.data.enums.State
import com.dreampany.tube.data.enums.Subtype
import com.dreampany.tube.data.enums.Type
import com.dreampany.tube.data.source.pref.Prefs
import com.dreampany.tube.databinding.HomeFragmentBinding
import com.dreampany.tube.ui.home.adapter.PageAdapter
import com.dreampany.tube.ui.model.PageItem
import com.dreampany.tube.ui.vm.PageViewModel
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

    @Inject
    internal lateinit var pref: Prefs

    private lateinit var bind: HomeFragmentBinding
    private lateinit var vm: PageViewModel
    private lateinit var adapter: PageAdapter

    override val layoutRes: Int = R.layout.home_fragment

    override val params: Map<String, Map<String, Any>?>
        get() {
            val params = HashMap<String, HashMap<String, Any>?>()

            val param = HashMap<String, Any>()
            param.put(Constant.Param.PACKAGE_NAME, parentRef.packageName)
            param.put(Constant.Param.VERSION_CODE, parentRef.versionCode)
            param.put(Constant.Param.VERSION_NAME, parentRef.versionName)
            param.put(Constant.Param.SCREEN, "HomeFragment")

            params.put(Constant.Event.fragment(context), param)
            return params
        }

    override fun onStartUi(state: Bundle?) {
        initUi()
        initPager()
        //vm.readCache()
    }

    override fun onStopUi() {
    }

    override fun onStart() {
        super.onStart()
        updatePages()
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = getBinding()
        vm = createVm(PageViewModel::class)

        vm.subscribes(this, { this.processResponses(it) })
    }

    private fun initPager() {
        if (::adapter.isInitialized) return
        adapter = PageAdapter(this)
        bind.pager.adapter = adapter
        TabLayoutMediator(
            bind.tabs,
            bind.pager,
            { tab, position ->
                tab.text = adapter.getTitle(position)
            }).attach()
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<PageItem>>) {
        if (response is Response.Progress) {
            //bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<PageItem>>) {
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

    private fun processResults(result: List<PageItem>?) {
        if (result != null) {
            if (!adapter.isEmpty) {
                adapter.clear()
            }
            adapter.addItems(result)
        }
    }

    private fun updatePages() {
        val pages = pref.pages ?: return
        if (adapter.hasUpdate(pages)) {
            vm.readsCache()
        } else {
            adapter.notifyDataSetChanged()
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