package com.dreampany.tools.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.dreampany.framework.worker.BaseWorker
import com.dreampany.framework.worker.IWorkerFactory
import javax.inject.Inject

/**
 * Created by roman on 31/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CryptoWorker(
    context: Context,
    params: WorkerParameters
) : BaseWorker(context, params) {
    override fun onStart(): Result {

        return Result.retry()
    }

    override fun onStop() {
    }

    class Factory
    @Inject constructor(

    ) : IWorkerFactory<CryptoWorker> {
        override fun create(context: Context, params: WorkerParameters): CryptoWorker = CryptoWorker(context, params)
    }
}