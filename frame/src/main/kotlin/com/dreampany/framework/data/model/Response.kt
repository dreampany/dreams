package com.dreampany.framework.data.model

import com.dreampany.framework.data.enums.Action
import com.dreampany.framework.data.enums.State
import com.dreampany.framework.ui.enums.UiState


/**
 * Created by Hawladar Roman on 5/26/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
sealed class Response<T> {

    data class UiResponse(
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        var uiState: UiState = UiState.DEFAULT
    )

    data class Progress<T>(
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val loading: Boolean
    ) : Response<T>()

    data class Failure<T>(
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val error: Throwable
    ) : Response<T>()

    data class Result<T>(
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val data: T
    ) : Response<T>()

    data class Empty<T>(
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val data: T?
    ) : Response<T>()

    companion object {
        fun response(state: State, action: Action, uiState: UiState): UiResponse = UiResponse(state, action, uiState)

        fun <T> response(state: State, action: Action, loading: Boolean): Response<T> =
            Progress(state, action, loading)

        fun <T> response(state: State, action: Action, error: Throwable): Response<T> =
            Failure(state, action, error)

        fun <T> response(state: State, action: Action, data: T): Response<T> =
            Result(state, action, data)

        fun <T> responseEmpty(
            state: State = State.DEFAULT,
            action: Action = Action.DEFAULT,
            data: T?
        ): Response<T> =
            Empty(state, action, data)
    }
}