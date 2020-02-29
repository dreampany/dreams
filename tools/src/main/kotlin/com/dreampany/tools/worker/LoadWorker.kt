package com.dreampany.tools.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.dreampany.framework.api.worker.BaseWorker
import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.Source
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.worker.factory.IWorkerFactory
import com.dreampany.tools.ui.request.LoadRequest
import com.dreampany.tools.data.source.pref.Pref
import com.dreampany.tools.ui.vm.LoadViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by roman on 2019-04-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class LoadWorker(
    context: Context,
    params: WorkerParameters,
    private val pref: Pref,
    private val vm: LoadViewModel
) : BaseWorker(context, params) {

    override fun onStart(): Result {
        Timber.v("LoadWorker Started")
        val firestoreRequest = LoadRequest(
            type = Type.WORD,
            source = Source.FIRESTORE,
            action = Action.LOAD
        )
        //vm.request(firestoreRequest)
        val assetsRequest = LoadRequest(
            type = Type.WORD,
            source = Source.ASSETS,
            action = Action.LOAD
        )
        vm.request(assetsRequest)
        return Result.retry()
    }

    override fun onStop() {
        Timber.v("LoadWorker Stopped")
        vm.clear()
    }

    class Factory
    @Inject constructor(
        private val pref: Pref,
        private val vm: Provider<LoadViewModel>
    ) : IWorkerFactory<LoadWorker> {
        override fun create(context: Context, params: WorkerParameters): LoadWorker {
            return LoadWorker(context, params, pref, vm.get())
        }

    }
}