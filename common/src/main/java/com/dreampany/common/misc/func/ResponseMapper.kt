package com.dreampany.common.misc.func

import com.dreampany.common.data.enums.Action
import com.dreampany.common.data.enums.BaseType
import com.dreampany.common.data.enums.State
import com.dreampany.common.data.model.Response
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResponseMapper
@Inject constructor() {

    fun <T> response(
        subject: PublishSubject<Response<T>>,
        type: BaseType,
        subtype: BaseType,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        loading: Boolean
    ) {
        subject.onNext(
            Response.Progress(
                type = type, subtype = subtype, state = state, action = action,
                loading = loading
            )
        )
    }

    fun <T> response(
        subject: PublishSubject<Response<T>>,
        type: BaseType,
        subtype: BaseType,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        subject.onNext(Response.Failure(type = type, subtype = subtype, state = state, action = action, error =  error))
    }

    fun <T> response(
        subject: PublishSubject<Response<T>>,
        type: BaseType,
        subtype: BaseType,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T
    ) {
        subject.onNext(Response.Result(type = type, subtype = subtype, state = state, action = action, data = data))
    }

    fun <T> responseWithProgress(
        subject: PublishSubject<Response<T>>,
        type: BaseType,
        subtype: BaseType,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        response(subject, type, subtype, state, action, loading = false)
        subject.onNext(Response.Failure(type, subtype, state, action, error))
    }

    fun <T> responseWithProgress(
        subject: PublishSubject<Response<T>>,
        type: BaseType,
        subtype: BaseType,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T
    ) {
        response(subject, type, subtype, state, action, false)
        subject.onNext(Response.Result(type = type, subtype = subtype, state = state, action = action, data = data))
    }

    fun <T> responseEmpty(
        subject: PublishSubject<Response<T>>,
        type: BaseType,
        subtype: BaseType,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T?
    ) {
        subject.onNext(Response.Empty(type, subtype, state, action, data))
    }

    fun <T> responseEmptyWithProgress(
        subject: PublishSubject<Response<T>>,
        type: BaseType,
        subtype: BaseType,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T?
    ) {
        response(subject, type, subtype, state, action, loading = false)
        subject.onNext(Response.Empty(type, subtype, state, action, data))
    }
}