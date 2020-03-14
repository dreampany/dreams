package com.dreampany.common.ui.vm

import android.app.Application
import androidx.lifecycle.*
import com.dreampany.common.misc.func.AppExecutor
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.RxMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseViewModel<T>
protected constructor(
    application: Application,
    protected val ex: AppExecutor,
    protected val rx: RxMapper,
    protected val rm: ResponseMapper
) : AndroidViewModel(application), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry
    protected val output: MutableLiveData<Pair<T?, Throwable?>>
    protected val outputs: MutableLiveData<Pair<List<T>?, Throwable?>>

    protected val job: Job
    protected val uiScope: CoroutineScope
    init {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED)

        output = MutableLiveData()
        outputs = MutableLiveData()
        job = SupervisorJob()
        uiScope = CoroutineScope(Dispatchers.Main + job)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCleared() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED)
        job.cancel()
        super.onCleared()
    }

    fun subscribe(owner: LifecycleOwner, onResult: (item: T?, error: Throwable?) -> Unit) {
        output.observe(owner, Observer { result ->
            if (result == null) {
                onResult(null, NullPointerException())
            } else {
                onResult(result.first, result.second)
            }
        })
    }

    fun subscribes(owner: LifecycleOwner, onResult: (items: List<T>?, error: Throwable?) -> Unit) {
        outputs.observe(owner, Observer { result ->
            if (result == null) {
                onResult(null, NullPointerException())
            } else {
                onResult(result.first, result.second)
            }
        })
    }
}