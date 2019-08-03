package com.dreampany.history.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.dreampany.history.vm.NotifyViewModel
import com.dreampany.frame.api.worker.BaseWorker
import com.dreampany.frame.worker.factory.IWorkerFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by roman on 2019-04-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class NotifyWorker(
    context: Context, workerParams: WorkerParameters,
    private val vm: NotifyViewModel
) : BaseWorker(context, workerParams) {

    override fun onStart(): Result {
        Timber.v("NotifyWorker Started")
        //vm.notifyIf()
        return Result.retry()
    }

    override fun onStop() {
        Timber.v("NotifyWorker Stopped")
        //vm.clear()
    }

    class Factory @Inject constructor(private val vm: Provider<NotifyViewModel>) : IWorkerFactory<NotifyWorker> {
        override fun create(context: Context, params: WorkerParameters): NotifyWorker {
            return NotifyWorker(context, params, vm.get())
        }

    }
}