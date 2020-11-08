package com.dreampany.tools.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.view.KeyEvent
import com.dreampany.framework.api.cast.CastManager
import com.dreampany.tools.api.radiobrowser.Constants
import com.dreampany.tools.api.radiobrowser.RadioDroidBrowser

/**
 * Created by roman on 8/11/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MediaSessionCallback
constructor(
    private val context: Context,
    private val serviceRadio: RadioPlayerService
) : MediaSessionCompat.Callback() {

    override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
        val event = mediaButtonEvent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
        if (event != null && event.keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            if (event.action == KeyEvent.ACTION_UP && !event.isLongPress) {
                if (serviceRadio.isPlaying()) {
                    serviceRadio.pause()
                } else {
                    serviceRadio.resume()
                }
            }
            return true
        }
        return super.onMediaButtonEvent(mediaButtonEvent)
    }

    override fun onPlay() {
        super.onPlay()
        serviceRadio.resume()
    }

    override fun onPause() {
        serviceRadio.pause()
        super.onPause()
    }

    override fun onStop() {
        serviceRadio.stop()
        super.onStop()
    }

    override fun onPlayFromMediaId(mediaId: String, extras: Bundle) {
        val stationId = RadioDroidBrowser.getStationIdOfMediaId(mediaId)
        if (stationId.isEmpty()) return
        val extra = hashMapOf(Constants.Keys.Radio.STATION_ID to stationId)
        CastManager.castLocally(context, Constants.Keys.Radio.PLAY_BY_STATION_ID, extra)
    }
}