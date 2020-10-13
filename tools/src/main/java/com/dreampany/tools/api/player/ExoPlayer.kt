package com.dreampany.tools.api.player

import android.content.Context
import android.net.Uri
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.R
import com.dreampany.tools.api.radio.ShoutCast
import com.dreampany.tools.api.radio.Stream
import com.dreampany.tools.misc.constants.Constants
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import okhttp3.OkHttpClient
import timber.log.Timber
import java.util.*

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExoPlayer
constructor(
    val context: Context,
    val network: NetworkManager,
    val listener: SmartPlayer.Listener
) : SmartPlayer, Player.EventListener, AnalyticsListener,
    IcyDataSource.Listener, NetworkManager.Callback {

    private var player: SimpleExoPlayer? = null

    private val meter: DefaultBandwidthMeter = DefaultBandwidthMeter()

    private var url: String? = null

    private var totalBytes: Long = 0
    private var playbackBytes: Long = 0

    private var hls: Boolean = false
    private var playingFlag: Boolean = false

    private var source: MediaSource? = null
    private var interruptedByConnectionLoss = false

    override fun onNetworks(networks: List<Network>) {
        if (!interruptedByConnectionLoss || player == null || source == null) {
            return
        }
        for (network in networks) {
            if (network.internet) {
                //interruptedByConnectionLoss = false
                //player?.prepare(source)
                //player?.playWhenReady = true
                break
            }
        }
    }

    override fun setVolume(volume: Float) {
        Timber.v("setVolume(%d)", volume)
        player?.setVolume(volume)
    }

    override fun play(http: OkHttpClient, url: String) {
        Timber.v("play - %s", url)
        if (!url.equals(this.url)) {
            playbackBytes = 0L
        }
        this.url = url
        listener.onState(SmartPlayer.State.PRE_PLAYING)
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
            player?.setAudioAttributes(AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA).build())
            //player?.addListener(this)
            player?.addAnalyticsListener(this)

        }
        hls = url.endsWith(Constants.Extension.M3U8)
        val retryTimeout = 4L
        val retryDelay = 10L

        val sourceFactory = DataSourceFactory(http, meter, this, retryTimeout, retryDelay)
        val extractorsFactory = DefaultExtractorsFactory()

        if (hls) {
            source = HlsMediaSource.Factory(sourceFactory).createMediaSource(Uri.parse(url))
        } else {
            source = ExtractorMediaSource.Factory(sourceFactory).setExtractorsFactory(extractorsFactory).createMediaSource(Uri.parse(url))
        }
        player?.prepare(source)
        player?.playWhenReady = true
        interruptedByConnectionLoss = false
        network.observe(this, true)
/*        context.registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )*/
    }

    override fun pause() {
        Timber.v("Pause. Stopping exoplayer.")
        //context.unregisterReceiver(networkReceiver)
        network.deObserve(this)
        player?.stop()
        player?.release()
        player = null
    }

    override fun stop() {
        Timber.v("Stopping exoplayer.")
        pause()

        //TODO stop recording
    }

    override fun isPlaying(): Boolean {
        val playing = player != null && playingFlag
        Timber.v("isPlaying exoplayer. $playing")
        return playing
    }

    override fun isLocal(): Boolean {
        return true
    }

    override fun getBufferedMs(): Long {
        val bufferMs = if (player != null) {
            player!!.getBufferedPosition() - player!!.getCurrentPosition()
        } else 0
        Timber.v("getBufferedMs %d", bufferMs)
        return bufferMs
    }

    override fun getAudioSessionId(): Int {
        val sessionId = if (player != null) {
            player!!.getAudioSessionId()
        } else 0
        Timber.v("getAudioSessionId %d", sessionId)
        return sessionId
    }

    override fun getTotalTransferredBytes(): Long {
        Timber.v("getTotalTransferredBytes %d", totalBytes)
        return totalBytes
    }

    override fun getCurrentPlaybackTransferredBytes(): Long {
        Timber.v("getCurrentPlaybackTransferredBytes %d", playbackBytes)
        return playbackBytes
    }

    override fun onConnected() {
        Timber.v("onStarted.")
    }

    override fun onConnectionLost() {
        Timber.v("onConnectionLost.")
    }

    override fun onConnectionLostIrrecoverably() {
        Timber.v("Connection lost irrecoverably.")

        listener.onState(SmartPlayer.State.IDLE)
        listener.onError(R.string.error_stream_reconnect_timeout)

        val resumeWithin = 60
        if (resumeWithin > 0) {
            Timber.v("Trying to resume playback within %ds.", resumeWithin)
            player?.playWhenReady = false
            interruptedByConnectionLoss = true

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (interruptedByConnectionLoss) {
                        interruptedByConnectionLoss = false
                        stop()
                        listener.onError(R.string.giving_up_resume)
                    }
                }
            }, (resumeWithin * 1000).toLong())
        } else {
            stop()
        }
    }

    override fun onShoutCast(cast: ShoutCast) {
        listener.onShoutCast(cast, false)
    }

    override fun onStream(stream: Stream) {
        listener.onStream(stream)
    }

    override fun onBytesRead(buffer: ByteArray, offset: Int, length: Int) {
        totalBytes += length
        playbackBytes += length
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        if (!interruptedByConnectionLoss) {
            stop()
            listener.onState(SmartPlayer.State.IDLE)
            listener.onError(R.string.error_play_stream)
        }
    }

    override fun onPlayerStateChanged(
        eventTime: AnalyticsListener.EventTime,
        playWhenReady: Boolean,
        playbackState: Int
    ) {
        Timber.v("onPlayerStateChanged $playWhenReady")
        playingFlag = playWhenReady
    }

    override fun onAudioSessionId(eventTime: AnalyticsListener.EventTime, audioSessionId: Int) {
        listener.onState(SmartPlayer.State.PLAYING)
    }

/*    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

        }

    }*/
}