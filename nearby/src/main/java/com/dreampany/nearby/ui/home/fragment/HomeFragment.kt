package com.dreampany.nearby.ui.home.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.refresh
import com.dreampany.framework.misc.exts.setOnSafeClickListener
import com.dreampany.framework.misc.exts.visible
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.nearby.R
import com.dreampany.nearby.data.enums.Action
import com.dreampany.nearby.data.enums.State
import com.dreampany.nearby.data.enums.Subtype
import com.dreampany.nearby.data.enums.Type
import com.dreampany.nearby.databinding.RecyclerFragmentBinding
import com.dreampany.nearby.ui.home.adapter.FastUserAdapter
import com.dreampany.nearby.ui.home.model.UserItem
import com.dreampany.nearby.ui.home.vm.UserViewModel
import com.dreampany.stateful.StatefulLayout
import kotlinx.android.synthetic.main.content_recycler.view.*
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

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: UserViewModel
    private lateinit var adapter: FastUserAdapter

    override val layoutRes: Int = R.layout.recycler_fragment

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            // Do something
            vm.startNearby()
        }
        /* if (adapter.isEmpty)
             vm.loadFeatures()*/
    }

    override fun onStopUi() {
    }

    private fun onItemPressed(view: View, item: UserItem) {
        Timber.v("Pressed $view")
        when (view.id) {
            R.id.layout -> {
                //openCoinUi(item)
            }
            else -> {

            }
        }
    }

    private fun initUi() {
        if (!::bind.isInitialized) {
            bind = getBinding()
            bind.fab.setImageResource(R.drawable.ic_photo_camera_black_48dp)
            bind.fab.visible()
            bind.fab.setOnSafeClickListener { openScanUi() }
            vm = createVm(UserViewModel::class)
            vm.subscribes(this, Observer { this.processResponses(it) })
        }

    }

    private fun initRecycler(state: Bundle?) {
        if (!::adapter.isInitialized) {
            adapter = FastUserAdapter(
                { currentPage ->
                    Timber.v("CurrentPage: %d", currentPage)
                    onRefresh()
                }, this::onItemPressed
            )

            adapter.initRecycler(
                state,
                bind.layoutRecycler.recycler
            )
        }
    }

    private fun processResponses(response: Response<Type, Subtype, State, Action, List<UserItem>>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, List<UserItem>>) {
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

    private fun processResults(result: List<UserItem>?) {
        if (result != null) {
            adapter.addItems(result)
        }

        if (adapter.isEmpty) {
            bind.stateful.setState(StatefulLayout.State.EMPTY)
        } else {
            bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
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