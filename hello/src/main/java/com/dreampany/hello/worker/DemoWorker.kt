package com.dreampany.hello.worker

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
class DemoWorker(
    context: Context,
    params: WorkerParameters
) : BaseWorker(context, params) {

    override fun onStart(): Result {
        //vm.notifyProfitableCoin()
        return Result.retry()
    }

    override fun onStop() {
    }

    class Factory
    @Inject constructor(

    ) : IWorkerFactory<DemoWorker> {
        override fun create(context: Context, params: WorkerParameters): DemoWorker = DemoWorker(context, params)
    }
}