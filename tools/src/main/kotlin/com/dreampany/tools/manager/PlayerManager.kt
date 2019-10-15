package com.dreampany.tools.manager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.dreampany.tools.service.player.PlayerService
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class PlayerManager
@Inject constructor(
    val context: Context
) : ServiceConnection {

    private var bound: Boolean = false
  //  private var service: IPlayerService? = null

    override fun onServiceDisconnected(name: ComponentName) {
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
    }

    fun bind() {
        if (bound) return
        val intent = Intent(context, PlayerService::class.java)
        context.startService(intent)
        context.bindService(intent, this, Context.BIND_AUTO_CREATE)
        bound = true
        Timber.v("Bind Player Service")
    }

    fun debind() {
        try {
            context.unbindService(this)
        } catch (error: Throwable) {
            Timber.e(error)
        }
        bound = false
        Timber.v("Debind Player Service")
    }

    fun stop() {
        debind()
        try {
            val intent = Intent(context, PlayerService::class.java)
            context.stopService(intent)
        } catch (error: Throwable) {
            Timber.e(error)
        }
        Timber.v("Stopping Player Service")
    }
}