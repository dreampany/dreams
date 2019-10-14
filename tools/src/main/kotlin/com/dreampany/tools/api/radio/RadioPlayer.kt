package com.dreampany.tools.api.radio

import android.content.Context
import android.os.Handler
import android.util.Log
import com.dreampany.framework.misc.AppExecutor
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.player.ExoPlayer
import com.dreampany.tools.api.player.SmartPlayer
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RadioPlayer
@Inject constructor(
    val context: Context,
    val ex: AppExecutor,
    val network: NetworkManager
) : SmartPlayer.Listener {

    val player: SmartPlayer

    init {
        player = ExoPlayer(context, network, this)
    }

    override fun onState(state: SmartPlayer.State) {

    }

    override fun onError(messageId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShoutCast(cast: ShoutCast, hls: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStream(stream: Stream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun play(url: String, name: String) {

    }

    private fun setState(state: SmartPlayer.State, audioSessionId: Int) {
        Timber.v("set state '%s'", state.name)
        if (AndroidUtil.isDebug(context)) {
            if (state == SmartPlayer.State.PLAYING) {
                ex.getUiHandler().removeCallbacks(bufferCheckRunnable)
                playerThreadHandler.post(bufferCheckRunnable)
            } else {
                playerThreadHandler.removeCallbacks(bufferCheckRunnable)
            }
        }
    }

    private val bufferCheckRunnable = object : Runnable {
        override fun run() {
            val bufferTimeMs = player.getBufferedMs()

            playerListener.onBufferedTimeUpdate(bufferTimeMs)

            if (BuildConfig.DEBUG) Log.d(TAG, String.format("buffered %d ms.", bufferTimeMs))

            playerThreadHandler.postDelayed(this, 2000)
        }
    }

}