package com.dreampany.tools.api.player

import androidx.annotation.StringRes
import com.dreampany.tools.api.radiobrowser.ShoutCast
import com.dreampany.tools.api.radiobrowser.Stream
import okhttp3.OkHttpClient

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface SmartPlayer : Recorder {

    enum class State {
        IDLE, PRE_PLAYING, PLAYING, PAUSED
    }

    interface Listener {
        fun onState(state: SmartPlayer.State)
        fun onError(@StringRes messageId: Int)
        fun onShoutCast(cast: ShoutCast, hls: Boolean)
        fun onStream(stream: Stream)
    }

    fun setVolume(volume: Float)

    fun play(http: OkHttpClient, url: String)

    fun pause()

    fun stop()

    fun isPlaying(): Boolean

    fun isLocal(): Boolean

    fun getBufferedMs(): Long

    fun getAudioSessionId(): Int

    fun getTotalTransferredBytes(): Long

    fun getCurrentPlaybackTransferredBytes(): Long
}