package com.dreampany.tools.receiver

import android.content.Context
import android.content.Intent
import com.dreampany.framework.api.notify.NotifyManager
import com.dreampany.framework.api.receiver.BaseReceiver
import javax.inject.Inject

/**
 * Created by roman on 2019-11-23
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class BlockCallReceiver : BaseReceiver() {

    @Inject
    internal lateinit var notify: NotifyManager

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
    }
}