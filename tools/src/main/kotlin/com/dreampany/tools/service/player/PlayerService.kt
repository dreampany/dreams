package com.dreampany.tools.service.player

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.dreampany.framework.api.service.BaseService
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.NotifyUtil
import com.dreampany.tools.R
import com.dreampany.tools.api.player.SmartPlayer
import com.dreampany.tools.api.radio.RadioPlayer
import com.dreampany.tools.api.radio.ShoutCast
import com.dreampany.tools.api.radio.Stream
import com.dreampany.tools.data.model.Station
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class PlayerService
    : BaseService(),
    AudioManager.OnAudioFocusChangeListener,
    RadioPlayer.Listener {

    private var powerManager: PowerManager? = null
    private var wifiManager: WifiManager? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var wifiLock: WifiManager.WifiLock? = null

    private var audioManager: AudioManager? = null
    private var session: MediaSessionCompat? = null

    private var station: Station? = null
    private var cast: ShoutCast? = null
    private var stream: Stream? = null

    @Inject
    internal lateinit var player: RadioPlayer

    private var hls = false
    private var resumeOnFocusGain = false

    override fun onStart() {
        init()
    }

    override fun onStop() {
        if (AndroidUtil.isDebug(this)) Timber.v("PlayService should be destroyed.")
        destroy()
        session?.release()
        player.destroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onAudioFocusChange(focusChange: Int) {

    }

    override fun onState(state: SmartPlayer.State, audioSessionId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(messageId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBufferedTimeUpdate(bufferedMs: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShoutCast(cast: ShoutCast, hls: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStream(stream: Stream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    private fun init() {
        powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        player.setListener(this)
    }

    private fun destroy() {
        if (AndroidUtil.isDebug(this)) Timber.v("stopping playback.")
        resumeOnFocusGain = false

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

    fun resume() {

    }

    fun pause() {

    }

    fun stop() {}


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