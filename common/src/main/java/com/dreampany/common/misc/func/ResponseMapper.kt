package com.dreampany.common.misc.func

import androidx.lifecycle.MutableLiveData
import com.dreampany.common.data.enums.BaseEnum
import com.dreampany.common.data.model.Response
import javax.inject.Inject

/**
 * Created by roman on 7/13/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class ResponseMapper @Inject constructor() {

    fun <T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I> response(
        live: MutableLiveData<Response<T, ST, S, A, I>>,
        type: T,
        subtype: ST,
        state: S,
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

    fun <T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I> response(
        live: MutableLiveData<Response<T, ST, S, A, I>>,
        type: T,
        subtype: ST,
        state: S,
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

    fun <T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I> response(
        live: MutableLiveData<Response<T, ST, S, A, I>>,
        type: T,
        subtype: ST,
        state: S,
        action: A,
        result: I?
    ) {
        live.value = Response.Result(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            result = result
        )
    }

    fun <T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I> responseWithProgress(
        live: MutableLiveData<Response<T, ST, S, A, I>>,
        type: T,
        subtype: ST,
        state: S,
        action: A,
        error: SmartError
    ) {
        response(live, type, subtype, state, action, progress = false)
        live.value = Response.Error(type, subtype, state, action, error)
    }

    fun <T : BaseEnum, ST : BaseEnum, S : BaseEnum, A : BaseEnum, I> responseWithProgress(
        live: MutableLiveData<Response<T, ST, S, A, I>>,
        type: T,
        subtype: ST,
        state: S,
        action: A,
        result: I?
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
}