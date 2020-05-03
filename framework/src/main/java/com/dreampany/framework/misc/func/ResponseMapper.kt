package com.dreampany.framework.misc.func

import androidx.lifecycle.MutableLiveData
import com.dreampany.framework.data.enums.*
import com.dreampany.framework.data.model.Response
import javax.inject.Inject

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResponseMapper
@Inject constructor() {

    fun <T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            R> response(
        live: MutableLiveData<Response<T, S, ST, A, R>>,
        type: T,
        subtype: S,
        state: ST,
        action: A,
        progress: Boolean
    ) {
        live.value = Response.Progress(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            progress = progress
        )
    }

    fun <T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            R> response(
        live: MutableLiveData<Response<T, S, ST, A, R>>,
        type: T,
        subtype: S,
        state: ST,
        action: A,
        error: SmartError
    ) {
        live.value = Response.Error(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            error = error
        )
    }

    fun <T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            R> response(
        live: MutableLiveData<Response<T, S, ST, A, R>>,
        type: T,
        subtype: S,
        state: ST,
        action: A,
        result: R?
    ) {
        live.value = Response.Result(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            result = result
        )
    }

    fun <T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            R> responseWithProgress(
        live: MutableLiveData<Response<T, S, ST, A, R>>,
        type: T,
        subtype: S,
        state: ST,
        action: A,
        error: SmartError
    ) {
        response(live, type, subtype, state, action, progress = false)
        live.value = Response.Error(type, subtype, state, action, error)
    }

    fun <T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            R> responseWithProgress(
        live: MutableLiveData<Response<T, S, ST, A, R>>,
        type: T,
        subtype: S,
        state: ST,
        action: A,
        result: R?
    ) {
        response(live, type, subtype, state, action, false)
        live.value = Response.Result(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            result = result
        )
    }

    /*fun <T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            I> responseEmpty(
        live: MutableLiveData<Response<T, S, ST, A, I>>,
        type: T,
        subtype: S,
        state: ST,
        action: A,
        result: I?
    ) {
        live.value = Response.Empty(type, subtype, state, action, result)
    }*/

    /*fun <T : BaseType,
            S : BaseSubtype,
            ST : BaseState,
            A : BaseAction,
            I> responseEmptyWithProgress(
        live: MutableLiveData<Response<T, S, ST, A, I>>,
        type: T,
        subtype: S,
        state: ST,
        action: A,
        result: I?
    ) {
        response(live, type, subtype, state, action, progress = false)
        live.value = Response.Empty(type, subtype, state, action, result)
    }*/
}