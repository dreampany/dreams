package com.dreampany.tools.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver
import com.dreampany.framework.api.notify.NotifyManager
import com.dreampany.framework.api.service.BaseService
import com.dreampany.framework.util.AndroidUtil
import com.dreampany.framework.util.NotifyUtil
import com.dreampany.tools.R
import com.dreampany.tools.api.player.SmartPlayer
import com.dreampany.tools.api.radio.MediaSessionCallback
import com.dreampany.tools.api.radio.RadioPlayer
import com.dreampany.tools.api.radio.ShoutCast
import com.dreampany.tools.api.radio.Stream
import com.dreampany.tools.data.model.Station
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.NavigationActivity
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

    @Inject
    internal lateinit var notify: NotifyManager
    @Inject
    internal lateinit var player: RadioPlayer

    private val binder: ServiceBinder = ServiceBinder()

    private lateinit var powerManager: PowerManager
    private lateinit var wifiManager: WifiManager
    private var wakeLock: PowerManager.WakeLock? = null
    private var wifiLock: WifiManager.WifiLock? = null

    private lateinit var audioManager: AudioManager
    private var session: MediaSessionCompat? = null

    private var station: Station? = null
    private var cast: ShoutCast? = null
    private var stream: Stream? = null

    private lateinit var sessionCallback: MediaSessionCallback

    private var hls = false
    private var resumeOnFocusGain = false

    private lateinit var notifyManager: NotificationManagerCompat

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onStart() {
        notifyManager = NotificationManagerCompat.from(this)
        init()
    }

    override fun onStop() {
        if (AndroidUtil.isDebug(this)) Timber.v("PlayService should be destroyed.")
        stop()
        session?.release()
        player.destroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (intent.action != null) {
                when (intent.action) {
                    Constants.Service.Command.RESUME -> {
                        resume()
                    }
                    Constants.Service.Command.PAUSE -> {
                        pause()
                    }
                    Constants.Service.Command.STOP -> {
                        stop()
                    }
                }
            }
            MediaButtonReceiver.handleIntent(session, intent)
        }
        return Service.START_STICKY
    }

    override fun onAudioFocusChange(focusChange: Int) {

    }

    override fun onState(state: SmartPlayer.State, audioSessionId: Int) {



    }

    override fun onError(messageId: Int) {
    }

    override fun onBufferedTimeUpdate(bufferedMs: Long) {
    }

    override fun onShoutCast(cast: ShoutCast, hls: Boolean) {
    }

    override fun onStream(stream: Stream) {
    }

    fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    fun play(station: Station? = null) {
        if (station != null) {
            this.station = station
        }
        val result = acquireAudioFocus()
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            ex.postToUi(kotlinx.coroutines.Runnable {
                NotifyUtil.shortToast(this, R.string.error_grant_audiofocus)
            })
            return
        }
        enableSession()
        replay()
    }

    fun replay() {
        Timber.v("replaying current.")
        stream = Stream()
        cast = null
        acquireLock()
        player.play(station!!.url!!, station!!.name!!)
    }

    fun resume() {
        Timber.v("resuming playback.")
        resumeOnFocusGain = false
        if (player.isPlaying()) return
        acquireAudioFocus()
        replay()
    }

    fun pause() {

    }

    fun stop() {
        if (AndroidUtil.isDebug(this)) Timber.v("stopping playback.")
        resumeOnFocusGain = false
        cast = null
        stream = Stream()
        releaseAudioFocus()
        disableSession()
        player.stop()
        releaseLock()
        stopForeground(true)
    }

    fun getStation() : Station? {
        return station
    }

    fun getCast() : ShoutCast ? {
        return cast
    }

    fun getStream() : Stream ? {
        return stream
    }

    private fun init() {
        powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        player.setListener(this)
        sessionCallback = MediaSessionCallback(this, this)
        session = MediaSessionCompat(baseContext, baseContext.packageName)
        session?.setCallback(sessionCallback)

        val startIntent = Intent(applicationContext, NavigationActivity::class.java)
        //todo keep ui task to define radio activity opening using fragment
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            startIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        session?.setSessionActivity(pendingIntent)
        session?.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

    }

    private fun acquireAudioFocus(): Int {
        Timber.v("acquireAudioFocus")
        val result =
            audioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            ) ?: AudioManager.AUDIOFOCUS_REQUEST_FAILED
        return result
    }

    private fun releaseAudioFocus() {
        Timber.v("releaseAudioFocus")
        audioManager.abandonAudioFocus(this)
    }

    private fun enableSession() {
        session?.isActive = true
    }

    private fun disableSession() {
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
            wakeLock = powerManager.newWakeLock(
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
            wifiLock = wifiManager.createWifiLock(
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

    private fun updateNotify() {
        updateNotify(player.getState())
    }

    private fun updateNotify(state: SmartPlayer.State) {
        when (state) {
            SmartPlayer.State.IDLE -> {
                notifyManager.cancel(Constants.Notify.PLAYER_FOREGROUND_ID)
            }
            SmartPlayer.State.PRE_PLAYING -> {
                showNotify(
                    station!!.name!!,
                    getString(R.string.notify_pre_play),
                    getString(R.string.notify_pre_play)
                )
            }
            SmartPlayer.State.PLAYING -> {
                val title: String? = stream?.title
                if (!title.isNullOrEmpty()) {
                    Timber.v("update message:$title")
                    showNotify(station!!.name!!, title, title)
                } else {
                    showNotify(
                        station!!.name!!,
                        getString(R.string.notify_playing),
                        station!!.name!!
                    )
                }

                if (session != null) {
                    val builder = MediaMetadataCompat.Builder()
                    builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, station?.name)
                    builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, stream?.artist)
                    builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, stream?.track)
                    if (stream!!.hasArtistAndTrack()) {
                        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, stream?.artist)
                        builder.putString(
                            MediaMetadataCompat.METADATA_KEY_TITLE,
                            stream?.track
                        )
                    } else {
                        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, stream?.title)
                        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, station?.name)
                    }
                    //builder.putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, radioIcon.getBitmap())
                    //builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, radioIcon.getBitmap())
                    session?.setMetadata(builder.build())
                }
            }
            SmartPlayer.State.PAUSED -> {
                showNotify(station!!.name!!, getString(R.string.notify_paused), station!!.name!!)
            }
        }
    }

    private fun showNotify(title: String, message: String, ticker: String) {
        val notifyIntent = Intent(this, NavigationActivity::class.java)
        //notificationIntent.putExtra("stationid", currentStation.StationUuid)
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val stopIntent = Intent(this, PlayerService::class.java)
        stopIntent.action = Constants.Service.Command.STOP
        val pendingIntentStop = PendingIntent.getService(this, 0, stopIntent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.Notify.PLAYER_FOREGROUND_CHANNEL_ID,
                "Smart Radio",
                NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(false)
            notificationChannel.enableVibration(false)
            notifyManager.createNotificationChannel(notificationChannel)
        }

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder =
            NotificationCompat.Builder(this, Constants.Notify.PLAYER_FOREGROUND_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setTicker(ticker)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
                //.setLargeIcon(radioIcon.getBitmap())
                .addAction(
                    R.drawable.ic_media_stop_light,
                    getString(R.string.stop),
                    pendingIntentStop
                )
/*            .addAction(
                R.drawable.ic_skip_previous_24dp,
                getString(R.string.action_skip_to_previous),
                pendingIntentPrevious
            )*/

        val state = player.getState()
        if (state == SmartPlayer.State.PRE_PLAYING || state == SmartPlayer.State.PLAYING) {
            val pauseIntent = Intent(this, PlayerService::class.java)
            pauseIntent.action = Constants.Service.Command.PAUSE
            val pendingIntentPause = PendingIntent.getService(this, 0, pauseIntent, 0)

            builder.addAction(
                R.drawable.ic_pause_black_24dp,
                getString(R.string.pause),
                pendingIntentPause
            )
            builder.setUsesChronometer(true).setOngoing(true)

        } else if (state == SmartPlayer.State.IDLE || state == SmartPlayer.State.PAUSED) {

            val resumeIntent = Intent(this, PlayerService::class.java)
            resumeIntent.action = Constants.Service.Command.RESUME
            val pendingIntentResume = PendingIntent.getService(this, 0, resumeIntent, 0)

            builder.addAction(
                R.drawable.ic_media_play_light,
                getString(R.string.resume),
                pendingIntentResume
            )

            builder.setUsesChronometer(false).setDeleteIntent(pendingIntentStop).setOngoing(false)
        }

/*        builder.addAction(
            R.drawable.ic_skip_next_24dp,
            getString(R.string.action_skip_to_next),
            pendingIntentNext
        )*/
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(session?.getSessionToken())
                .setShowActionsInCompactView(1 /* previous, play/pause, next */)
                .setCancelButtonIntent(pendingIntentStop)
                .setShowCancelButton(true)
        )
        val notification = builder.build()
        startForeground(Constants.Notify.PLAYER_FOREGROUND_ID, notification)

        if (state == SmartPlayer.State.PAUSED) {
            stopForeground(false) // necessary to make notification dismissible
        }
    }

    inner class ServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }
}