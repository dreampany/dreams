package com.dreampany.tools.api.player

import android.content.Context
import com.dreampany.tools.misc.Constants
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExoPlayer
@Inject constructor(val context: Context) : SmartPlayer, Player.EventListener, AnalyticsListener {

    private var player: SimpleExoPlayer? = null
    private var listener: SmartPlayer.Listener? = null

    private val meter: DefaultBandwidthMeter = DefaultBandwidthMeter()

    private var url: String? = null

    private var totalBytes: Long = 0
    private var playbackBytes: Long = 0

    private var hls: Boolean = false
    private var playingFlag: Boolean = false

    override fun setListener(listener: SmartPlayer.Listener) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setVolume(volume: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun play(http: OkHttpClient, url: String) {
        if (!url.equals(this.url)) {
            playbackBytes = 0L
        }
        this.url = url
        listener?.onState(SmartPlayer.State.PRE_PLAYING)
        player?.stop()
        if (player == null) {
            val factory = AdaptiveTrackSelection.Factory()
            val track = DefaultTrackSelector(factory)
            val control = DefaultLoadControl.Builder().createDefaultLoadControl()
            player = ExoPlayerFactory.newSimpleInstance(
                context,
                DefaultRenderersFactory(context),
                track,
                control
            )
            player?.setAudioAttributes(
                AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA).build()
            )
            player?.addListener(this)
            player?.addAnalyticsListener(this)

        }
        hls = url.endsWith(Constants.Extension.M3U8)
        //val sourceFactory
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isPlaying(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLocal(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBufferedMs(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAudioSessionId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTotalTransferredBytes(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentPlaybackTransferredBytes(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}