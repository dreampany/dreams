package com.dreampany.tools.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.dreampany.common.data.enums.ServiceState
import com.dreampany.common.data.source.pref.ServicePref
import com.dreampany.common.misc.extension.currentPackage
import com.dreampany.common.misc.extension.isEquals
import com.dreampany.framework.api.notify.NotifyManager
import com.dreampany.framework.api.service.BaseService
import com.dreampany.tools.R
import com.dreampany.tools.data.mapper.LockMapper
import com.dreampany.tools.data.source.pref.LockPref
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
    internal lateinit var lockPref: LockPref

    @Inject
    internal lateinit var lockMapper: LockMapper

    @Inject
    internal lateinit var notify: NotifyManager

    private lateinit var powerManager: PowerManager
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

    private lateinit var locker: Thread
    private var lockerRunning = false
    private var lastPackage: String? = null

    companion object {
        fun startIntent(context: Context): Intent {
            val intent = Intent(context, AppService::class.java)
            intent.action = Constants.Service.Command.START
            return intent
        }

        fun stopIntent(context: Context): Intent {
            val intent = Intent(context, AppService::class.java)
            intent.action = Constants.Service.Command.STOP
            return intent
        }

        fun lockIntent(context: Context): Intent {
            val intent = Intent(context, AppService::class.java)
            intent.action = Constants.Service.Command.START_LOCK
            return intent
        }

        fun unlockIntent(context: Context): Intent {
            val intent = Intent(context, AppService::class.java)
            intent.action = Constants.Service.Command.STOP_LOCK
            return intent
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == null) {
            Timber.v("with a null intent. It has been probably restarted by the system.")
        } else {
            when (action) {
                Constants.Service.Command.START -> {
                    startService()
                    if (lockPref.isServicePermitted()) startLocker()
                }
                Constants.Service.Command.STOP -> stopService()
                Constants.Service.Command.START_LOCK -> {
                    if (lockPref.isServicePermitted()) startLocker()
                }
                Constants.Service.Command.STOP_LOCK -> stopLocker()
            }
        }
        return Service.START_STICKY
    }

    override fun onStart() {
        Timber.v("The service has been created".toUpperCase(Locale.getDefault()))
        initService()
        showNotify()
        //startLocker()
        //configWork()
    }

    override fun onStop() {
        Timber.v("The service has been destroyed".toUpperCase(Locale.getDefault()))
        hideNotify()
        stopLocker()
    }

    private fun initService() {
        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (lock == null)
            lock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                AppService::javaClass.name
            )
    }

    @SuppressLint("WakelockTimeout")
    private fun startService() {
        if (started) return
        Timber.v("Starting the foreground service task")
        started = true
        servicePref.setState(Constants.Pref.Service.APP_SERVICE, ServiceState.STARTED)
        lock?.run {
            if (isHeld) {
                Timber.v("wake lock is held")
            } else {
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
            stopSelf()
        } catch (error: Throwable) {
            Timber.e(error, "Service stopped without being started: %s", error.message)
        }
        started = false
        servicePref.setState(Constants.Pref.Service.APP_SERVICE, ServiceState.STOPPED)
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

    /* lock */
    private fun startLocker() {
        Timber.v("Locker thread is staring")
        if (::locker.isInitialized.not()) {
            locker = object : Thread() {
                override fun run() {
                    while (lockerRunning && !locker.isInterrupted) {
                        //Timber.v("Locker thread is running %d", System.currentTimeMillis())
                        if (!lockPref.isServicePermitted()) {
                            lockerRunning = false
                            continue
                        }
                        checkLock()
                        try {
                            sleep(800L)
                        } catch (error: InterruptedException) {
                            Timber.e(error, "Locker thread is interrupted")
                        }
                    }
                }
            }
        }

        if (locker.isAlive.not()) {
            lockerRunning = true
            locker.start()
        }

    }

    private fun stopLocker() {
        if (locker.isAlive) {
            lockerRunning = false
            locker.interrupt()
        }
    }

    private fun checkLock() {
        val pkg = currentPackage() ?: return
        if (!pkg.isEquals(lastPackage)) {
            Timber.v("Cache %s - Current %s", lastPackage, pkg)
            if (lockMapper.isLocked(pkg) && !lockMapper.isUnlocked(pkg))
                lockedAppOpened(pkg)
        }
        lastPackage = pkg
    }

    private fun lockedAppOpened(packageName: String) {
        Timber.v("AppService Lock Package: $packageName")
        startService(LockService.lockIntent(this, packageName))
    }

}