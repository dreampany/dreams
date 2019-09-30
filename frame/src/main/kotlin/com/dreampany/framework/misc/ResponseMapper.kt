package com.dreampany.framework.misc

import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.data.model.Response
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by Hawladar Roman on 5/26/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
class ResponseMapper @Inject constructor() {

    fun <T> response(subject: PublishSubject<Response<T>>, loading: Boolean) {
        subject.onNext(Response.Progress(loading))
    }

    fun <T> response(subject: PublishSubject<Response<T>>, error: Throwable) {
        subject.onNext(Response.Failure(error))
    }

    fun <T> response(subject: PublishSubject<Response<T>>, state : State = State.DEFAULT, action: Action = Action.DEFAULT, data: T) {
        subject.onNext(Response.Result(state = state, action = action, data =  data))
    }

    fun <T> responseWithProgress(subject: PublishSubject<Response<T>>, error: Throwable) {
        response(subject, false)
        subject.onNext(Response.Failure(error))
    }

    fun <T> responseWithProgress(subject: PublishSubject<Response<T>>, state : State = State.DEFAULT, action: Action = Action.DEFAULT, data: T) {
        response(subject, false)
        subject.onNext(Response.Result(state = state, action = action, data =  data))
    }

    fun <T> responseEmpty(subject: PublishSubject<Response<T>>, data: T?) {
        subject.onNext(Response.Empty(data))
    }

    fun <T> responseEmptyWithProgress(subject: PublishSubject<Response<T>>, data: T?) {
        response(subject, false)
        subject.onNext(Response.Empty(data))
    }
}
