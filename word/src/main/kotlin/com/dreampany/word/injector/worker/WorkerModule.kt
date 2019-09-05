package com.dreampany.word.injector.worker

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.dreampany.framework.misc.WorkerKey
import com.dreampany.framework.worker.factory.IWorkerFactory
import com.dreampany.framework.worker.factory.WorkerInjectorFactory
import com.dreampany.word.worker.NotifyWorker
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