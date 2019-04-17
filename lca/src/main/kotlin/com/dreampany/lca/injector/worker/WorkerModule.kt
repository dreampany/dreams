package com.dreampany.lca.injector.worker

import androidx.work.ListenableWorker
import com.dreampany.frame.worker.WorkerKey
import com.dreampany.frame.worker.factory.IWorkerFactory
import com.dreampany.lca.worker.NotifyWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Roman-372 on 4/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */

@Module
interface WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(NotifyWorker::class)
    fun bindGithubUsersWorker(worker: NotifyWorker.Factory): IWorkerFactory<out ListenableWorker>
}