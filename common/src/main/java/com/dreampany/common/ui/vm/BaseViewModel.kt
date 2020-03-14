package com.dreampany.common.ui.vm

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.extension.reObserve
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
/**
 * B2 - B1 = Added
 * B1 - B2 = Removed
 * B1 - Removed = Updated
 *
 * T = Model
 * X = Ui Model Item
 * Y = UiTask<T>
 */
abstract class BaseViewModel<T, I, U>
protected constructor(
    application: Application
) : AndroidViewModel(application), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry

    protected val singleOwners: MutableList<LifecycleOwner>
    protected val multipleOwners: MutableList<LifecycleOwner>

    protected val output: MutableLiveData<Response<I>>
    protected val outputs: MutableLiveData<Response<List<I>>>

    protected val job: Job
    protected val uiScope: CoroutineScope

    init {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.setCurrentState(Lifecycle.State.INITIALIZED)

        singleOwners = Collections.synchronizedList(ArrayList<LifecycleOwner>())
        multipleOwners = Collections.synchronizedList(ArrayList<LifecycleOwner>())

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
        singleOwners.forEach {
            output.removeObservers(it)
        }
        multipleOwners.forEach {
            outputs.removeObservers(it)
        }
        super.onCleared()
    }

    fun subscribe(owner: LifecycleOwner, observer: Observer<Response<I>>) {
        singleOwners.add(owner)
        output.reObserve(owner, observer)
    }

    fun subscribes(owner: LifecycleOwner, observer: Observer<Response<List<I>>>) {
        multipleOwners.add(owner)
        outputs.reObserve(owner, observer)
    }
/*
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
    }*/
}