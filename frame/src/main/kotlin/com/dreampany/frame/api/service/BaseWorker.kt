package com.dreampany.frame.api.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import javax.inject.Inject

/**
 * Created by Roman-372 on 4/17/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseWorker constructor(
    private val context: Context,
    private val params: WorkerParameters
) : Worker(context, params) {
}