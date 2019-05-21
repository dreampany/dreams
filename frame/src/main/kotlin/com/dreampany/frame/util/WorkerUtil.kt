package com.dreampany.frame.util

import androidx.work.*
import com.dreampany.frame.api.worker.BaseWorker
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * Created by Roman-372 on 5/21/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WorkerUtil private constructor() {
    companion object {
        fun createConstraints() =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()

        fun <W : BaseWorker> createPeriodicWorkRequest(
            workerOfClass: KClass<W>,
            data: Data,
            period: Long,
            timeUnit: TimeUnit
        ) =
            PeriodicWorkRequest.Builder(workerOfClass.java, period, timeUnit)
                .setInputData(data).setConstraints(createConstraints()).setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                ).build()
    }
}