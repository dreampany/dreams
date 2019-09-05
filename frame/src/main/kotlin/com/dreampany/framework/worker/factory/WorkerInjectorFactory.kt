package com.dreampany.framework.worker.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by Roman-372 on 4/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Singleton
class WorkerInjectorFactory
@Inject
constructor(
    private val factories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<IWorkerFactory<out ListenableWorker>>>
) : WorkerFactory() {

    override fun createWorker(context: Context, workerClassName: String, params: WorkerParameters): ListenableWorker? {
        val entry = factories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factory = entry?.value ?: throw IllegalArgumentException("could not find worker: $workerClassName")
        return factory.get().create(context, params)
    }
}