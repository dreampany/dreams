package com.dreampany.fit.receiver

import android.content.Context
import android.content.Intent
import com.dreampany.fit.service.AppService
import com.dreampany.frame.api.receiver.BaseReceiver
import com.dreampany.frame.api.service.ServiceManager

import javax.inject.Inject

/**
 * Created by Roman-372 on 2/8/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class BootReceiver : BaseReceiver() {

    @Inject
    lateinit var service: ServiceManager

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)

        if (Intent.ACTION_BOOT_COMPLETED == intent!!.action) {
            ex.postToUi({ service.openService(AppService::class.java) }, 5000L)
        }
    }
}
