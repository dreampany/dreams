package com.dreampany.tools.api.radio

import android.content.Context
import androidx.annotation.StringRes
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.player.ExoPlayer
import com.dreampany.tools.api.player.SmartPlayer
import com.dreampany.tools.data.source.pref.RadioPref
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RadioPlayer
@Inject constructor(
    private val context: Context,
    private val ex: AppExecutor,
    private val radioPref: RadioPref,
    private val network: NetworkManager,
    private val pool: ConnectionPool
) : SmartPlayer.Listener {

    interface Listener {
        fun onState(state: SmartPlayer.State, audioSessionId: Int)
        fun onError(@StringRes messageId: Int)
        fun onBufferedTimeUpdate(bufferedMs: Long)
        fun onShoutCast(cast: ShoutCast, hls: Boolean)
        fun onStream(stream: Stream)
    }

    private val player: SmartPlayer
    private lateinit var listener: Listener

    private lateinit var name: String

    private var state: SmartPlayer.State
    private var stream: Stream? = null

    init {
        player = ExoPlayer(context, network, this)
        state = SmartPlayer.State.IDLE
    }

    override fun onState(state: SmartPlayer.State) {
        setState(state, player.getAudioSessionId())
    }

    override fun onError(messageId: Int) {
        stop()
        listener.onError(messageId)
    }

    override fun onShoutCast(cast: ShoutCast, hls: Boolean) {
        listener.onShoutCast(cast, hls)
    }

    override fun onStream(stream: Stream) {
        this.stream = stream
        listener.onStream(stream)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setVolume(volume: Float) {
        player.setVolume(volume)
    }

    fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    fun getState(): SmartPlayer.State {
        return state
    }

    fun play(url: String, name: String) {
        setState(SmartPlayer.State.PRE_PLAYING, -1)

        this.name = name

        val connectTimeout = 4L
        val readTimeout = 10L

        val http = newHttpClient(connectTimeout, readTimeout)

        ex.getUiHandler().post(kotlinx.coroutines.Runnable {
            player.play(http, url)
        })
    }

    fun pause() {
        ex.getUiHandler().post(kotlinx.coroutines.Runnable {
            val sessionId = player.getAudioSessionId()
            player.pause()

            if (AndroidUtil.isDebug(context)) {
                ex.getUiHandler().removeCallbacks(bufferCheckRunnable)
            }

            setState(SmartPlayer.State.PAUSED, sessionId)
        })
    }

    fun stop() {
        if (state == SmartPlayer.State.IDLE) return
        ex.getUiHandler().post(kotlinx.coroutines.Runnable {
            val audioSessionId = player.getAudioSessionId()
            player.stop()

            if (AndroidUtil.isDebug(context)) {
                ex.getUiHandler().removeCallbacks(bufferCheckRunnable)
            }

            setState(SmartPlayer.State.IDLE, audioSessionId)
        })
    }

    fun destroy() {
        stop()
    }

    private fun setState(state: SmartPlayer.State, audioSessionId: Int) {
        Timber.v("set state '%s'", state.name)
        if (AndroidUtil.isDebug(context)) {
            if (state == SmartPlayer.State.PLAYING) {
                ex.getUiHandler().removeCallbacks(bufferCheckRunnable)
                ex.getUiHandler().post(bufferCheckRunnable)
            } else {
                ex.getUiHandler().removeCallbacks(bufferCheckRunnable)
            }
        }
        this.state = state
        listener.onState(state, audioSessionId)
    }

    private val bufferCheckRunnable = object : Runnable {
        override fun run() {
            val bufferTimeMs = player.getBufferedMs()

            listener.onBufferedTimeUpdate(bufferTimeMs)

            Timber.v("buffered %d ms.", bufferTimeMs)
            ex.getUiHandler().postDelayed(this, 2000)
        }
    }

    private fun newHttpClient(connectTimeout: Long, readTimeout: Long): OkHttpClient {
        val builder = OkHttpClient.Builder().connectionPool(pool)
        builder.connectTimeout(connectTimeout, TimeUnit.SECONDS)
        builder.readTimeout(readTimeout, TimeUnit.SECONDS)
        return builder.build()
    }

}