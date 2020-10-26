package com.dreampany.framework.ui.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.BaseParcel
import com.dreampany.framework.data.model.Response
import com.dreampany.framework.misc.exts.reObserve
import com.dreampany.framework.misc.func.ResponseMapper
import com.dreampany.framework.misc.func.SmartError
import com.dreampany.framework.ui.model.UiTask
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
        IN : BaseParcel,
        OUT,
        X : UiTask<T, S, ST, A, IN>>
protected constructor(
    application: Application,
    protected val rm: ResponseMapper
) : AndroidViewModel(application), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry

    protected val singleOwners: MutableList<LifecycleOwner>
    protected val multipleOwners: MutableList<LifecycleOwner>

    //protected val status: MutableLiveData<Status>
    protected val output: MutableLiveData<Response<T, S, ST, A, OUT>>
    protected val outputs: MutableLiveData<Response<T, S, ST, A, List<OUT>>>

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

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

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

    protected val context : Context
        get() = getApplication()

    /*fun subscribeStatus(owner: LifecycleOwner, callback: (status: Status) -> Unit) {
        status.observe(owner, Observer { result ->
            if (result == null) {
                callback(Status.DEFAULT)
            } else {
                callback(result)
            }
        })
    }*/

    fun subscribe(owner: LifecycleOwner, observer: Observer<Response<T, S, ST, A, OUT>>) {
        singleOwners.add(owner)
        output.reObserve(owner, observer)
    }

    fun subscribes(owner: LifecycleOwner, observer: Observer<Response<T, S, ST, A, List<OUT>>>) {
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
        error: SmartError? = null,
        result: OUT? = null,
        showProgress: Boolean
    ) = if (showProgress) {
        if (error != null) {
            rm.responseWithProgress(output, type, subtype, state, action, error)
        } else{
            rm.responseWithProgress(output, type, subtype, state, action, result)
        }
    } else {
        if (error != null) {
            rm.response(output, type, subtype, state, action, error)
        } else {
            rm.response(output, type, subtype, state, action, result)
        }
    }

    fun postMultiple(
        type: T,
        subtype: S,
        state: ST,
        action: A,
        error: SmartError? = null,
        result: List<OUT>? = null,
        showProgress: Boolean
    ) = if (showProgress) {
        if (error != null) {
            rm.responseWithProgress(outputs, type, subtype, state, action, error)
        } else {
            rm.responseWithProgress(outputs, type, subtype, state, action, result)
        }
    } else {
        if (error != null) {
            rm.response(outputs, type, subtype, state, action, error)
        } else {
            rm.response(outputs, type, subtype, state, action, result)
        }
    }
}