package com.dreampany.common.ui.vm

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.dreampany.common.data.enums.*
import com.dreampany.common.data.model.BaseParcel
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.extension.reObserve
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.ui.model.UiTask
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
abstract class BaseViewModel<
        T : BaseType,
        S : BaseSubtype,
        ST : BaseState,
        A : BaseAction,
        I : BaseParcel,
        X : UiTask<T, S, ST, A, I>>
protected constructor(
    application: Application,
    protected val rm: ResponseMapper
) : AndroidViewModel(application), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry

    protected val singleOwners: MutableList<LifecycleOwner>
    protected val multipleOwners: MutableList<LifecycleOwner>

    //protected val status: MutableLiveData<Status>
    protected val output: MutableLiveData<Response<T, S, ST, A, I>>
    protected val outputs: MutableLiveData<Response<T, S, ST, A, List<I>>>

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

    /*fun subscribeStatus(owner: LifecycleOwner, callback: (status: Status) -> Unit) {
        status.observe(owner, Observer { result ->
            if (result == null) {
                callback(Status.DEFAULT)
            } else {
                callback(result)
            }
        })
    }*/

    fun subscribe(owner: LifecycleOwner, observer: Observer<Response<T, S, ST, A, I>>) {
        singleOwners.add(owner)
        output.reObserve(owner, observer)
    }

    fun subscribes(owner: LifecycleOwner, observer: Observer<Response<T, S, ST, A, List<I>>>) {
        multipleOwners.add(owner)
        outputs.reObserve(owner, observer)
    }

    fun postProgressSingle(
        type: T,
        subtype: S,
        state: ST,
        action: A,
        progress: Boolean
    ) = rm.response(output, type, subtype, state, action, progress)

    fun postProgressMultiple(
        type: T,
        subtype: S,
        state: ST,
        action: A,
        progress: Boolean
    ) = rm.response(outputs, type, subtype, state, action, progress)

    fun postSingle(
        type: T,
        subtype: S,
        state: ST,
        action: A,
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
        type: T,
        subtype: S,
        state: ST,
        action: A,
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
}