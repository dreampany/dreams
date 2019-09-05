package com.dreampany.tools.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.dreampany.framework.api.worker.BaseWorker
import com.dreampany.framework.worker.factory.IWorkerFactory
import com.dreampany.tools.ui.vm.NotifyViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by roman on 2019-08-28
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class WordWorker(
    context: Context,
    params: WorkerParameters
) : BaseWorker(context, params) {


    override fun onStart(): Result {
        Timber.v("WordWorker Started")
        //vm.notifyIf()
        return Result.retry()
    }

    override fun onStop() {
        Timber.v("WordWorker Stopped")
        //vm.clearIf()
    }

    class Factory
    @Inject constructor(private val vm: Provider<NotifyViewModel>) : IWorkerFactory<WordWorker> {
        override fun create(context: Context, params: WorkerParameters): WordWorker {
            return WordWorker(context, params)
        }

    }
}