package com.dreampany.tools.service.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.session.MediaSessionCompat
import android.view.KeyEvent
import timber.log.Timber

/**
 * Created by roman on 2019-10-15
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class MediaSessionCallback
constructor(
    private val context: Context,
    private val service: PlayerService
) : MediaSessionCompat.Callback() {

    override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
        val event = mediaButtonEvent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
        if (event != null && event.keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
            if (event.action == KeyEvent.ACTION_UP && !event.isLongPress){
                if (service.isPlaying()) {
                    service.pause()
                } else {
                    service.resume()
                }
            }
            return true
        }
        return super.onMediaButtonEvent(mediaButtonEvent)
    }

    override fun onPlay() {
        super.onPlay()
        service.resume()
    }

    override fun onPause() {
        service.pause()
        super.onPause()
    }

    override fun onStop() {
        service.stop()
        super.onStop()
    }

    override fun onPlayFromMediaId(mediaId: String, extras: Bundle) {
        super.onPlayFromMediaId(mediaId, extras)
    }
}