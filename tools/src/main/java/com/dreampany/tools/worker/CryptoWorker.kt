package com.dreampany.tools.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.dreampany.framework.worker.BaseWorker
import com.dreampany.framework.worker.IWorkerFactory
import com.dreampany.tools.ui.crypto.vm.CryptoViewModel
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by roman on 31/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class CryptoWorker(
    context: Context,
    params: WorkerParameters,
    private val vm : CryptoViewModel
) : BaseWorker(context, params) {

    override fun onStart(): Result {
        vm.notifyProfitableCoin()
        return Result.retry()
    }

    override fun onStop() {
    }

    class Factory
    @Inject constructor(
        private val vm: Provider<CryptoViewModel>
    ) : IWorkerFactory<CryptoWorker> {
        override fun create(context: Context, params: WorkerParameters): CryptoWorker = CryptoWorker(context, params, vm.get())
    }
}