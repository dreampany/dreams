package com.dreampany.tools.service

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.dreampany.framework.api.service.BaseService
import com.dreampany.framework.util.NotifyUtil
import com.dreampany.tools.R
import com.dreampany.tools.data.model.Station
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.upstream.DefaultAllocator
import timber.log.Timber

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PlayerService : BaseService(), AudioManager.OnAudioFocusChangeListener {

    //private var player: SimpleExoPlayer? = null

    private var powerManager: PowerManager? = null
    private var wifiManager: WifiManager? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var wifiLock: WifiManager.WifiLock? = null

    private var audioManager: AudioManager? = null
    private var session: MediaSessionCompat? = null

    private var station: Station? = null

    override fun onStart() {
        init()
        //initPlayer()
    }

    override fun onStop() {
    }

    override fun onAudioFocusChange(focusChange: Int) {

    }

    private fun init() {
        powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private fun play() {
        val result = acquireAudioFocus()
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            ex.postToUi(kotlinx.coroutines.Runnable {
                NotifyUtil.shortToast(this, R.string.error_grant_audiofocus)
            })
            return
        }

    }

    private fun replay() {
        acquireLock()
    }

    private fun pause() {

    }

    private fun initPlayer() {
/*        releasePlayer()
        val track = DefaultTrackSelector()
        val load = createLoadControl(10)
        player = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(applicationContext),
            track,
            load
        )
        player.addAnalyticsListener()*/
    }

    private fun releasePlayer() {
        /*player?.release()
        player = null*/
    }

    private fun createLoadControl(factor: Int): LoadControl {
        return DefaultLoadControl.Builder().setAllocator(
            DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE * factor)
        ).createDefaultLoadControl()
    }

    private fun acquireAudioFocus(): Int {
        Timber.v("acquireAudioFocus")
        val result =
            audioManager?.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            ) ?: AudioManager.AUDIOFOCUS_REQUEST_FAILED
        return result
    }

    private fun releaseAudioFocus() {
        Timber.v("releaseAudioFocus")
        audioManager?.abandonAudioFocus(this)
    }

    private fun enableMediaSession() {
        session?.isActive = true
    }

    private fun disableMediaSession() {
        session?.run {
            if (isActive) {
                isActive = false
            }
        }
    }

    private fun setMediaPlaybackState(state: Int) {
        if (session == null) return
        val builder = PlaybackStateCompat.Builder()
        builder.setActions(
            PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                    or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                    or PlaybackStateCompat.ACTION_PAUSE
        )
        builder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
        session?.setPlaybackState(builder.build())
    }

    @SuppressLint("WakelockTimeout")
    private fun acquireLock() {
        Timber.v("acquireLock")
        if (wakeLock == null)
            wakeLock = powerManager?.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                PlayerService::javaClass.name
            )

        wakeLock?.run {
            if (isHeld) {
                Timber.v("wake lock held")
            } else {
                acquire()
            }
        }

        if (wifiLock == null)
            wifiLock = wifiManager?.createWifiLock(
                WifiManager.WIFI_MODE_FULL_HIGH_PERF,
                PlayerService::javaClass.name
            )

        wifiLock?.run {
            if (isHeld) {
                Timber.v("wifi lock held")
            } else {
                acquire()
            }
        }
    }

    private fun releaseLock() {
        Timber.v("releaseLock")
        wakeLock?.run {
            if (isHeld) release()
        }
        wifiLock?.run {
            if (isHeld) release()
        }
        wakeLock = null
        wifiLock = null
    }
}