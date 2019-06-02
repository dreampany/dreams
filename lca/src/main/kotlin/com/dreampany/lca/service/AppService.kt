package com.dreampany.lca.service

import android.content.Intent
import android.os.IBinder
import com.dreampany.frame.api.notify.NotifyManager
import com.dreampany.frame.api.service.BaseService
import com.dreampany.lca.R
import com.dreampany.lca.misc.Constants
import com.dreampany.lca.ui.activity.NavigationActivity
import javax.inject.Inject

/**
 * Created by roman on 2019-06-01
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppService : BaseService() {

    @Inject
    internal lateinit var notify: NotifyManager
    internal val notifyId = Constants.Id.NotifyForeground
    internal lateinit var notifyTitle: String
    internal lateinit var contentText: String
    internal val smallIcon = R.mipmap.ic_launcher
    internal val targetClass = NavigationActivity::class.java

    internal val channelId = Constants.Id.NotifyForegroundChannelId
    internal lateinit var channelName: String
    internal lateinit var channelDescription: String

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null || intent.action == null) {
            return super.onStartCommand(intent, flags, startId)
        }

        val action = intent.action
        when (action) {
            Constants.Action.StartService -> {
                //registerStepCounter()
            }
            Constants.Action.StopService -> {
                //unregisterStepCounter()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        //unregisterStepCounter()
        stopForeground(true)
        super.onDestroy()
    }
}