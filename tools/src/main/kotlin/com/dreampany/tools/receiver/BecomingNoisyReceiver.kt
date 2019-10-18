package com.dreampany.tools.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

/**
 * Created by roman on 2019-10-13
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class BecomingNoisyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {

        }
    }
}