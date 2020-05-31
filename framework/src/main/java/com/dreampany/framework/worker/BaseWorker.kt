package com.dreampany.framework.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created by roman on 31/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseWorker
constructor(
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

    abstract fun onStart(): Result
    abstract fun onStop()
}