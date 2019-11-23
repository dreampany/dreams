package com.dreampany.tools.receiver

import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.dreampany.framework.api.receiver.BaseReceiver
import com.dreampany.tools.ui.vm.BlockViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by roman on 2019-11-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class BlockCallReceiver : BaseReceiver() {

    @Inject
    internal lateinit var vm: BlockViewModel

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED != intent.action) {
            Timber.d("IncomingCallReceiver called with incorrect intent action: %s", intent.action)
            return
        }
        val newState = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        Timber.d("Call state changed to %s", newState)
        if (TelephonyManager.EXTRA_STATE_RINGING == newState) {
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (phoneNumber.isNullOrEmpty()) {
                Timber.d("Ignoring call; for some reason every state change is doubled")
                return
            }
            Timber.i("Incoming call from %s", phoneNumber)
            vm.blockContactIf(phoneNumber)
        }
    }
}