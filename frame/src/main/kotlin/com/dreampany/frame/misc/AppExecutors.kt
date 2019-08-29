package com.dreampany.frame.misc

import android.os.Handler
import android.os.Looper
import com.dreampany.frame.util.AndroidUtil
import kotlinx.coroutines.Runnable
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Roman-372 on 5/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class AppExecutors(
    private val uiExecutor: UiThreadExecutor,
    private val diskIO: DiskIOThreadExecutor,
    private val networkIO: NetworkIOThreadExecutor
) {
    @Inject
    constructor() : this(
        UiThreadExecutor(), DiskIOThreadExecutor(), NetworkIOThreadExecutor()
    ) {
    }

    fun postToUi(run: Runnable) {
        uiExecutor.execute(run)
    }

    fun postToUi(run: Runnable, delay: Long) {
        uiExecutor.executeUniquely(run, delay)
    }

    fun postToUiSmartly(runnable: Runnable) {
        if (AndroidUtil.isOnUiThread()) {
            runnable.run()
        } else {
            uiExecutor.execute(runnable)
        }
    }

    fun postToIO(run: Runnable): Boolean {
        diskIO.execute(run)
        return true
    }

    fun postToNetwork(run: Runnable): Boolean {
        networkIO.execute(run)
        return true
    }

    /* Ui Executor */
    class UiThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())

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

    /* DiskIO Executor */
    class DiskIOThreadExecutor : Executor {
        private val diskIO: Executor

        init {
            diskIO = Executors.newSingleThreadExecutor()
        }

        override fun execute(command: Runnable) {
            diskIO.execute(command)
        }
    }

    /* NetworkIO Executor */
    class NetworkIOThreadExecutor : Executor {
        private val THREAD_COUNT = 3
        private val networkIO: Executor

        init {
            networkIO = Executors.newFixedThreadPool(THREAD_COUNT)
        }

        override fun execute(command: Runnable) {
            networkIO.execute(command)
        }
    }
}