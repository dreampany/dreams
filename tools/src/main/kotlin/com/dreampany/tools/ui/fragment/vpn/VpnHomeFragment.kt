package com.dreampany.tools.ui.fragment.vpn

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.framework.ui.model.UiTask
import com.dreampany.framework.util.ColorUtil
import com.dreampany.framework.util.MenuTint
import com.dreampany.framework.util.ViewUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.ServerMapper
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.databinding.ContentServerSummaryBinding
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.ContentVpnHomeBinding
import com.dreampany.tools.databinding.FragmentVpnHomeBinding
import com.dreampany.tools.manager.VpnManager
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.ToolsActivity
import com.dreampany.tools.ui.misc.ServerRequest
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.tools.ui.vm.ServerViewModel
import cz.kinst.jakub.view.StatefulLayout
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class VpnHomeFragment
@Inject constructor() : BaseMenuFragment(), VpnManager.Callback {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory
    @Inject
    internal lateinit var vpn: VpnManager
    @Inject
    internal lateinit var mapper: ServerMapper

    private lateinit var bind: FragmentVpnHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindVpn: ContentVpnHomeBinding
    private lateinit var bindServer: ContentServerSummaryBinding

    private lateinit var vm: ServerViewModel

    private var server: Server? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_vpn_home
    }

    override fun getMenuId(): Int {
        return R.menu.menu_vpn_home
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_vpn
    }

    override fun getScreen(): String {
        return Constants.vpnHome(context!!)
    }

    override fun onMenuCreated(menu: Menu, inflater: MenuInflater) {
        super.onMenuCreated(menu, inflater)

        val serverItem = findMenuItemById(R.id.item_servers)
        MenuTint.colorMenuItem(
            ColorUtil.getColor(context!!, R.color.material_white),
            null, serverItem
        )
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        resolveUi()
        vpn.setCallback(this)
        vm.updateUiState(uiState = UiState.EMPTY)
        request(state = State.RANDOM, single = true, progress = true)
    }

    override fun onStopUi() {

    }

    override fun onResume() {
        super.onResume()
        vpn.bind()
        vpn.resolveVpn()
    }

    override fun onPause() {
        vpn.debind()
        super.onPause()
    }

    override fun onRefresh() {
        super.onRefresh()
        if (server == null) {
            request(state = State.RANDOM, single = true, progress = true)
        } else {
            vm.updateUiState(uiState = UiState.HIDE_PROGRESS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!isOkay(resultCode)) {
            return
        }
        when (requestCode) {
            Constants.RequestCode.Vpn.START_VPN_PROFILE -> {
                vpn.startOpenVpn()
            }
            Constants.RequestCode.Vpn.OPEN_SERVER -> {
                data?.run {
                    val task = getCurrentTask<UiTask<Server>>(this)
                    task?.run {
                        task.input?.run {
                            Timber.v("Selected Server %s", this.id)
                            mapper.setServer(this)
                            server = this
                            resolveUi()
                            vpn.start(this)
                        }

                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_servers -> {
                openServersUi()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_action -> {
                server?.let {
                    vpn.toggle(it)
                }
            }
        }
    }

    override fun checkVpnProfile(requestCode: Int, intent: Intent) {
        startActivityForResult(intent, Constants.RequestCode.Vpn.START_VPN_PROFILE)
    }

    override fun resultOfVpn(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult(requestCode, resultCode, data)
    }

    override fun onStarting(server: Server) {
        if (isVisible)
        bindVpn.buttonAction.setButtonColor(
            ColorUtil.getColor(context!!, R.color.material_yellow700)
        )
    }

    override fun onStarted(server: Server) {
        if (isVisible)
        bindVpn.buttonAction.setText(R.string.disconnect)
        bindVpn.buttonAction.setButtonColor(
            ColorUtil.getColor(
                context!!,
                R.color.material_green700
            )
        )
    }

    override fun onStopped(server: Server) {
        if (isVisible)
        bindVpn.buttonAction.setText(R.string.connect)
        bindVpn.buttonAction.setButtonColor(ColorUtil.getColor(context!!, R.color.material_red700))
        bindVpn.viewLog.text = null
    }

    override fun onLog(log: String?) {
        bindVpn.viewLog.text = log
    }

    private fun initUi() {
        bind = super.binding as FragmentVpnHomeBinding
        bindStatus = bind.layoutTopStatus
        bindVpn = bind.layoutVpnHome
        bindServer = bindVpn.layoutServerSummary

        ViewUtil.setSwipe(bind.layoutRefresh, this)
        bindVpn.buttonAction.setOnClickListener(this)

        bind.stateful.setStateView(
            UiState.DEFAULT.name,
            LayoutInflater.from(context).inflate(R.layout.item_default, null)
        )
        bind.stateful.setStateView(
            UiState.SEARCH.name,
            LayoutInflater.from(context).inflate(R.layout.item_search, null)
        )
        bind.stateful.setStateView(
            UiState.EMPTY.name,
            LayoutInflater.from(context).inflate(R.layout.item_empty, null)
        )

        vm = ViewModelProvider(this, factory).get(ServerViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

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
            //UiState.EXTRA -> processUiState(if (adapter.isEmpty()) UiState.EMPTY else UiState.CONTENT)
            UiState.SEARCH -> bind.stateful.setState(UiState.SEARCH.name)
            UiState.EMPTY -> bind.stateful.setState(UiState.EMPTY.name)
            UiState.ERROR -> {
            }
            UiState.CONTENT -> {
                bind.stateful.setState(StatefulLayout.State.CONTENT)
            }
        }
    }

    private fun processSingleResponse(response: Response<ServerItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.state, result.action, result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            vm.processFailure(result.state, result.action, result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<ServerItem>
            processSingleSuccess(result.state, result.action, result.data)
        }
    }

    private fun processSingleSuccess(state: State, action: Action, uiItem: ServerItem) {
        Timber.v("Result Single Server[%s]", uiItem.item.id)
        bind.setItem(uiItem)
        server = uiItem.item
        resolveUi()
        vm.updateUiState(uiState = UiState.CONTENT)

    }

    private fun resolveUi() {
        bindVpn.buttonAction.isEnabled = server != null
        if (server != null) {
            server!!.countryCode?.run {
                bindServer.viewFlag.setCountryCode(this)
            }
            var labelTextRes = R.string.low
            var labelColorRes = R.color.material_red500
            if (server!!.quality == Quality.MEDIUM) {
                labelTextRes = R.string.medium
                labelColorRes = R.color.material_yellow500
            }
            if (server!!.quality == Quality.HIGH) {
                labelTextRes = R.string.high
                labelColorRes = R.color.material_green500
            }

            bindServer.labelType.setPrimaryText(labelTextRes)
            bindServer.labelType.setTriangleBackgroundColorResource(labelColorRes)

            bindServer.viewCountryName.text = server!!.countryName
            bindServer.viewCity.text = getString(R.string.vpn_city, server!!.city)
            bindServer.viewIp.text = getString(R.string.vpn_ip, server!!.id)
            bindServer.viewSessions.text = getString(R.string.vpn_sessions, server!!.sessions)
            bindVpn.viewLog.text = vpn.lastLog()

            bindServer.viewFlag.visibility = View.VISIBLE
            bindServer.labelType.visibility = View.VISIBLE
            bindServer.viewCountryName.visibility = View.VISIBLE
            //bindServer.viewCity.visibility = View.VISIBLE
            bindServer.viewIp.visibility = View.VISIBLE
            bindServer.viewSessions.visibility = View.VISIBLE
            bindVpn.buttonAction.visibility = View.VISIBLE
            bindServer.layoutServerSummary.visibility = View.VISIBLE
        } else {
            bindServer.viewFlag.visibility = View.INVISIBLE
            bindServer.labelType.visibility = View.INVISIBLE
            bindServer.viewCountryName.visibility = View.INVISIBLE
            bindServer.viewCity.visibility = View.INVISIBLE
            bindServer.viewIp.visibility = View.INVISIBLE
            bindServer.viewSessions.visibility = View.INVISIBLE
            bindVpn.buttonAction.visibility = View.INVISIBLE
            bindServer.layoutServerSummary.visibility = View.INVISIBLE
        }
    }

    private fun openServersUi() {
        val task = UiTask<Server>(
            type = Type.SERVER,
            state = State.LIST,
            action = Action.OPEN
        )
        openActivity(ToolsActivity::class.java, task, Constants.RequestCode.Vpn.OPEN_SERVER)
    }

    private fun request(
        id: String? = Constants.Default.NULL,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        single: Boolean = Constants.Default.BOOLEAN,
        progress: Boolean = Constants.Default.BOOLEAN
    ) {
        val request = ServerRequest(
            id = id,
            type = Type.SERVER,
            subtype = Subtype.DEFAULT,
            state = state,
            action = action,
            single = single,
            progress = progress
        )
        vm.request(request)
    }
}