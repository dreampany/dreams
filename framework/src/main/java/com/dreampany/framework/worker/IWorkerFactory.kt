package com.dreampany.framework.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

/**
 * Created by roman on 31/5/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
interface IWorkerFactory<T : ListenableWorker> {
    fun create(context: Context, params: WorkerParameters): T
}