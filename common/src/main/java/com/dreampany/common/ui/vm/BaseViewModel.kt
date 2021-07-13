package com.dreampany.common.ui.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dreampany.common.data.enums.BaseEnum
import com.dreampany.common.data.model.BaseParcel
import com.dreampany.common.data.model.Response
import com.dreampany.common.misc.exts.reObserve
import com.dreampany.common.misc.func.ResponseMapper
import com.dreampany.common.misc.func.SmartError
import com.dreampany.common.ui.model.UiTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by roman on 7/10/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
abstract class BaseViewModel<T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I : BaseParcel, O, X : UiTask<T, ST, S, A, I>> protected constructor(
    application: Application,
    protected val rm: ResponseMapper
) : AndroidViewModel(application) {

    private val singleOwners: MutableList<LifecycleOwner>
    private val multipleOwners: MutableList<LifecycleOwner>

    private val output: MutableLiveData<Response<T, ST, S, A, O>>
    private val outputs: MutableLiveData<Response<T, ST, S, A, List<O>>>

    private val job: Job
    private val uiScope: CoroutineScope

    init {
        singleOwners = Collections.synchronizedList(ArrayList<LifecycleOwner>())
        multipleOwners = Collections.synchronizedList(ArrayList<LifecycleOwner>())

        output = MutableLiveData()
        outputs = MutableLiveData()

        job = SupervisorJob()
        uiScope = CoroutineScope(Dispatchers.Main + job)
    }

    override fun onCleared() {
        super.onCleared()
        singleOwners.forEach {
            output.removeObservers(it)
        }
        multipleOwners.forEach {
            outputs.removeObservers(it)
        }
        job.cancel()
    }

    protected val context: Context
        get() = getApplication()

    fun subscribe(owner: LifecycleOwner, observer: Observer<Response<T, ST, S, A, O>>) {
        singleOwners.add(owner)
        output.reObserve(owner, observer)
    }

    fun subscribes(owner: LifecycleOwner, observer: Observer<Response<T, ST, S, A, List<O>>>) {
        multipleOwners.add(owner)
        outputs.reObserve(owner, observer)
    }

    fun postSingle(
        type: T,
        subtype: ST,
        state: S,
        action: A,
        progress: Boolean
    ) = rm.response(output, type, subtype, state, action, progress)

    fun postMultiple(
        type: T,
        subtype: ST,
        state: S,
        action: A,
        progress: Boolean
    ) = rm.response(outputs, type, subtype, state, action, progress)

    fun postSingle(
        type: T,
        subtype: ST,
        state: S,
        action: A,
        error: SmartError? = null,
        result: O? = null,
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
        subtype: ST,
        state: S,
        action: A,
        error: SmartError? = null,
        result: List<O>? = null,
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