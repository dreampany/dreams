package com.dreampany.tools.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import timber.log.Timber

/**
 * Created by Roman-372 on 9/3/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.action)) {
            intent.action?.run {
                Timber.v("CallReceiver called with incorrect intent action: %s", this)
            }
            return
        }
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        Timber.v("Call state changed to %s", state)
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (phoneNumber == null) {
                Timber.v("Ignoring call; for some reason every state change is doubled")
                return
            }
            Timber.v("Incoming call from %s", phoneNumber)
        }
    }
}