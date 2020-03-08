package com.dreampany.tools.receiver

import android.content.Context
import android.content.Intent
import com.dreampany.framework.api.receiver.BaseReceiver
import com.dreampany.tools.service.AppService
import kotlinx.coroutines.Runnable

/**
 * Created by roman on 2019-09-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class BootReceiver : BaseReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            ex.postToUi(Runnable { service.openService(AppService.startIntent(context)) }, 5000L)
        }
    }
}
