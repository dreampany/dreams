package com.dreampany.common.data.model

import com.dreampany.common.data.enums.Action
import com.dreampany.common.data.enums.BaseType
import com.dreampany.common.data.enums.State
import com.dreampany.common.ui.enums.UiState

/**
 * Created by roman on 14/3/20
 * Copyright (c) 2020 bjit. All rights reserved.
 * hawladar.roman@bjitgroup.com
 * Last modified $file.lastModified
 */
sealed class Response<T, X : BaseType, Y : BaseType> {

    data class UiResponse<X : BaseType, Y : BaseType>(
        val type: X,
        val subtype: Y,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        var uiState: UiState = UiState.DEFAULT
    )

    data class Progress<T, X : BaseType, Y : BaseType>(
        val type: X,
        val subtype: Y,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val progress: Boolean
    ) : Response<T, X, Y>()

    data class Error<T, X : BaseType, Y : BaseType>(
        val type: X,
        val subtype: Y,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val error: Throwable
    ) : Response<T, X, Y>()

    data class Result<T, X : BaseType, Y : BaseType>(
        val type: X,
        val subtype: Y,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val data: T
    ) : Response<T, X, Y>()

    data class Empty<T, X : BaseType, Y : BaseType>(
        val type: X,
        val subtype: Y,
        val state: State = State.DEFAULT,
        val action: Action = Action.DEFAULT,
        val data: T?
    ) : Response<T, X, Y>()

    companion object {
        fun <X : BaseType, Y : BaseType> response(
            type: X,
            subtype: Y,
            state: State,
            action: Action,
            uiState: UiState
        ): UiResponse<X, Y> = UiResponse(type, subtype, state, action, uiState)

        fun <T, X : BaseType, Y : BaseType> response(
            type: X,
            subtype: Y,
            state: State,
            action: Action,
            progress: Boolean
        ): Response<T, X, Y> =
            Progress(type, subtype, state, action, progress)

        fun <T, X : BaseType, Y : BaseType> response(
            type: X,
            subtype: Y,
            state: State,
            action: Action,
            error: Throwable
        ): Response<T, X, Y> =
            Error(type, subtype, state, action, error)

        fun <T, X : BaseType, Y : BaseType> response(
            type: X,
            subtype: Y,
            state: State,
            action: Action,
            data: T
        ): Response<T, X, Y> =
            Result(type, subtype, state, action, data)

        fun <T, X : BaseType, Y : BaseType> responseEmpty(
            type: X,
            subtype: Y,
            state: State = State.DEFAULT,
            action: Action = Action.DEFAULT,
            data: T?
        ): Response<T, X, Y> =
            Empty(type, subtype, state, action, data)
    }
}