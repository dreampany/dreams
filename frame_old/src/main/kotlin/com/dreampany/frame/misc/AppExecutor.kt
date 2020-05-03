package com.dreampany.frame.misc

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.Runnable
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AppExecutor
private constructor(
    private val ui: UiThreadExecutor,
    private val disk: DiskThreadExecutor,
    private val network: NetworkThreadExecutor
) {

    @Inject
    constructor() : this(
        UiThreadExecutor(),
        DiskThreadExecutor(),
        NetworkThreadExecutor()
    ) {
    }

    fun getUiHandler(): Handler {
        return ui.handler
    }

    fun postToUi(run: Runnable) {
        ui.executeUniquely(run)
    }

    fun postToUi(run: Runnable, delay: Long) {
        ui.executeUniquely(run, delay)
    }

    fun postToUiSmartly(runnable: Runnable) {
        ui.execute(runnable)
    }

    fun postToDisk(run: Runnable): Boolean {
        disk.execute(run)
        return true
    }

    fun postToNetwork(run: Runnable): Boolean {
        network.execute(run)
        return true
    }

    /* Ui Executor */
    class UiThreadExecutor : Executor {
        internal val handler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            handler.post(command)
        }

        fun executeUniquely(command: Runnable) {
            handler.removeCallbacks(command)
            handler.post(command)
        }

        fun execute(command: Runnable, delay: Long) {
            handler.postDelayed(command, delay)
        }

        fun executeUniquely(command: Runnable, delay: Long) {
            handler.removeCallbacks(command)
            handler.postDelayed(command, delay)
        }
    }

    /* Disk Executor */
    class DiskThreadExecutor : Executor {
        private val disk: Executor

        init {
            disk = Executors.newSingleThreadExecutor()
        }

        override fun execute(command: Runnable) {
            disk.execute(command)
        }
    }

    /* Network Executor */
    class NetworkThreadExecutor : Executor {
        private val THREAD_COUNT = Constants.Count.THREAD_NETWORK
        private val network: Executor

        init {
            network = Executors.newFixedThreadPool(THREAD_COUNT)
        }

        override fun execute(command: Runnable) {
            network.execute(command)
        }
    }
}