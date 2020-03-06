package com.dreampany.tools.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.dreampany.common.data.enums.ServiceState
import com.dreampany.common.data.source.pref.ServicePref
import com.dreampany.framework.api.notify.NotifyManager
import com.dreampany.framework.api.service.BaseService
import com.dreampany.tools.R
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.misc.Constants
import com.dreampany.tools.ui.activity.NavigationActivity
import com.dreampany.tools.worker.NotifyWorker
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by roman on 2019-09-12
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class AppService : BaseService() {

    @Inject
    internal lateinit var pref: Pref

    @Inject
    internal lateinit var servicePref: ServicePref

    @Inject
    internal lateinit var notify: NotifyManager

    private var lock: PowerManager.WakeLock? = null
    private var started = false

    private val notifyId = Constants.Notify.FOREGROUND_ID
    private lateinit var notifyTitle: String
    private lateinit var contentText: String
    private val smallIcon = R.drawable.ic_notification
    private val targetClass = NavigationActivity::class.java

    private val channelId = Constants.Notify.FOREGROUND_CHANNEL_ID
    private lateinit var channelName: String
    private lateinit var channelDescription: String

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == null) {
            Timber.v("with a null intent. It has been probably restarted by the system.")
        } else {
            when (action) {
                Constants.Action.START_SERVICE -> startService()
                Constants.Action.STOP_SERVICE -> stopService()
            }
        }
        return Service.START_STICKY
    }

    override fun onStart() {
        Timber.v("The service has been created".toUpperCase(Locale.getDefault()))
        showNotify()
        //configWork()
    }

    override fun onStop() {
        Timber.v("The service has been destroyed".toUpperCase(Locale.getDefault()))
        //hideNotify()
    }

    private fun startService() {
        if (started) return
        Timber.v("Starting the foreground service task")
        started = true
        servicePref.setState(Constants.Pref.Service.APP_SERVICE, ServiceState.STARTED)
        lock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "${AppService::class.java.simpleName}::lock"
            ).apply {
                acquire()
            }
        }
    }

    private fun stopService() {
        Timber.v("Stopping the foreground service")
        try {
            lock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            hideNotify()
            stopSelf()
        } catch (error: Throwable) {
            Timber.e(error, "Service stopped without being started: %s", error.message)
        }
    }

    private fun showNotify() {
        notifyTitle = getString(R.string.title_notify)
        contentText = getString(R.string.description_notify)
        channelName = getString(R.string.title_notify_channel)
        channelDescription = getString(R.string.description_notify_channel)
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

    /**
     * java.lang.IllegalArgumentException: could not find worker: androidx.work.impl.workers.ConstraintTrackingWorker
     * at com.dreampany.frame.worker.factory.WorkerInjectorFactory.createWorker(WorkerInjectorFactory.kt:26)
     */
    private fun configWork() {
        if (pref.hasNotification()) {
            worker.createPeriodic(
                NotifyWorker::class,
                Constants.Period.NOTIFY,
                TimeUnit.MILLISECONDS
            )
        } else {
            worker.cancel(NotifyWorker::class)
        }
    }
}