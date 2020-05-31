package com.dreampany.framework.misc.util

import androidx.work.*
import com.dreampany.framework.worker.BaseWorker
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * Created by roman on 31/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
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

        fun <T : BaseWorker> createPeriodicWorkRequest(
            workerOfClass: KClass<T>,
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
