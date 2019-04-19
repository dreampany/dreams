package com.dreampany.frame.worker.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

/**
 * Created by Roman-372 on 4/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface IWorkerFactory<T : ListenableWorker> {
    fun create(context: Context, params: WorkerParameters): T
}