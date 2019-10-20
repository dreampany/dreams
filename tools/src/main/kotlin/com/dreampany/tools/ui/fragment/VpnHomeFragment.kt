package com.dreampany.tools.ui.fragment

import android.app.Activity
import android.content.*
import android.net.VpnService
import android.os.Bundle
import android.os.IBinder
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.ActivityScope
import com.dreampany.framework.misc.exception.EmptyException
import com.dreampany.framework.misc.exception.ExtraException
import com.dreampany.framework.misc.exception.MultiException
import com.dreampany.framework.ui.enums.UiState
import com.dreampany.framework.ui.fragment.BaseMenuFragment
import com.dreampany.tools.R
import com.dreampany.tools.ui.misc.ServerRequest
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.databinding.ContentTopStatusBinding
import com.dreampany.tools.databinding.ContentVpnHomeBinding
import com.dreampany.tools.databinding.FragmentVpnHomeBinding
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.model.ServerItem
import com.dreampany.tools.ui.vm.ServerViewModel
import com.dreampany.tools.util.TotalTraffic
import cz.kinst.jakub.view.StatefulLayout
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.core.*
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * Created by roman on 2019-10-07
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@ActivityScope
class VpnHomeFragment
@Inject constructor() : BaseMenuFragment() {

    private val START_VPN_PROFILE = 70

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var bind: FragmentVpnHomeBinding
    private lateinit var bindStatus: ContentTopStatusBinding
    private lateinit var bindVpn: ContentVpnHomeBinding

    private lateinit var vm: ServerViewModel

    private lateinit var statusReceiver: BroadcastReceiver
    private lateinit var trafficReceiver: BroadcastReceiver

    private var background: Boolean = false
    private var boundService: Boolean = false


    companion object {
        private var vpn: OpenVPNService? = null
        private var profile: VpnProfile? = null
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_vpn_home
    }

    override fun getTitleResId(): Int {
        return R.string.title_feature_vpn
    }

    override fun getScreen(): String {
        return Constants.wordHome(context!!)
    }

    override fun onStartUi(state: Bundle?) {
        initUi()
        request(state = State.RANDOM, single = true)
    }

    override fun onStopUi() {
        getParent()?.run {
            unregisterReceiver(statusReceiver)
            unregisterReceiver(trafficReceiver)
        }
    }

    override fun onResume() {
        super.onResume()
        background = false

        val intent = Intent(context, OpenVPNService::class.java)
        intent.setAction(OpenVPNService.START_SERVICE)
        boundService =
            getParent()?.bindService(intent, connection, Context.BIND_AUTO_CREATE) ?: false

        if (!VpnStatus.isVPNActive()) {
            prepareVpn()
        }
    }

    override fun onPause() {
        super.onPause()
        background = true
        if (boundService) {
            boundService = false
            getParent()?.unbindService(connection)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            START_VPN_PROFILE -> {
                VPNLaunchHelper.startOpenVpn(profile, context!!)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_action -> {
                prepareVpn()
            }
        }
    }

    private fun initUi() {
        bind = super.binding as FragmentVpnHomeBinding
        bindStatus = bind.layoutTopStatus
        bindVpn = bind.layoutVpnHome

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

        processUiState(UiState.DEFAULT)

        vm = ViewModelProviders.of(this, factory).get(ServerViewModel::class.java)
        vm.observeUiState(this, Observer { this.processUiState(it) })
        vm.observeOutput(this, Observer { this.processSingleResponse(it) })

        statusReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

            }

        }

        trafficReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

            }

        }

        getParent()?.run {
            registerReceiver(statusReceiver, IntentFilter("de.blinkt.openvpn.VPN_STATUS"))
            registerReceiver(trafficReceiver, IntentFilter(TotalTraffic.TRAFFIC_ACTION))
        }
    }

    private fun processUiState(state: UiState) {
        when (state) {
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
            UiState.CONTENT -> bind.stateful.setState(StatefulLayout.State.CONTENT)
        }
    }

    private fun processSingleResponse(response: Response<ServerItem>) {
        if (response is Response.Progress<*>) {
            val result = response as Response.Progress<*>
            vm.processProgress(result.loading)
        } else if (response is Response.Failure<*>) {
            val result = response as Response.Failure<*>
            processFailure(result.error)
        } else if (response is Response.Result<*>) {
            val result = response as Response.Result<ServerItem>
            processSingleSuccess(result.state, result.action, result.data)
        }
    }

    private fun processFailure(error: Throwable) {
        if (error is IOException || error.cause is IOException) {
            vm.updateUiState(UiState.OFFLINE)
        } else if (error is EmptyException) {
            vm.updateUiState(UiState.EMPTY)
        } else if (error is ExtraException) {
            vm.updateUiState(UiState.EXTRA)
        } else if (error is MultiException) {
            for (e in error.errors) {
                processFailure(e)
            }
        }
    }

    private fun processSingleSuccess(state: State, action: Action, uiItem: ServerItem) {
        Timber.v("Result Single Server[%s]", uiItem.item.id)
        bind.setItem(uiItem)
        processUiState(UiState.CONTENT)
    }

    private fun prepareVpn() {
        val server = bind.item?.item ?: return
        Timber.v("Preparing vpn [%s]", server.id)
        profile = loadVpn(server)
        if (profile != null) {
            startVpn(server)
        }
    }

    private fun loadVpn(server: Server): VpnProfile? {
        var data: ByteArray? = null
        try {
            data = Base64.decode(server.config, Base64.DEFAULT)
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
        val parser = ConfigParser()
        val reader = InputStreamReader(ByteArrayInputStream(data))
        try {
            parser.parseConfig(reader)
            val profile = parser.convertProfile()
            profile.mName = server.countryName
            ProfileManager.getInstance(context).addProfile(profile)
            return profile
        } catch (error: Throwable) {
            Timber.e(error)
            return null
        }
    }

    private fun startVpn(server: Server) {
        val intent = VpnService.prepare(context)
        if (intent != null) {
            Timber.v("Starting vpn [%s]", server.id)
            VpnStatus.updateStateString(
                "USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
                VpnStatus.ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT
            )
            try {
                startActivityForResult(intent, START_VPN_PROFILE)
            } catch (ane: ActivityNotFoundException) {
                // Shame on you Sony! At least one user reported that
                // an official Sony Xperia Arc S image triggers this exception
                VpnStatus.logError(R.string.no_vpn_support_image)
            }

        } else {
            onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null)
        }
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
            state = state,
            action = action,
            single = single,
            progress = progress
        )
        vm.request(request)
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            VpnHomeFragment.vpn = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as OpenVPNService.LocalBinder
            VpnHomeFragment.vpn = binder.service
        }

    }
}