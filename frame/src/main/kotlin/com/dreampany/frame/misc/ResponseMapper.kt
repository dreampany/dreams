package com.dreampany.frame.misc

import com.dreampany.frame.data.enums.Action
import com.dreampany.frame.data.enums.State
import com.dreampany.frame.data.model.Response
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by roman on 2020-01-01
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
class ResponseMapper @Inject constructor() {

    fun <T> response(
        subject: PublishSubject<Response<T>>,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        loading: Boolean
    ) {
        subject.onNext(Response.Progress(state, action, loading))
    }

    fun <T> response(
        subject: PublishSubject<Response<T>>,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        subject.onNext(Response.Failure(state, action, error))
    }

    fun <T> response(
        subject: PublishSubject<Response<T>>,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T
    ) {
        subject.onNext(Response.Result(state = state, action = action, data = data))
    }

    fun <T> responseWithProgress(
        subject: PublishSubject<Response<T>>,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        error: Throwable
    ) {
        response(subject, state, action, false)
        subject.onNext(Response.Failure(state, action, error))
    }

    fun <T> responseWithProgress(
        subject: PublishSubject<Response<T>>,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T
    ) {
        response(subject, state, action, false)
        subject.onNext(Response.Result(state = state, action = action, data = data))
    }

    fun <T> responseEmpty(
        subject: PublishSubject<Response<T>>,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T?
    ) {
        subject.onNext(Response.Empty(state, action, data))
    }

    fun <T> responseEmptyWithProgress(
        subject: PublishSubject<Response<T>>,
        state: State = State.DEFAULT,
        action: Action = Action.DEFAULT,
        data: T?
    ) {
        response(subject, state, action, false)
        subject.onNext(Response.Empty(state, action, data))
    }
}
