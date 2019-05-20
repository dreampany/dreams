package com.dreampany.common.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.dreampany.common.misc.AppExecutors
import com.dreampany.common.misc.RxMapper

/**
 * Created by Roman-372 on 5/20/2019
 * Copyright (c) 2019 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseViewModel protected constructor(
    application: Application, protected val rx: RxMapper, protected val ex: AppExecutors
) : AndroidViewModel(application), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry

    init {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED)
    }

    override fun onCleared() {
        clear()
        super.onCleared()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    open fun clear() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED)

    }
}