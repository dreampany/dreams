package com.dreampany.nearby.ui.home.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.inject.annote.ActivityScope
import com.dreampany.framework.misc.exts.*
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.fragment.InjectFragment
import com.dreampany.nearby.R
import com.dreampany.nearby.data.enums.Action
import com.dreampany.nearby.data.enums.State
import com.dreampany.nearby.data.enums.Subtype
import com.dreampany.nearby.data.enums.Type
import com.dreampany.nearby.data.source.pref.AppPref
import com.dreampany.nearby.databinding.RecyclerFragmentBinding
import com.dreampany.nearby.ui.home.adapter.FastUserAdapter
import com.dreampany.nearby.ui.home.model.UserItem
import com.dreampany.nearby.ui.home.vm.UserViewModel
import com.dreampany.network.nearby.core.NearbyApi
import com.dreampany.stateful.StatefulLayout
import com.google.android.gms.nearby.connection.Strategy
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
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
@Inject constructor() : InjectFragment(), OnMenuItemClickListener<PowerMenuItem> {

    @Inject
    internal lateinit var pref : AppPref

    private lateinit var bind: RecyclerFragmentBinding
    private lateinit var vm: UserViewModel
    private lateinit var adapter: FastUserAdapter

    private val powerItems = mutableListOf<PowerMenuItem>()
    private var powerMenu: PowerMenu? = null

    override val layoutRes: Int = R.layout.recycler_fragment

    override val menuRes: Int = R.menu.menu_home

    override val searchMenuItemId: Int = R.id.item_search

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler(state)
        createMenuItems()

        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            // Do something
            vm.startNearby()
        }
        /* if (adapter.isEmpty)
             vm.loadFeatures()*/
    }

    override fun onStopUi() {
    }

    override fun onMenuCreated(menu: Menu) {
        getSearchMenuItem().toTint(context, R.color.material_white)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_nearby_type -> {
                toolbarRef?.let {

                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int, item: PowerMenuItem) {
        powerMenu?.setSelectedPosition(position)
        powerMenu?.dismiss()
        val type = item.tag as NearbyApi.Type
        findMenuItemById(R.id.action_nearby_type)?.setTitle(type.titleRes)
        pref.setNearbyType(type)
        vm.startNearby()
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
            vm.subscribe(this, Observer { this.processResponse(it) })
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

    private fun createMenuItems() {
        if (powerItems.isEmpty()) {

            powerItems.add(
                PowerMenuItem(
                    getString(R.string.nearby_type_ptp),
                    NearbyApi.Type.PTP == pref.getNearbyType(),
                    NearbyApi.Type.PTP
                )
            )
            powerItems.add(
                PowerMenuItem(
                    getString(R.string.nearby_type_cluster),
                    NearbyApi.Type.CLUSTER == pref.getNearbyType(),
                    NearbyApi.Type.PTP
                )
            )
            powerItems.add(
                PowerMenuItem(
                    getString(R.string.nearby_type_star),
                    NearbyApi.Type.STAR == pref.getNearbyType(),
                    NearbyApi.Type.STAR
                )
            )
        }
    }

    private fun openNearbyTypePicker(view: View) {
        powerMenu = PowerMenu.Builder(requireContext())
            .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
            .addItemList(powerItems)
            .setSelectedMenuColor(color(R.color.colorPrimary))
            .setSelectedTextColor(Color.WHITE)
            .setOnMenuItemClickListener(this)
            .setLifecycleOwner(this)
            .build()
        powerMenu?.showAsAnchorRightBottom(view)
    }

    private fun processResponse(response: Response<Type, Subtype, State, Action, UserItem>) {
        if (response is Response.Progress) {
            bind.swipe.refresh(response.progress)
        } else if (response is Response.Error) {
            processError(response.error)
        } else if (response is Response.Result<Type, Subtype, State, Action, UserItem>) {
            Timber.v("Result [%s]", response.result)
            processResult(response.result)
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

    private fun processResult(result: UserItem?) {
        if (result != null) {
            adapter.addItem(result)
        }
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

    @get:StringRes
    private val NearbyApi.Type.titleRes : Int
        get() {
            when (this) {
                NearbyApi.Type.PTP -> {
                    return R.string.nearby_type_ptp
                }
                NearbyApi.Type.CLUSTER -> {
                    return R.string.nearby_type_cluster
                }
                NearbyApi.Type.STAR -> {
                    return R.string.nearby_type_star
                }
                else -> {
                    return R.string.nearby_type_star
                }
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