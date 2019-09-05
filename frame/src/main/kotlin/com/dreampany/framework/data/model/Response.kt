package com.dreampany.framework.data.model

import com.dreampany.framework.data.enums.Action


/**
 * Created by Hawladar Roman on 5/26/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
sealed class Response<T> {

    data class Progress<T>(val loading: Boolean) : Response<T>()
    data class Failure<T>(val error: Throwable) : Response<T>()
    data class Result<T>(val action: Action, val data: T) : Response<T>()
    data class Empty<T>(val data: T?) : Response<T>()

    companion object {
        fun <T> response(loading: Boolean): Response<T> = Progress(loading)
        fun <T> response(error: Throwable): Response<T> = Failure(error)
        fun <T> response(action: Action, data: T): Response<T> = Result(action, data)
        fun <T> responseEmpty(data: T?): Response<T> = Empty(data)
    }
}