package com.dreampany.tools.service

import android.content.Intent
import com.dreampany.framework.api.notify.NotifyManager
import com.dreampany.framework.api.service.BaseService
import com.dreampany.tools.R
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.NavigationActivity
import javax.inject.Inject

/**
 * Created by roman on 2019-09-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppService : BaseService() {

    @Inject
    internal lateinit var notify: NotifyManager
    internal val notifyId = Constants.Id.NOTIFY_FOREGROUND
    internal lateinit var notifyTitle: String
    internal lateinit var contentText: String
    internal val smallIcon = R.mipmap.ic_launcher
    internal val targetClass = NavigationActivity::class.java

    internal val channelId = Constants.Id.NOTIFY_FOREGROUND_CHANNEL_ID
    internal lateinit var channelName: String
    internal lateinit var channelDescription: String

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null || intent.action == null) {
            return super.onStartCommand(intent, flags, startId)
        }

        val action = intent.action
        when (action) {
            Constants.Action.START_SERVICE -> {
                //registerStepCounter()
            }
            Constants.Action.STOP_SERVICE -> {
                //unregisterStepCounter()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onStart() {
        showNotify()
    }

    override fun onStop() {
        hideNotify()
    }

    private fun showNotify() {
        notifyTitle = getString(R.string.app_name)
        contentText = getString(R.string.app_name)
        channelName = getString(R.string.app_name)
        channelDescription = getString(R.string.app_name)
        notify.showForegroundNotification(
            this,
            notifyId,
            notifyTitle,
            contentText,
            smallIcon,
            targetClass,
            null,
            channelId,
            channelName,
            channelDescription
        )
    }

    private fun hideNotify() {
        stopForeground(true)
    }
}