package com.dreampany.crypto.inject.worker

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.dreampany.framework.inject.annote.WorkerKey
import com.dreampany.framework.worker.IWorkerFactory
import com.dreampany.framework.worker.WorkerInjectFactory
import com.dreampany.crypto.worker.DemoWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Created by roman on 31/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class WorkerModule {
    @Singleton
    @Binds
    abstract fun bindFactory(factory: WorkerInjectFactory): WorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(DemoWorker::class)
    abstract fun bindDemoWorker(worker: DemoWorker.Factory): IWorkerFactory<out ListenableWorker>
}