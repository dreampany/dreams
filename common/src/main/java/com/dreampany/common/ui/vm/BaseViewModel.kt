package com.dreampany.common.ui.vm

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.dreampany.common.data.enums.Action
import com.dreampany.common.data.enums.BaseType
import com.dreampany.common.data.enums.State
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.extension.reObserve
import com.dreampany.common.misc.func.ResponseMapper
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
abstract class BaseViewModel<T, I, U, X : BaseType, Y : BaseType>
protected constructor(
    application: Application,
    protected val rm: ResponseMapper
) : AndroidViewModel(application), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry

    protected val singleOwners: MutableList<LifecycleOwner>
    protected val multipleOwners: MutableList<LifecycleOwner>

    protected val output: MutableLiveData<Response<I, X, Y>>
    protected val outputs: MutableLiveData<Response<List<I>, X, Y>>

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
        job.cancel()
        singleOwners.forEach {
            output.removeObservers(it)
        }
        multipleOwners.forEach {
            outputs.removeObservers(it)
        }
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED)
        super.onCleared()
    }

    fun subscribe(owner: LifecycleOwner, observer: Observer<Response<I, X, Y>>) {
        singleOwners.add(owner)
        output.reObserve(owner, observer)
    }

    fun subscribes(owner: LifecycleOwner, observer: Observer<Response<List<I>, X, Y>>) {
        multipleOwners.add(owner)
        outputs.reObserve(owner, observer)
    }

    fun postProgressSingle(
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        progress: Boolean
    ) = rm.response(output, type, subtype, state, action, progress)

    fun postProgressMultiple(
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        progress: Boolean
    ) = rm.response(outputs, type, subtype, state, action, progress)

    fun postSingle(
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable? = null,
        result: I? = null,
        showProgress: Boolean
    ) = if (showProgress) {
        error?.let { rm.responseWithProgress(output, type, subtype, state, action, it) }
        result?.let { rm.responseWithProgress(output, type, subtype, state, action, it) }
    } else {
        error?.let { rm.response(output, type, subtype, state, action, it) }
        result?.let { rm.response(output, type, subtype, state, action, it) }
    }

    fun postMultiple(
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable? = null,
        result: List<I>? = null,
        showProgress: Boolean
    ) = if (showProgress) {
        error?.let { rm.responseWithProgress(outputs, type, subtype, state, action, it) }
        result?.let { rm.responseWithProgress(outputs, type, subtype, state, action, it) }
    } else {
        error?.let { rm.response(outputs, type, subtype, state, action, it) }
        result?.let { rm.response(outputs, type, subtype, state, action, it) }
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