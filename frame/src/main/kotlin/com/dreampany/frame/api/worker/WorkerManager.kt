package com.dreampany.frame.api.worker

import android.content.Context
import androidx.work.*
import com.dreampany.frame.api.service.BaseJobService
import com.dreampany.frame.util.WorkerUtil
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * Created by Roman-372 on 5/21/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WorkerManager @Inject constructor(val context: Context) {
    @Inject
    internal lateinit var factory: WorkerFactory

    fun init() {
        WorkManager.initialize(
            context,
            Configuration.Builder().setWorkerFactory(factory).build()
        )
    }

    fun <W : BaseWorker> createPeriodic(classOfWorker: KClass<W>, period: Long, unit: TimeUnit) {
        val workId = classOfWorker.java.canonicalName
        val work = WorkerUtil.createPeriodicWorkRequest(classOfWorker, Data.EMPTY, period, unit)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(workId!!, ExistingPeriodicWorkPolicy.REPLACE, work)
    }

    fun <W : BaseWorker> cancel(classOfWorker: KClass<W>) {
        val workId = classOfWorker.java.canonicalName
        WorkManager.getInstance(context).cancelUniqueWork(workId!!)
    }
}