package com.dreampany.tools.manager

import android.app.Activity
import android.content.*
import android.net.VpnService
import android.os.IBinder
import android.util.Base64
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Server
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.service.PlayerService
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.core.*
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-27
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class VpnManager
@Inject constructor(
    val context: Context
) : ServiceConnection {

    interface Callback {
        fun checkVpnProfile(requestCode: Int, intent: Intent)
        fun resultOfVpn(requestCode: Int, resultCode: Int, intent: Intent?)
    }

    private var bound: Boolean = false
    private var profile: VpnProfile? = null
    private var service: OpenVPNService? = null

    private var callback: Callback? = null

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Timber.v("Service came online")
        this.service = (service as OpenVPNService.LocalBinder).getService()
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Timber.v("Vpn Service offline")
        debind()
        service = null
    }

    fun bind() {
        if (bound) return
        val intent = Intent(context, PlayerService::class.java)
        context.startService(intent)
        context.bindService(intent, this, Context.BIND_AUTO_CREATE)
        bound = true
        Timber.v("Bind OpenVpn Service")
    }

    fun debind() {
        try {
            context.unbindService(this)
        } catch (error: Throwable) {
            Timber.e(error)
        }
        bound = false
        Timber.v("Debind OpenVpn Service")
    }

    fun destroy() {
        debind()
        try {
            val intent = Intent(context, OpenVPNService::class.java)
            context.stopService(intent)
        } catch (error: Throwable) {
            Timber.e(error)
        }
        Timber.v("Stopping Player Service")
    }

    fun setCallback(callback: Callback? = null) {
        this.callback = callback
    }

    fun start(server: Server) {
        profile = loadProfile(server)
        if (profile != null) {
            startVpn(server)
        }
    }

    fun startVpn(server: Server) {
        val intent = VpnService.prepare(context)
        if (intent != null) {
            Timber.v("Starting vpn [%s]", server.id)
            VpnStatus.updateStateString(
                "USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
                VpnStatus.ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT
            )
            try {
               callback?.checkVpnProfile(Constants.RequestCode.Vpn.START_VPN_PROFILE, intent)
            } catch (ane: ActivityNotFoundException) {
                // Shame on you Sony! At least one user reported that
                // an official Sony Xperia Arc S image triggers this exception
                VpnStatus.logError(R.string.no_vpn_support_image)
            }

        } else {
            callback?.resultOfVpn(Constants.RequestCode.Vpn.START_VPN_PROFILE,  Activity.RESULT_OK, intent)
        }
    }

    fun startOpenVpn() {
        VPNLaunchHelper.startOpenVpn(profile, context)
    }

    private fun loadProfile(server: Server): VpnProfile? {
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
}