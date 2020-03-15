package com.dreampany.common.misc.func

import androidx.lifecycle.MutableLiveData
import com.dreampany.common.data.enums.Action
import com.dreampany.common.data.enums.BaseType
import com.dreampany.common.data.enums.State
import com.dreampany.common.data.model.Response
import javax.inject.Inject

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResponseMapper
@Inject constructor(

) {

    fun <T, X : BaseType, Y : BaseType> response(
        live: MutableLiveData<Response<T, X, Y>>,
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
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

    fun <T, X : BaseType, Y : BaseType> response(
        live: MutableLiveData<Response<T, X, Y>>,
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        live.value = Response.Error(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            error = error
        )
    }

    fun <T, X : BaseType, Y : BaseType> response(
        live: MutableLiveData<Response<T, X, Y>>,
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T
    ) {
        live.value = Response.Result(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            data = data
        )
    }

    fun <T, X : BaseType, Y : BaseType> responseWithProgress(
        live: MutableLiveData<Response<T, X, Y>>,
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        response(live, type, subtype, state, action, progress = false)
        live.value = Response.Error(type, subtype, state, action, error)
    }

    fun <T, X : BaseType, Y : BaseType> responseWithProgress(
        live: MutableLiveData<Response<T, X, Y>>,
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T
    ) {
        response(live, type, subtype, state, action, false)
        live.value = Response.Result(
            type = type,
            subtype = subtype,
            state = state,
            action = action,
            data = data
        )
    }

    fun <T, X : BaseType, Y : BaseType> responseEmpty(
        live: MutableLiveData<Response<T, X, Y>>,
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T?
    ) {
        live.value = Response.Empty(type, subtype, state, action, data)
    }

    fun <T, X : BaseType, Y : BaseType> responseEmptyWithProgress(
        live: MutableLiveData<Response<T, X, Y>>,
        type: X,
        subtype: Y,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T?
    ) {
        response(live, type, subtype, state, action, progress = false)
        live.value = Response.Empty(type, subtype, state, action, data)
    }
}