package com.dreampany.radio.manager

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.dreampany.radio.api.radiobrowser.ShoutCast
import com.dreampany.radio.api.radiobrowser.Stream
import com.dreampany.radio.data.model.Station
import com.dreampany.radio.service.RadioPlayerService
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class RadioPlayerManager
@Inject constructor(
    val context: Context
) : ServiceConnection {

    private var bound: Boolean = false
    private var service: RadioPlayerService? = null

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        Timber.v("Service came online")
        this.service = (service as RadioPlayerService.ServiceBinder).getService()
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Timber.v("Service offline")
        debind()
        service = null
    }

    fun bind() {
        if (bound) return
        val intent = Intent(context, RadioPlayerService::class.java)
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

    fun destroy() {
        debind()
        try {
            val intent = Intent(context, RadioPlayerService::class.java)
            context.stopService(intent)
        } catch (error: Throwable) {
            Timber.e(error)
        }
        Timber.v("Stopping Player Service")
    }

    fun isPlaying() : Boolean {
        return service?.isPlaying() ?: false
    }

    fun stop () {
        service?.stop()
    }

    fun play(station: Station) {
        service?.play(station)
    }

    fun pause() {
        service?.pause()
    }

    fun resume() {
        service?.resume()
    }

    fun getStation() : Station? {
        return service?.getStation()
    }

    fun getCast() : ShoutCast? {
        return service?.getCast()
    }

    fun getStream() : Stream? {
        return service?.getStream()
    }
}