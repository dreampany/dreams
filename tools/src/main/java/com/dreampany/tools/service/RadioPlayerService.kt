package com.dreampany.tools.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.audiofx.AudioEffect
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media.session.MediaButtonReceiver
import com.dreampany.framework.misc.exts.isDebug
import com.dreampany.framework.misc.exts.value
import com.dreampany.framework.misc.func.NotifyManager
import com.dreampany.framework.misc.util.NotifyUtil
import com.dreampany.framework.service.InjectService
import com.dreampany.tools.R
import com.dreampany.tools.api.player.SmartPlayer
import com.dreampany.tools.api.radio.MediaSessionCallback
import com.dreampany.tools.api.radio.RadioPlayer
import com.dreampany.tools.api.radio.ShoutCast
import com.dreampany.tools.api.radio.Stream
import com.dreampany.tools.data.model.radio.Station
import com.dreampany.tools.misc.constants.AppConstants
import com.dreampany.tools.misc.constants.RadioConstants
import com.dreampany.tools.ui.home.activity.HomeActivity
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class RadioPlayerService
    : InjectService(),
    AudioManager.OnAudioFocusChangeListener,
    RadioPlayer.Listener {

    @Inject
    internal lateinit var notify: NotifyManager

    @Inject
    internal lateinit var player: RadioPlayer

    private lateinit var notifyManager: NotificationManagerCompat

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
    private var lastPlayStartTime: Long = 0

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onStart() {
        initService()
    }

    override fun onStop() {
        if (isDebug) Timber.v("PlayService should be destroyed.")
        stop()
        session?.release()
        player.destroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == null) {
            Timber.v("with a null intent. It has been probably restarted by the system.")
        } else {
            when (action) {
                AppConstants.Service.Command.RESUME -> {
                    resume()
                }
                AppConstants.Service.Command.PAUSE -> {
                    pause()
                }
                AppConstants.Service.Command.STOP -> {
                    stop()
                }
            }
            MediaButtonReceiver.handleIntent(session, intent)
        }
        return Service.START_STICKY
    }

    override fun onAudioFocusChange(focusChange: Int) {

    }

    override fun onState(state: SmartPlayer.State, audioSessionId: Int) {
        ex.postToUi(Runnable {
            when (state) {
                SmartPlayer.State.PAUSED -> {
                    setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED)
                }
                SmartPlayer.State.PLAYING -> {
                    enableSession()
                    setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                    Timber.v("Open audio effect control session, session id=$audioSessionId")
                    lastPlayStartTime = System.currentTimeMillis()

                    val intent = Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
                    intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId)
                    intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
                    localCast(intent)
                }
                else -> {
                    setMediaPlaybackState(PlaybackStateCompat.STATE_NONE)
                    if (state != SmartPlayer.State.PAUSED) {
                        disableSession()
                    }
                    if (audioSessionId > 0) {
                        Timber.v("Close audio effect control session, session id=$audioSessionId")
                        val intent = Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION)
                        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId)
                        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
                        localCast(intent)
                    }
                    if (state == SmartPlayer.State.IDLE) {
                        stop()
                    }
                }
            }

            updateNotify(state)

            val intent = Intent(AppConstants.Service.PLAYER_SERVICE_STATE_CHANGE)
            intent.putExtra(AppConstants.Service.PLAYER_SERVICE_STATE, state)
            localCast(intent)
        })
    }

    override fun onError(messageId: Int) {
        ex.postToUi(kotlinx.coroutines.Runnable {
            NotifyUtil.shortToast(baseContext, messageId)
        })
    }

    override fun onBufferedTimeUpdate(bufferedMs: Long) {
    }

    override fun onShoutCast(cast: ShoutCast, hls: Boolean) {
        this.cast = cast
        this.hls = hls
        Timber.v("Metadata offset: $cast.metadataOffset")
        Timber.v("Bitrate: $cast.bitrate")
        Timber.v("Name: $cast.name")
        Timber.v("Hls:$hls")
        Timber.v("Server: $cast.server")
        Timber.v("AudioInfo: $cast.audioInfo")
        localCast(AppConstants.Service.PLAYER_SERVICE_UPDATE)
    }

    override fun onStream(stream: Stream) {
        val oldStream = this.stream
        this.stream = stream
        stream.meta?.forEach {
            Timber.v("Stream INFO: ${it.key} - ${it.value}")
        }

        if (oldStream == null || !oldStream.title?.value.equals(stream.title)) {
            localCast(AppConstants.Service.PLAYER_SERVICE_UPDATE)
            updateNotify()
        }
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
        Timber.v("pausing playback.")
        resumeOnFocusGain = false
        releaseLock()
        player.pause()
    }

    fun stop() {
        if (isDebug) Timber.v("stopping playback.")
        resumeOnFocusGain = false
        cast = null
        stream = Stream()
        releaseAudioFocus()
        disableSession()
        player.stop()
        releaseLock()
        stopForeground(true)
    }

    fun getStation(): Station? {
        return station
    }

    fun getCast(): ShoutCast? {
        return cast
    }

    fun getStream(): Stream? {
        return stream
    }

    private fun initService() {
        notifyManager = NotificationManagerCompat.from(this)
        powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        player.setListener(this)
        sessionCallback = MediaSessionCallback(this, this)
        session = MediaSessionCompat(baseContext, baseContext.packageName)
        session?.setCallback(sessionCallback)

        val startIntent = Intent(applicationContext, HomeActivity::class.java)
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
                RadioPlayerService::javaClass.name
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
                RadioPlayerService::javaClass.name
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
                notifyManager.cancel(AppConstants.Notify.PLAYER_FOREGROUND_ID)
            }
            SmartPlayer.State.PRE_PLAYING -> {
                showNotify(
                    station!!.name!!,
                    getString(R.string.connecting),
                    getString(R.string.connecting)
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
                        getString(R.string.playing),
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
                showNotify(station!!.name!!, getString(R.string.paused), station!!.name!!)
            }
        }
    }

    private fun showNotify(title: String, message: String, ticker: String) {
        val notifyIntent = Intent(this, HomeActivity::class.java)
        notifyIntent.putExtra(RadioConstants.Keys.Station.STATION_UUID, station?.getStationUuid())
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val stopIntent = Intent(this, RadioPlayerService::class.java)
        stopIntent.action = AppConstants.Service.Command.STOP
        val pendingIntentStop = PendingIntent.getService(this, 0, stopIntent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                AppConstants.Notify.PLAYER_FOREGROUND_CHANNEL_ID,
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
            NotificationCompat.Builder(this, AppConstants.Notify.PLAYER_FOREGROUND_CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setTicker(ticker)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
                //.setLargeIcon(radioIcon.getBitmap())
                .addAction(
                    R.drawable.ic_stop_black_24dp,
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
            val pauseIntent = Intent(this, RadioPlayerService::class.java)
            pauseIntent.action = AppConstants.Service.Command.PAUSE
            val pendingIntentPause = PendingIntent.getService(this, 0, pauseIntent, 0)

            builder.addAction(
                R.drawable.ic_pause_black_24dp,
                getString(R.string.pause),
                pendingIntentPause
            )
            builder.setUsesChronometer(true).setOngoing(true)

        } else if (state == SmartPlayer.State.IDLE || state == SmartPlayer.State.PAUSED) {

            val resumeIntent = Intent(this, RadioPlayerService::class.java)
            resumeIntent.action = AppConstants.Service.Command.RESUME
            val pendingIntentResume = PendingIntent.getService(this, 0, resumeIntent, 0)

            builder.addAction(
                R.drawable.ic_play_arrow_black_24dp,
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
        startForeground(AppConstants.Notify.PLAYER_FOREGROUND_ID, notification)

        if (state == SmartPlayer.State.PAUSED) {
            stopForeground(false) // necessary to make notification dismissible
        }
    }

    private fun localCast(action: String) {
        localCast(Intent(action))
    }

    private fun localCast(intent: Intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    inner class ServiceBinder : Binder() {
        fun getService(): RadioPlayerService = this@RadioPlayerService
    }
}