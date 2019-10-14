package com.dreampany.tools.api.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import com.dreampany.network.data.model.Network
import com.dreampany.network.manager.NetworkManager
import com.dreampany.tools.api.radio.ShoutCast
import com.dreampany.tools.api.radio.Stream
import com.dreampany.tools.misc.Constants
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
import javax.inject.Inject

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ExoPlayer
@Inject constructor(
    val context: Context,
    val network: NetworkManager
) : SmartPlayer, Player.EventListener, AnalyticsListener,
    IcyDataSource.Listener, NetworkManager.Callback {

    private var player: SimpleExoPlayer? = null
    private var listener: SmartPlayer.Listener? = null

    private val meter: DefaultBandwidthMeter = DefaultBandwidthMeter()

    private var url: String? = null

    private var totalBytes: Long = 0
    private var playbackBytes: Long = 0

    private var hls: Boolean = false
    private var playingFlag: Boolean = false

    private var source: MediaSource? = null
    private var interruptedByConnectionLoss = false

    override fun setListener(listener: SmartPlayer.Listener) {
        this.listener = listener
    }

    override fun setVolume(volume: Float) {
        player?.setVolume(volume)
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
        val retryTimeout = 4L
        val retryDelay = 10L

        val sourceFactory = DataSourceFactory(http, meter, this, retryTimeout, retryDelay)
        val extractorsFactory = DefaultExtractorsFactory()

        if (!hls) {
            source =
                ExtractorMediaSource.Factory(sourceFactory).setExtractorsFactory(extractorsFactory)
                    .createMediaSource(
                        Uri.parse(url)
                    )
        } else {
            source = HlsMediaSource.Factory(sourceFactory).createMediaSource(Uri.parse(url))
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
        return player != null && playingFlag
    }

    override fun isLocal(): Boolean {
        return true
    }

    override fun getBufferedMs(): Long {
        return if (player != null) {
            player!!.getBufferedPosition() - player!!.getCurrentPosition()
        } else 0
    }

    override fun getAudioSessionId(): Int {
        return if (player != null) {
            player!!.getAudioSessionId()
        } else 0
    }

    override fun getTotalTransferredBytes(): Long {
        return totalBytes
    }

    override fun getCurrentPlaybackTransferredBytes(): Long {
        return playbackBytes
    }

    override fun onConnected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionLost() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionLostIrrecoverably() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShoutCast(cast: ShoutCast) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStream(stream: Stream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBytesRead(buffer: ByteArray, offset: Int, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNetworks(networks: List<Network>) {
        if (!interruptedByConnectionLoss || player == null || source == null) {
            return
        }
        for (network in networks) {
            if (network.internet) {
                interruptedByConnectionLoss = false
                player?.prepare(source)
                player?.playWhenReady = true
                break
            }
        }
    }

/*    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

        }

    }*/
}