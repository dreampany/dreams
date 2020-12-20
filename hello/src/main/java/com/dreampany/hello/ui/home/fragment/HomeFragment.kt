package com.dreampany.hello.ui.home.fragment

import android.os.Bundle
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.hello.R
import com.dreampany.hello.databinding.RecyclerFragmentBinding
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.misc.exts.visible
import com.dreampany.hello.databinding.HomeFragmentBinding
import com.dreampany.hello.ui.vm.UserViewModel
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

    companion object {
        private val REQUEST_CAMERA = 101
    }

    private lateinit var bind: HomeFragmentBinding
    private lateinit var vm: UserViewModel

    //private lateinit var adapter: FastFeatureAdapter

    override val layoutRes: Int = R.layout.home_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        /* if (adapter.isEmpty)
             vm.loadFeatures()*/
    }

    override fun onStopUi() {
    }

    private fun initUi() {
        if (::bind.isInitialized) return
        bind = binding()
        vm = createVm(UserViewModel::class)

        bind.profile.icon.setOnSafeClickListener {
            vm.writeDummyUser()
            //vm.newUsers()
        }

        /*bind.fab.setImageResource(R.drawable.ic_photo_camera_black_48dp)
        bind.fab.visible()
        bind.fab.setOnSafeClickListener { openScanUi() }*/
        /*if (!::vm.isInitialized) {
            vm = createVm(FeatureViewModel::class)
            vm.subscribes(this, Observer { this.processResponse(it) })
        }*/
    }

    private fun initRecycler(state: Bundle?) {
        /*if (!::adapter.isInitialized) {
            adapter = FastFeatureAdapter(clickListener = { item: FeatureItem ->
                Timber.v("StationItem: %s", item.item.toString())
                openUi(item.item)
            })

            adapter.initRecycler(
                state,
                bind.layoutRecycler.recycler
            )
        }*/
    }

    /*private fun processResponse(response: Response<Type, Subtype, State, Action, List<FeatureItem>>) {
        if (response is Response.Progress) {
            if (response.progress) showProgress() else hideProgress()
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<FeatureItem>>) {
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

    private fun processResults(result: List<FeatureItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }
    }

    private fun openUi(item: Feature) {
        when (item.subtype) {
            Subtype.WIFI -> activity.open(WifisActivity::class)
            Subtype.CRYPTO -> activity.open(CoinsActivity::class)
            Subtype.RADIO -> activity.open(StationsActivity::class)
            Subtype.NOTE -> activity.open(NotesActivity::class)
            Subtype.HISTORY -> activity.open(HistoriesActivity::class)
        }
    }*/

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