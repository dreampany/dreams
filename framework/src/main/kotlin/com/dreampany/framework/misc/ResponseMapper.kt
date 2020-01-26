package com.dreampany.framework.misc

import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.enums.Subtype
import com.dreampany.framework.data.enums.Type
import com.dreampany.framework.data.model.Response
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by roman on 2020-01-12
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResponseMapper
@Inject constructor() {

    fun <T> response(
        subject: PublishSubject<Response<T>>,
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
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
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        subject.onNext(Response.Failure(type = type, subtype = subtype, state = state, action = action, error =  error))
    }

    fun <T> response(
        subject: PublishSubject<Response<T>>,
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T
    ) {
        subject.onNext(Response.Result(type = type, subtype = subtype, state = state, action = action, data = data))
    }

    fun <T> responseWithProgress(
        subject: PublishSubject<Response<T>>,
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        response(subject, type, subtype, state, action, loading = false)
        subject.onNext(Response.Failure(type, subtype, state, action, error))
    }

    fun <T> responseWithProgress(
        subject: PublishSubject<Response<T>>,
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T
    ) {
        response(subject, type, subtype, state, action, false)
        subject.onNext(Response.Result(type = type, subtype = subtype, state = state, action = action, data = data))
    }

    fun <T> responseEmpty(
        subject: PublishSubject<Response<T>>,
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T?
    ) {
        subject.onNext(Response.Empty(type, subtype, state, action, data))
    }

    fun <T> responseEmptyWithProgress(
        subject: PublishSubject<Response<T>>,
        type: Type = Type.DEFAULT,
        subtype: Subtype = Subtype.DEFAULT,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T?
    ) {
        response(subject, type, subtype, state, action, loading = false)
        subject.onNext(Response.Empty(type, subtype, state, action, data))
    }
}
