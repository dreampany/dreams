package com.dreampany.cast.injector.worker

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.dreampany.frame.misc.WorkerKey
import com.dreampany.frame.worker.factory.IWorkerFactory
import com.dreampany.frame.worker.factory.WorkerInjectorFactory
import com.dreampany.cast.worker.NotifyWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Created by roman on 2019-04-19
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
@Module
abstract class WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(NotifyWorker::class)
    abstract fun bindNotifyWorker(worker: NotifyWorker.Factory): IWorkerFactory<out ListenableWorker>

    @Singleton
    @Binds
    abstract fun bindWorkerFactory(factory: WorkerInjectorFactory): WorkerFactory
}