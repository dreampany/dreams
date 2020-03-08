package com.dreampany.tools.ui.fragment.lock

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.common.misc.extension.hasOverlayPermission
import com.dreampany.common.misc.extension.hasUsagePermission
import com.dreampany.common.misc.extension.packageName
import com.dreampany.framework.api.service.ServiceManager
import com.dreampany.framework.api.session.SessionManager
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.injector.annote.ActivityScope
import com.dreampany.framework.ui.adapter.SmartAdapter
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.listener.OnVerticalScrollListener
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.ViewUtil
import com.dreampany.lockui.ui.activity.PinActivity
import com.dreampany.tools.R
import com.dreampany.tools.data.source.pref.LockPref
import com.dreampany.tools.databinding.ContentRecyclerBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.FragmentLockHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.service.AppService
import com.dreampany.tools.ui.adapter.AppAdapter
import com.dreampany.tools.ui.model.AppItem
import com.dreampany.tools.ui.request.AppRequest
import com.dreampany.tools.ui.vm.AppViewModel
import cz.kinst.jakub.view.StatefulLayout
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by roman on 1/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class LockHomeFragment
@Inject constructor() :
    BaseMenuFragment(),
    SmartAdapter.OnUiItemClickListener<AppItem, Action> {

    private val REQUEST_CODE_USAGE = 101
    private val REQUEST_CODE_OVERLAY = 102
    private val REQUEST_CODE_SET_LOCK = 103
    private val REQUEST_CODE_CHECK_LOCK = 104

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    @Inject
    internal lateinit var lockPref: LockPref

    @Inject
    internal lateinit var session: SessionManager

    @Inject
    internal lateinit var service: ServiceManager

    private lateinit var bind: FragmentLockHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindRecycler: ContentRecyclerBinding

    private lateinit var vm: AppViewModel
    private lateinit var adapter: AppAdapter
    private lateinit var scroller: OnVerticalScrollListener

    @LayoutRes
    override fun getLayoutId(): Int = R.layout.fragment_lock_home

    override fun onStartUi(state: Bundle?) {
        initUi()
        initRecycler()

        session.track()
        initTitleSubtitle()
        loadUi()
    }

    override fun onStopUi() {
        vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_USAGE -> {
                Timber.v("Result Code %d", resultCode)
                if (context.hasOverlayPermission().not()) {
                    requestOverlayPermission()
                } else {
                    requestLockUi()
                }
            }
            REQUEST_CODE_OVERLAY -> {
                Timber.v("Result Code %d", resultCode)
                requestLockUi()
            }
            REQUEST_CODE_SET_LOCK -> {
                if (resultCode != PinActivity.RESULT_BACK_PRESSED) {
                    data?.getStringExtra(PinActivity.KEY_PIN)?.run {
                        lockPref.setPin(this)
                        lockPref.commitServicePermitted()
                        context?.run {
                            service.openService(AppService.lockIntent(this))
                        }
                    }
                }
            }
            REQUEST_CODE_CHECK_LOCK -> {
                if (resultCode != PinActivity.RESULT_BACK_PRESSED) {
                    context?.run {
                        service.openService(AppService.lockIntent(this))
                    }
                }
            }
        }
    }

    override fun onUiItemClick(view: View, item: AppItem, action: Action) {
        when (action) {
            Action.OPEN -> {
                context?.run {
                    AndroidUtil.openApplication(this, item.item.id)
                }
            }
            Action.LOCK -> {
                request(id = item.item.id, action = Action.LOCK, single = true, lockStatus = true)
            }
        }
    }

    override fun onUiItemLongClick(view: View, item: AppItem, action: Action) {


    }

    private fun initTitleSubtitle() {
        setTitle(R.string.title_app)
        val subtitle = getString(R.string.subtitle_app, adapter.itemCount)
        setSubtitle(subtitle)
    }

    private fun initUi() {
        bind = super.binding as FragmentLockHomeBinding
        bindStatus = bind.layoutTopStatus
        bindRecycler = bind.layoutRecycler

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bind.fab.setOnClickListener(this)

        vm = ViewModelProvider(this, factory).get(AppViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutputs(this, Observer { this.processMultipleResponse(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        if (context.hasUsagePermission().not()
        ) {
            requestUsagePermission()
        } else if (context.hasOverlayPermission().not()) {
            requestOverlayPermission()
        } else {
            requestLockUi()
        }
    }

    private fun initRecycler() {
        bind.setItems(ObservableArrayList<Any>())
        adapter = AppAdapter(this)
        adapter.setStickyHeaders(false)
        scroller = object : OnVerticalScrollListener() {}
        ViewUtil.setRecycler(
            adapter,
            bindRecycler.recycler,
            SmoothScrollGridLayoutManager(context!!, adapter.getSpanCount()),
            FlexibleItemDecoration(context!!)
                .addItemViewType(R.layout.item_lock_app, adapter.getItemOffset())
                .withEdge(true),
            null,
            scroller,
            null
        )
    }

    private fun loadUi() {
        request(progress = true, lockStatus = true)
    }

    private fun requestUsagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                REQUEST_CODE_USAGE
            )
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.packageName())
            )
            startActivityForResult(intent, REQUEST_CODE_OVERLAY)
        } else {
            TODO("VERSION.SDK_INT < M")
        }

    }

    private fun requestLockUi() {
        if (lockPref.hasPin()) {
            context?.run {
                startActivityForResult(
                    PinActivity.checkIntent(this, lockPref.getPin()),
                    REQUEST_CODE_CHECK_LOCK
                )
            }
        } else {
            context?.run {
                startActivityForResult(
                    PinActivity.setIntent(this),
                    REQUEST_CODE_SET_LOCK
                )
            }
        }
    }

    private fun request(
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        important: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN,
        id: String? = Constants.Default.NULL,
        lockStatus: Boolean = Constants.Default.BOOLEAN

    ) {
        val request = AppRequest(
            type = Type.APP,
            action = action,
            single = single,
            important = important,
            progress = progress,
            id = id,
            lockStatus = lockStatus
        )
        vm.request(request)
    }

    private fun processUiState(response: Response.UiResponse) {
        Timber.v("UiState %s", response.uiState.name)
        when (response.uiState) {
            UiState.DEFAULT -> bind.stateful.setState(UiState.DEFAULT.name)
            UiState.SHOW_PROGRESS -> if (!bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(true)
            }
            UiState.HIDE_PROGRESS -> if (bind.layoutRefresh.isRefreshing()) {
                bind.layoutRefresh.setRefreshing(false)
            }
            UiState.OFFLINE -> bindStatus.layoutExpandable.expand()
            UiState.ONLINE -> bindStatus.layoutExpandable.collapse()
            UiState.EXTRA -> {
                response.uiState = if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT
                processUiState(response)
            }
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
                initTitleSubtitle()
            }
        }
    }

    fun processMultipleResponse(response: Response<List<AppItem>>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<List<AppItem>>
            processSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSingleResponse(response: Response<AppItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(
                state = result.state,
                action = result.action,
                loading = result.loading
            )
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(state = result.state, action = result.action, error = result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<AppItem>
            processSingleSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSuccess(state: State, action: Action, items: List<AppItem>) {
        Timber.v("Result Type[%s] Size[%s]", action.name, items.size)
        adapter.addItems(items)
        ex.postToUi(Runnable {
            vm.updateUiState(state = state, action = action, uiState = UiState.EXTRA)
        }, 500L)
    }

    private fun processSingleSuccess(state: State, action: Action, item: AppItem) {
        Timber.v("Result Single App[%s]", item.item.id)
        adapter.addItem(item)
    }
}