package com.dreampany.frame.ui.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.dreampany.frame.misc.AppExecutor
import com.dreampany.frame.misc.ResponseMapper
import com.dreampany.frame.misc.RxMapper

/**
 * Created by roman on 2020-01-02
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
abstract class BaseViewModel<T>
protected constructor(
    application: Application,
    protected val rx: RxMapper,
    protected val ex: AppExecutor,
    protected val rm: ResponseMapper
) : AndroidViewModel(application), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry
    protected val output: MutableLiveData<Pair<T?, Throwable?>>
    protected val outputs: MutableLiveData<Pair<List<T>?, Throwable?>>

    init {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED)

        output = MutableLiveData()
        outputs = MutableLiveData()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCleared() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED)
        super.onCleared()
    }

    fun subscribe(owner: LifecycleOwner, onResult: (item: T?, error: Throwable?) -> Unit) {
        output.observe(owner, Observer { result ->
            if (result == null) {
                onResult(null, EmptyException())
            } else {
                onResult(result.first, result.second)
            }
        })
    }

    fun subscribes(owner: LifecycleOwner, onResult: (items: List<T>?, error: Throwable?) -> Unit) {
        outputs.observe(owner, Observer { result ->
            if (result == null) {
                onResult(null, EmptyException())
            } else {
                onResult(result.first, result.second)
            }
        })
    }
}