package com.dreampany.framework.api.service

import android.content.Context
import android.content.Intent
import com.dreampany.framework.util.AndroidUtil
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2019-09-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class ServiceManager @Inject constructor(val context: Context) {
    fun <T : BaseService> openService(classOfT: Class<T>) {
        val intent = Intent(context, classOfT)
        openService(intent)
    }

    fun openService(intent: Intent) {
        if (AndroidUtil.hasOreo()) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}