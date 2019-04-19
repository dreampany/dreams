package com.dreampany.lca.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dreampany.frame.worker.factory.IWorkerFactory
import com.dreampany.lca.vm.NotifyViewModel
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
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class Factory
    @Inject constructor(private val vm: Provider<NotifyViewModel>) : IWorkerFactory<NotifyWorker> {
        override fun create(context: Context, params: WorkerParameters): NotifyWorker {
            return NotifyWorker(context, params, vm.get())
        }

    }
}