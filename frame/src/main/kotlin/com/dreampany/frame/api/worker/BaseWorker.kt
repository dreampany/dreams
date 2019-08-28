package com.dreampany.frame.api.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created by roman on 2019-04-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseWorker constructor(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        return onStart()
        //return if (result) Result.retry() else Result.failure()
    }

    override fun onStopped() {
        onStop()
        super.onStopped()
    }

    open fun onStart(): Result {
        return Result.success()
    }

    open fun onStop() {

    }
}
